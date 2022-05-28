package com.plutecoder.geoattendance;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import settergetter.CategoryMainTM;
import settergetter.UserGtSt;
import utils.Constant;
import utils.PreferenceHelper;
import utils.Typefaces;
import utils.Utility;
import utils.Utils;

import static android.text.TextUtils.isEmpty;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtfullname, edtusername, edtpasword;
    Button btnsignup;
    TextView btnsignin;
    ProgressBar pb;
    TextView txtstrttime, txtendtime;
    String strstarttime = "11:00", strendtime = "5:00";
    private int mYear, mMonth, mDay, mHour, mMinute;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Spinner spdesignation;

    String designation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        mAuth = FirebaseAuth.getInstance();
        pb = findViewById(R.id.Signupprogress);
        txtstrttime = findViewById(R.id.txtstarttime);
        txtendtime = findViewById(R.id.txtenddtime);
        spdesignation = findViewById(R.id.designationspinner);
        btnsignin = (TextView) findViewById(R.id.textView23);
        edtfullname = (EditText) findViewById(R.id.fullname);
        edtusername = (EditText) findViewById(R.id.username);
        edtpasword = (EditText) findViewById(R.id.edtpin);
        btnsignup = (Button) findViewById(R.id.signUpBtn);
        String text = "<font color=#a8a6a6>Already Have an Account?</font> <font color=#5A5A5A>LOGIN.</font>";
        btnsignin.setText(Html.fromHtml(text));
        try {
            if (Typefaces.get(SignupActivity.this, "HelveticaNeue Light") != null) {

                edtfullname.setTypeface(Typefaces.get(SignupActivity.this, "HelveticaNeue Light"));
                edtusername.setTypeface(Typefaces.get(SignupActivity.this, "HelveticaNeue Light"));
                edtpasword.setTypeface(Typefaces.get(SignupActivity.this, "HelveticaNeue Light"));
                btnsignin.setTypeface(Typefaces.get(SignupActivity.this, "HelveticaNeue Light"));
                btnsignup.setTypeface(Typefaces.get(SignupActivity.this, "HelveticaNeue Light"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtstrttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                strstarttime = hourOfDay + ":" + minute;
                                txtstrttime.setText("Start Time :" + hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        txtendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                strendtime = hourOfDay + ":" + minute;
                                txtendtime.setText("Start Time :" + hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(SignupActivity.this , LoginActivity.class));
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (!isEmpty(edtusername.getText().toString()) && !isEmpty(edtfullname.getText().toString())
                        && !isEmpty(edtpasword.getText().toString())
                ) {

                    String strdisplayname = edtfullname.getText().toString();
                    String strusernma = edtusername.getText().toString();
                    String strpin = edtpasword.getText().toString();
                    if(strusernma.equals("admin")){
                        Toast.makeText(SignupActivity.this,"Please select another username",Toast.LENGTH_SHORT).show();
                    }else {

                        AnnonmSignup(strdisplayname, strusernma, strpin);
                    }

                } else {
                    Toast.makeText(SignupActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            Utility.checkPermission(SignupActivity.this);
            // Utility.checkPermissionreadphonestatuts(SignupActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            ArrayList<CategoryMainTM> categoryMainTMS = new ArrayList<>();

            CategoryMainTM categoryMainTM1 = new CategoryMainTM();
            categoryMainTM1.setDesignation("Professor");

            CategoryMainTM categoryMainTM2 = new CategoryMainTM();
            categoryMainTM2.setDesignation("HOD");

            CategoryMainTM categoryMainTM3 = new CategoryMainTM();
            categoryMainTM3.setDesignation("Lab Assistant");

            CategoryMainTM categoryMainTM4 = new CategoryMainTM();
            categoryMainTM4.setDesignation("Principal");


            categoryMainTMS.add(categoryMainTM1);
            categoryMainTMS.add(categoryMainTM2);
            categoryMainTMS.add(categoryMainTM3);
            categoryMainTMS.add(categoryMainTM4);

            CustomAdapterMainCategory adapter = new CustomAdapterMainCategory(SignupActivity.this, R.layout.listitem_layout, R.id.spinnertext, categoryMainTMS);
            spdesignation.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        spdesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // your code here
                CategoryMainTM selection = (CategoryMainTM) spdesignation.getSelectedItem();

                try {

                    designation = selection.getDesignation();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    public void AnnonmSignup(final String displayname, final String username, final String pin) {
        pb.setVisibility(View.VISIBLE);
        try {
            mAuth.signInAnonymously().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pb.setVisibility(View.INVISIBLE);

                }
            }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //  Log.d(TAG, "OnComplete : " +task.isSuccessful());
                    try {
                        if (!task.isSuccessful()) {
                            pb.setVisibility(View.INVISIBLE);
                            //  Log.w(TAG, "Failed : ", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            System.out.println("firebaseUser------" + firebaseUser.getUid());

                            IfuserAlreadyExist(displayname, username, pin);
                        }
                    } catch (Exception e) {
                        pb.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void IfuserAlreadyExist(final String displayname, final String username, final String pin) {
        try {
            pb.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("users").orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("is data avila====" + dataSnapshot.toString());
                    try {
                        if (dataSnapshot.exists()) {
                            pb.setVisibility(View.INVISIBLE);
                            // Toast.makeText(SignupActivity.this, "Username Exist", Toast.LENGTH_SHORT).show();
                            HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                            // dataSnapshot is the "issue" node with all children with id 0
                            if (yourData != null) {
                                System.out.println("is data codecodecode====" + yourData.get("pin"));
                                System.out.println("is data codecodecode====" + yourData.toString());
                                // authodata(displayname, username, pin);
                                Toast.makeText(SignupActivity.this, "User already exist with same username", Toast.LENGTH_SHORT).show();
                            } else {
                                pb.setVisibility(View.INVISIBLE);
                            }
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        // do something with the individual "issues"
//                    }
                        } else {
                            // pb.setVisibility(View.INVISIBLE);
                            authodata(displayname, username, pin);
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


    public void authodata(final String displayname, String username, String pin) {
        pb.setVisibility(View.VISIBLE);

        UserGtSt userGtSt = new UserGtSt();
        final String usercode = Utils.getRandomString(4);
        try {
            userGtSt.setStarttime(strstarttime);
            userGtSt.setEndtime(strendtime);
            userGtSt.setIsgeoEnabled(true);
            userGtSt.setDesignation(designation);
            userGtSt.setName(displayname);
            userGtSt.setUsername(username);
            userGtSt.setCode(usercode);
            userGtSt.setPin(Utils.toBase64(pin));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String myuid = mDatabase.push().getKey();
        // mDatabase.push();
        mDatabase.child("users").child(myuid).setValue(userGtSt).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    pb.setVisibility(View.INVISIBLE);

                    Toast.makeText(SignupActivity.this, "Please try later", Toast.LENGTH_SHORT).show();
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

                        PreferenceHelper.storeuseruid(SignupActivity.this, myuid);
                        PreferenceHelper.storename(SignupActivity.this, displayname);
                        PreferenceHelper.storeusercode(SignupActivity.this, usercode);
                        PreferenceHelper.storeLoginstatus(SignupActivity.this, true);
                        Toast.makeText(SignupActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public class CustomAdapterMainCategory extends ArrayAdapter<CategoryMainTM> {

        LayoutInflater flater;
        Context context;

        public CustomAdapterMainCategory(Activity context, int resouceId, int textviewId, List<CategoryMainTM> list) {

            super(context, resouceId, textviewId, list);
            this.context = context;
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            CategoryMainTM rowItem = getItem(position);

            CustomAdapterMainCategory.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new CustomAdapterMainCategory.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.listitem_layout, null, false);

                holder.txtTitle = (TextView) rowview.findViewById(R.id.spinnertext);
//                try {
//                    if (Typefaces.get(context, "CharlotteSans_nn") != null) {
//
//                        holder.txtTitle.setTypeface(Typefaces.get(context, "CharlotteSans_nn"));
//
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                rowview.setTag(holder);
            } else {
                holder = (CustomAdapterMainCategory.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getDesignation());

            return rowview;
        }

        private class viewHolder {
            TextView txtTitle;
        }
    }
}
