package fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.plutecoder.geoattendance.LoginActivity;
import com.plutecoder.geoattendance.MainActivity;
import com.plutecoder.geoattendance.R;
import com.plutecoder.geoattendance.SignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import settergetter.ClockOnOffGtSt;
import settergetter.UserGtSt;
import utils.NetworkUtility;
import utils.PrefHelperOffline;
import utils.PreferenceHelper;
import utils.Utils;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class Fragment_Mytime extends Fragment implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 6000;
    View view;
    Context context;
    int Clockstatus;
    private boolean mRequestingLocationUpdates;
    private ProgressBar mProgress;
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String strLocation, strLatitude, strLongitude;
    private RecyclerView Recyclerleavesummary;
    RelativeLayout rosterreqrelative;
    Button btnclockon, btnclockoff;
    BiometricPrompt myBiometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    boolean isclockonoroff = true;
    ProgressBar pb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.testtime, container, false);
        view.findViewById(R.id.Location).setOnClickListener(this);
        pb = (ProgressBar) view.findViewById(R.id.LoginProgress);
        rosterreqrelative = view.findViewById(R.id.rosterreqrelative);
        btnclockon = view.findViewById(R.id.btnclockon);
        btnclockoff = view.findViewById(R.id.btnclockoff);
        mProgress = view.findViewById(R.id.ProgressbarMyTime);
        mProgress.setVisibility(View.INVISIBLE);

        try {
            if (MainActivity.isfingureprintavailable) {

                Executor newExecutor = Executors.newSingleThreadExecutor();

                myBiometricPrompt = new BiometricPrompt(getActivity(), newExecutor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        } else {
                            //Log.d(TAG, "An unrecoverable error occurred");
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Log.d(TAG, "Fingerprint recognised successfully");
                        try {
                            if(isclockonoroff) {
                                ClockOnDialog();
                            }else{
                                ClockOffDialog();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        //  Log.d(TAG, "Fingerprint not recognised");
                    }


                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("fingerprint authentication")
                        .setSubtitle("You are in location you can Clock ON/OFF")
                        .setDescription("Before Clock On/Off, we are going to make sure you are the right person or not for do this activity.")
                        .setNegativeButtonText("Cancel")
                        .build();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        try {

            if (true) {

                rosterreqrelative.setVisibility(View.VISIBLE);
                view.findViewById(R.id.BtnClock).setVisibility(View.GONE);
            } else {
                rosterreqrelative.setVisibility(View.GONE);
                view.findViewById(R.id.BtnClock).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        mRequestingLocationUpdates = true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        mRequestingLocationUpdates = false;

                        if (Clockstatus != -1) {
                            ((TextView) view.findViewById(R.id.Location)).setText(Html.fromHtml("Unable to find the Location.<br />Please turn on the <u>Device Location!</u>"));
                            view.findViewById(R.id.BtnClock).setVisibility(View.GONE);
                            view.findViewById(R.id.Exceptioncount).setVisibility(View.INVISIBLE);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });


        context = getContext();
        setListener();
        setFont();
        checkPlayServices();
        ((TextClock) view.findViewById(R.id.Digitalclock)).setFormat24Hour("hh:mm:ss");
        ((TextClock) view.findViewById(R.id.Date)).setFormat24Hour("MMM dd, yyyy");
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnclockon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.isfingureprintavailable) {
                    isclockonoroff = true;
                    myBiometricPrompt.authenticate(promptInfo);
                } else {
                    ClockOnDialog();
                }




            }
        });

        btnclockoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.isfingureprintavailable) {
                    isclockonoroff = false;
                    myBiometricPrompt.authenticate(promptInfo);
                } else {
                    IfClockedOffForDay();
                }


            }
        });

        return view;
    }
    public void ClockOffDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtility.getConnectivityStatusString(getContext())) {
                    String Msg = "Are you sure you want to Clock-Off?";
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext(), R.style.MyAlertDialogStyle);
                    alertDialogBuilder.setTitle("Clock Off");
                    alertDialogBuilder
                            .setMessage(
                                    Msg)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {

                                            // if (sharedPreferenceUtils.IsGeoFenceEnabled()) {
                                            if (true) {

                                                try {

                                                    boolean inlocation = ((MainActivity) getActivity()).IsinOrgLocation();
                                                    if (inlocation) {
                                                        IfClockedOffForDay();

                                                    } else {
                                                        Toast.makeText(getActivity(), "You are not within work location to act. Thank you!", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getActivity(), "exception" , Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    NetworkUtility.showAlertDialog(getActivity(), "Clock Entry Details", "Please check your internet connection.", false);
                    //Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void ClockOnDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "You are trying clock on=" + isclockonoroff, Toast.LENGTH_SHORT).show();
                if (NetworkUtility.getConnectivityStatusString(getContext())) {
                    String Msg = "Are you sure you want to Clock-On?";
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext(), R.style.MyAlertDialogStyle);
                    alertDialogBuilder.setTitle("Clock Entry Details");
                    alertDialogBuilder
                            .setMessage(
                                    Msg)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {

                                            // if (sharedPreferenceUtils.IsGeoFenceEnabled()) {
                                            if (true) {

                                                try {

                                                    boolean inlocation = ((MainActivity) getActivity()).IsinOrgLocation();
                                                    if (inlocation) {

                                                        IfClockedOnForDay();

                                                    } else {
                                                        Toast.makeText(getActivity(), "You are not within work location to act. Thank you!", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getActivity(), "exception" , Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    NetworkUtility.showAlertDialog(getActivity(), "Clock Entry Details", "Please check your internet connection.", false);
                    //Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && !mRequestingLocationUpdates) {
            //  startLocationUpdates();
        }

        if (NetworkUtility.getConnectivityStatusString(getContext())) {
            // mProgress.setVisibility(View.VISIBLE);
            //  CheckCount();
        } else {
            // Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getcurrenttime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String dateformatted = dateFormat.format(date);
            return dateformatted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void UpdateClockOff(String UidDayobject) {
        try {
pb.setVisibility(View.VISIBLE);
System.out.println("key---------"+UidDayobject);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            //final String myuid = PreferenceHelper.getTodayClockUid(MainActivity.activity);
            // mDatabase.push();
            mDatabase.child("clockonoff").child(PreferenceHelper.getuseruid(getActivity())).child(UidDayobject).child("outtime").setValue(getcurrenttime());
            mDatabase.child("clockonoff").child(PreferenceHelper.getuseruid(getActivity())).child(UidDayobject).child("isclockoffforday").setValue(true).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    try {
                        // pb.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.INVISIBLE);

                        Toast.makeText(getActivity(), "Please try later", Toast.LENGTH_SHORT).show();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    try {
                        //Profile created successfully
                        if (task.isSuccessful()) {
                            pb.setVisibility(View.INVISIBLE);

                            Toast.makeText(getActivity(), "Clock Off Successfully", Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pb.setVisibility(View.INVISIBLE);

                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            pb.setVisibility(View.INVISIBLE);

        }
    }


    public void IfClockedOnForDay() {
        try {
            pb.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("clockonoff").child(PreferenceHelper.getuseruid(MainActivity.activity)).orderByChild("date").equalTo(MainActivity.dateselected);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        System.out.println("---------"+dataSnapshot.toString());
                        if (dataSnapshot.exists())
                        {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {


                                HashMap<String, Object> yourData = (HashMap<String, Object>) snapshot.getValue();
                                if (yourData != null) {

                                    if(yourData.get("isclockonforday").toString().equals("true")){
                                        Toast.makeText(MainActivity.activity, "You already Clocked ON for the day", Toast.LENGTH_LONG).show();
                                        pb.setVisibility(View.INVISIBLE);

                                    }else{
                                        ClockOn(MainActivity.dateselected);
                                        pb.setVisibility(View.INVISIBLE);

                                    }


                                } else {
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            }

                        } else {
                            pb.setVisibility(View.INVISIBLE);
                           // Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                            ClockOn(MainActivity.dateselected);

                        }
                    } catch (Exception e) {
                        pb.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            pb.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

    }

    public void IfClockedOffForDay() {
        try {
            pb.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("clockonoff").child(PreferenceHelper.getuseruid(MainActivity.activity)).orderByChild("date").equalTo(MainActivity.dateselected);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        System.out.println("---------"+dataSnapshot.toString());
                        if (dataSnapshot.exists())
                        {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {


                                HashMap<String, Object> yourData = (HashMap<String, Object>) snapshot.getValue();
                                if (yourData != null) {

                                    if(yourData.get("isclockoffforday").toString().equals("true")){
                                        Toast.makeText(MainActivity.activity, "You already Clocked OFF for the day", Toast.LENGTH_LONG).show();
                                        pb.setVisibility(View.INVISIBLE);

                                    }else{
                                        UpdateClockOff(snapshot.getKey());
                                        pb.setVisibility(View.INVISIBLE);

                                    }


                                } else {
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            }

                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            // Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                            ClockOn(MainActivity.dateselected);

                        }
                    } catch (Exception e) {
                        pb.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            pb.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

    }



    public void ClockOn(final String dateselected) {

        pb.setVisibility(View.VISIBLE);

            ClockOnOffGtSt userGtSt = new ClockOnOffGtSt();
            try {
                userGtSt.setDate(dateselected);
                userGtSt.setIntime(getcurrenttime());
                userGtSt.setOuttime("-");
                userGtSt.setIsweeklyoff(false);
                userGtSt.setIsonleave(false);
                userGtSt.setIsclockonforday(true);
                userGtSt.setIsclockoffforday(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            final String myuid = mDatabase.push().getKey();
            // mDatabase.push();
            mDatabase.child("clockonoff").child(PreferenceHelper.getuseruid(getActivity())).child(myuid).setValue(userGtSt).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    try {
                        // pb.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.INVISIBLE);

                        Toast.makeText(getActivity(), "Please try later", Toast.LENGTH_SHORT).show();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    try {
                        //Profile created successfully
                        if (task.isSuccessful()) {
                            pb.setVisibility(View.INVISIBLE);

                            Toast.makeText(getActivity(), "Clocked On", Toast.LENGTH_SHORT).show();

                            PreferenceHelper.storetodayClockUid(MainActivity.activity, myuid);
                            PreferenceHelper.storeClockOnOffDate(MainActivity.activity, dateselected);
                            Toast.makeText(getActivity(), "Clock On Successfully", Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pb.setVisibility(View.INVISIBLE);

                    }
                }
            });


    }


    public void setclockonoff(final String dateselected) {
        //  pb.setVisibility(View.VISIBLE);
        if (PreferenceHelper.getClockOnOffDate(MainActivity.activity).equals(MainActivity.dateselected)) {
            Toast.makeText(MainActivity.activity, "You already Clocked ON for the day", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "else cond" , Toast.LENGTH_SHORT).show();

            ClockOnOffGtSt userGtSt = new ClockOnOffGtSt();
            try {
                userGtSt.setDate(dateselected);
                userGtSt.setIntime(getcurrenttime());
                userGtSt.setOuttime("-");
                userGtSt.setIsweeklyoff(false);
                userGtSt.setIsonleave(false);
                userGtSt.setIsclockonforday(true);
                userGtSt.setIsclockoffforday(false);
            } catch (Exception e) {
                e.printStackTrace();
            }


            // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            final String myuid = mDatabase.push().getKey();
            // mDatabase.push();
            mDatabase.child("clockonoff").child(PreferenceHelper.getuseruid(getActivity())).child(myuid).setValue(userGtSt).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    try {
                        // pb.setVisibility(View.INVISIBLE);

                        Toast.makeText(getActivity(), "Please try later", Toast.LENGTH_SHORT).show();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    try {
                        //Profile created successfully
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Clocked On", Toast.LENGTH_SHORT).show();

                            PreferenceHelper.storetodayClockUid(MainActivity.activity, myuid);
                            PreferenceHelper.storeClockOnOffDate(MainActivity.activity, dateselected);
                            Toast.makeText(getActivity(), "Clock On Successfully", Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    private void setListener() {

        view.findViewById(R.id.BtnClock).setOnClickListener(this);
        view.findViewById(R.id.Exceptioncount).setOnClickListener(this);
    }

    private void setFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "sanfrancisco.ttf");
        ((TextView) view.findViewById(R.id.BtnClock)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.Digitalclock)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.Number_Cl)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.stringcl)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.Date)).setTypeface(tf, Typeface.NORMAL);
        //  ((TextView) view.findViewById(R.id.LeaveDeatils)).setTypeface(tf, Typeface.ITALIC);
        ((TextView) view.findViewById(R.id.ViewClock)).setTypeface(tf, Typeface.NORMAL);
        //     ((TextView) view.findViewById(R.id.ViewLeavesummary)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.Location)).setTypeface(tf, Typeface.NORMAL);
        // view.findViewById(R.id.Location).setOnClickListener(this);

        ((TextView) view.findViewById(R.id.btnclockoff)).setTypeface(tf, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.btnclockon)).setTypeface(tf, Typeface.NORMAL);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnClock) {


            String Msg = "";
            if (Clockstatus == 0) {
                Msg = "Are you sure you want to Clock-On?";
            } else if (Clockstatus == 1) {
                Msg = "Are you sure you want to Clock-Off?";
            } else if (Clockstatus == 2) {
                Msg = "You are already Clocked Off For The Day!";
            } else if (Clockstatus == -1) {
                Msg = "You are not supposed to use the mobile clock-on feature, \nplease talk to your administrator’";
            }

            if (Clockstatus == -1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext(), R.style.MyAlertDialogStyle);
                alertDialogBuilder.setTitle("Clock Entry Details");
                alertDialogBuilder
                        .setMessage(
                                Msg)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {


                                        dialog.dismiss();
                                    }
                                });


                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();
            } else if (Clockstatus == 2) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext(), R.style.MyAlertDialogStyle);
                alertDialogBuilder.setTitle("Clock Entry Details");
                alertDialogBuilder
                        .setMessage(
                                Msg)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {


                                        dialog.dismiss();
                                    }
                                });


                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();
            } else {
                if (NetworkUtility.getConnectivityStatusString(getContext())) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext(), R.style.MyAlertDialogStyle);
                    alertDialogBuilder.setTitle("Clock Entry Details");
                    alertDialogBuilder
                            .setMessage(
                                    Msg)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {

                                            if (true) {
                                                //shail changes start
                                                if (Clockstatus == 0) {
                                                    try {

                                                        boolean inlocation = ((MainActivity) getActivity()).IsinOrgLocation();
                                                        if (inlocation) {
                                                            // new ProcessUserLogin().execute();
                                                            // Toast.makeText(getActivity(), "in the location====", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "You are not within work location to act. Thank you!", Toast.LENGTH_LONG).show();
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    //System.out.println("clock on already====");
                                                    if (Clockstatus == 1) {
                                                        try {

                                                            boolean inlocation = ((MainActivity) getActivity()).IsinOrgLocation();
                                                            if (inlocation) {
                                                                //  new ProcessUserLogin().execute();
                                                                // Toast.makeText(getActivity(), "in the location====", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Toast.makeText(getActivity(), "You are not within work location to act. Thank you!", Toast.LENGTH_LONG).show();
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        //  new ProcessUserLogin().execute();
                                                    }
                                                }
                                            } else {
                                                // new ProcessUserLogin().execute();
                                            }


                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    NetworkUtility.showAlertDialog(getActivity(), "Clock Entry Details", "Please check your internet connection.", false);
                    //Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }


        } else if (v.getId() == R.id.Exceptioncount) {

        } else if (v.getId() == R.id.Location) {

            if (((TextView) view.findViewById(R.id.Location)).getText().toString().contains("Unable to find the Location.")) {
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //   startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(getActivity(), "For Better Result Updaet play services", Toast.LENGTH_SHORT).show();
                Log.i("SignupActivity", "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        // stopLocationUpdates();
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        boolean gps_enabled = false;
        try {
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        if (mLastLocation != null && Clockstatus != -1) {
            strLatitude = String.valueOf(mLastLocation.getLatitude());
            strLongitude = String.valueOf(mLastLocation.getLongitude());
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            // mProgress.setVisibility(View.VISIBLE);
            List<Address> addresses;
            try {
                if (NetworkUtility.getConnectivityStatusString(getContext())) {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 2);
                    if (addresses.size() > 0) {
                        //   mProgress.setVisibility(View.GONE);
                        ((TextView) view.findViewById(R.id.Location)).setText(addresses.get(0).getSubLocality() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName());
                        strLocation = addresses.get(0).getSubLocality() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();

                    } else {
                        // mProgress.setVisibility(View.GONE);
                        // ((TextView) view.findViewById(R.id.Location)).setText(Html.fromHtml("Unable to find location...<br /> Please enable intenet data/ <br /> Please turn on the <u>location Setting!</u>"));
                    }
                } else {
                    // mProgress.setVisibility(View.GONE);
                    view.findViewById(R.id.BtnClock).setVisibility(View.GONE);
                    view.findViewById(R.id.Exceptioncount).setVisibility(View.INVISIBLE);
                    ((TextView) view.findViewById(R.id.Location)).setText(Html.fromHtml("You're disconnected<br/>Check your Internet Connection and Try Again.</u>"));
                }

            } catch (IOException e) {
                e.printStackTrace();
                view.findViewById(R.id.BtnClock).setVisibility(View.GONE);
                view.findViewById(R.id.Exceptioncount).setVisibility(View.INVISIBLE);
                ((TextView) view.findViewById(R.id.Location)).setText(Html.fromHtml("You're disconnected<br/>Check your Internet Connection and Try Again.</u>"));
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        //  inflater.inflate(R.menu.reimbursement, menu);
    }


}
