package com.plutecoder.geoattendance;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fragments.Fragment_Mytime;
import fragments.MyClockTime;
import fragments.MyaccountFragment;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import utils.Haversine;
import utils.LocationConstant;
import utils.PreferenceHelper;
import utils.Typefaces;
import utils.Utility;
import utils.Utils;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    BottomNavigation bottomNavigationView;
    private FragmentManager fragmentManager;
    public static Activity activity;
    private Location mCurrentLastLocation;
    static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    public static   String dateselected;



    private Cipher cipher;
    private KeyStore keyStore;
    private static final String KEY_NAME = "geoattendance";
    public static boolean isfingureprintavailable=false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        activity = MainActivity.this;
        Utility.checkpermissionlocation(MainActivity.this);
        try {
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.gereenapp), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {

            FingerprintManager fingerprintManager = (FingerprintManager) MainActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                isfingureprintavailable = false;

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                isfingureprintavailable = false;

            } else {
                // Everything is ready for fingerprint authentication
                isfingureprintavailable = true;

            }
        }catch (Exception e){
            e.printStackTrace();
            isfingureprintavailable = false;


        }


        changeActionbarTitle("Home");



        try {
            bottomNavigationView = (BottomNavigation) findViewById(R.id.BottomNavigation);
            bottomNavigationView.setDefaultSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bottomNavigationView.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int position, boolean b) {

                try {
                    if (position == 0) {

                        Fragment_Mytime fragments = new Fragment_Mytime();
                        replaceFragment(fragments, "");
                        setActionBarTitle(MainActivity.this, "My Activity", getSupportActionBar());

                    }

                    if (position == 1) {
//

                        MyClockTime fragments = new MyClockTime();
                        replaceFragment(fragments, "");
                        setActionBarTitle(MainActivity.this, "My Clock", getSupportActionBar());
                    }

                    if (position == 2) {

                        MyaccountFragment fragments = new MyaccountFragment();
                        replaceFragment(fragments, "");
                        setActionBarTitle(MainActivity.this, "Profile", getSupportActionBar());
                    }

                    if (position == 3) {
                        // ismapvisible=false;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                // Do something
            }
        });
        Utility.checkPermission(MainActivity.this);
        try {
            Fragment_Mytime fragments = new Fragment_Mytime();
            replaceFragment(fragments, "");
            setActionBarTitle(MainActivity.this, "My Activity", getSupportActionBar());


            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            startLocationListener();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void startLocationListener() {
        try {

            System.out.println("---------called----");
            long mLocTrackingInterval = 1000 * 5; // 5 sec
            float trackingDistance = 1;
            LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

            // LocationParams   params = LocationParams.BEST_EFFORT;
            LocationParams.Builder builder = new LocationParams.Builder()
                    .setAccuracy(trackingAccuracy)
                    .setDistance(trackingDistance)
                    .setInterval(mLocTrackingInterval);

            SmartLocation.with(this)
                    .location()
                    .continuous()
                    .config(builder.build())
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            if (isBetterLocation(mCurrentLastLocation, location)) {
                                // If location is better, do some user preview.
                                System.out.println("---------Mainactivitylocation----" + location);
                                mCurrentLastLocation = location;

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDate() {
        showDialog(999);
        Toast.makeText(MainActivity.this, "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(MainActivity.this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    public String showDate(int year, int month, int day) {

        StringBuilder date=new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year);
        dateselected=date.toString();

        try{
            if(!PreferenceHelper.getClockOnOffDate(MainActivity.activity).equals(dateselected)){
                PreferenceHelper.storetodayClockUid(MainActivity.activity,"");
                PreferenceHelper.storetodayClockUid(MainActivity.activity, "");

            }else{
                Toast.makeText(MainActivity.this,"Date is same",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return date.toString();
    }
    boolean isBetterLocation(Location oldLocation, Location newLocation) {
        try {
            // If there is no old location, of course the new location is better.
            if (oldLocation == null) {
                return true;
            }

            // Check if new location is newer in time.
            boolean isNewer = newLocation.getTime() > oldLocation.getTime();

            // Check if new location more accurate. Accuracy is radius in meters, so less is better.
            boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();
            if (isMoreAccurate && isNewer) {
                // More accurate and newer is always better.
                return true;
            } else if (isMoreAccurate && !isNewer) {
                // More accurate but not newer can lead to bad fix because of user movement.
                // Let us set a threshold for the maximum tolerance of time difference.
                long timeDifference = newLocation.getTime() - oldLocation.getTime();

                // If time difference is not greater then allowed threshold we accept it.
                if (timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void changeActionbarTitle(String title) {
        //getSupportActionBar().setTitle(title);
        setActionBarTitle(MainActivity.this, title, getSupportActionBar());
    }

    public void setActionBarTitle(Context context, String title, ActionBar actionBar) {
        try {
            if (Typefaces.get(context, "HelveticaNeue Light") != null) {
                SpannableString SpanString = new SpannableString(title);
                SpanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpanString.setSpan(new utils.TypefaceSpan(context, Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue Light.ttf").toString()), 0, SpanString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(SpanString);
            } else {
                actionBar.setTitle(title);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public boolean IsinOrgLocation() {
        try {
            try {

                double distance=Haversine.distance(mCurrentLastLocation.getLatitude(),17.652356,mCurrentLastLocation.getLongitude(),75.283372);
                if (isGpsEnabled(MainActivity.this)) {

                    if (mCurrentLastLocation != null) {

                        boolean isinrange=LocationConstant.IsInRange(mCurrentLastLocation.getLatitude(), mCurrentLastLocation.getLongitude());
                        if(!isinrange){
                            Toast.makeText(MainActivity.this, "Distance==="+distance+"\nYou are not within work location to act. Thank you!", Toast.LENGTH_SHORT).show();

                        }

                        return isinrange;

                    } else {
                        Toast.makeText(MainActivity.this, "Please try later or check GPS status.", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                } else {
                    try {
                        Toast.makeText(MainActivity.this, "please turn ON  GPS...", Toast.LENGTH_SHORT).show();
                        Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(viewIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

    public static boolean isGpsEnabled(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                String providers = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                if (TextUtils.isEmpty(providers)) {
                    return false;
                }
                return providers.contains(LocationManager.GPS_PROVIDER);
            } else {
                final int locationMode;
                try {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(),
                            Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                switch (locationMode) {

                    case Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                    case Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                        return true;
                    case Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                    case Settings.Secure.LOCATION_MODE_OFF:
                    default:
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //set status of connection using UI Thread
    public void replaceFragment(final Fragment targetFragment, final String msg) {
        clearFragmentBackStack();
        System.gc();
        //start thread here for 1 sec
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 1 seconds
                    sleep(1 * 500);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  mTextHeader.setText(headerText);
                            if (targetFragment != null) {
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.addOnBackStackChangedListener(MainActivity.this);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                String packageNameAsTag = ((Object) targetFragment).getClass().getCanonicalName();
                                // System.out.println("package name is :: " + packageNameAsTag);

                                if (fragmentManager.findFragmentByTag(packageNameAsTag) == null) {
                                    fragmentTransaction.add(R.id.container_body, targetFragment, packageNameAsTag);
                                    fragmentTransaction.addToBackStack(packageNameAsTag);
                                    Bundle bundle = new Bundle();
//                                    bundle.putString(Constant.EXTRAS_DEVICE_NAME, mDeviceName);
//                                    bundle.putString(Constant.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
                                    bundle.putString("STRING", msg);
                                    targetFragment.setArguments(bundle);
                                    // System.out.println(((Object) targetFragment).getClass().getSimpleName() + " added to backstack");
                                    fragmentTransaction.commit();
                                } else {
                                    // System.out.println("this fragment is already in the backstack");
                                }
                            } else {
//            ToastUtil.displayToast(this, this.getString(R.string.toast_working));

                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
        // start thread

    }

    //method to clear fragment
    public void clearFragmentBackStack() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                int i = fragmentManager.getBackStackEntryCount();
                for (int j = 0; j < i; j++) {
                    fragmentManager.popBackStackImmediate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackStackChanged() {

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private Context context;

        // Constructor
        public FingerprintHandler(Context mContext) {
            context = mContext;
        }

        public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            this.update("Fingerprint Authentication error\n" + errString);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            this.update("Fingerprint Authentication help\n" + helpString);
        }

        @Override
        public void onAuthenticationFailed() {
            this.update("Fingerprint Authentication failed.");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        }

        private void update(String e){
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void fingautho(){
        try {
            // Initializing both Android Keyguard Manager and Fingerprint Manager
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            // Check whether the device has a Fingerprint sensor.
            if(!fingerprintManager.isHardwareDetected()){
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                // textView.setText("Your Device does not have a Fingerprint Sensor");
            }else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    // textView.setText("Fingerprint authentication permission not enabled");
                    Toast.makeText(this,"Fingerprint authentication permission not enabled",Toast.LENGTH_SHORT).show();
                }else{
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        // textView.setText("Register at least one fingerprint in Settings");
                        Toast.makeText(this,"Register at least one fingerprint in Settings",Toast.LENGTH_SHORT).show();

                    }else{
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            // textView.setText("Lock screen security not enabled in Settings");
                            Toast.makeText(this,"Lock screen security not enabled in Settings",Toast.LENGTH_SHORT).show();

                        }else{
                            generateKey();

                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

class TestAsyk extends AsyncTask{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }


}

}
