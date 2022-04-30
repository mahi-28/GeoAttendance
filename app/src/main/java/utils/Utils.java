package utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Utils {
public static final String ALLOWED_CHARACTERS ="0123456789abcdef";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
    /**
     * @param message the message to be encoded
     *
     * @return the enooded from of the message
     */
    public static String toBase64(String message) {
        byte[] data;
        try {
            data = message.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms.replaceAll("=","").replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param message the encoded message
     *
     * @return the decoded message
     */
    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isAppIsInBackground(Context context) {
        try {
            boolean isInBackground = true;
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }

            return isInBackground;
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, activity, 0).show();
            return false;
        }
    }

    public static Long GetUnixTimeStamp(){
        long unixTime = System.currentTimeMillis() / 1000L;

        return unixTime;

    }


    //check internet connection
    public static boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static String GetCurrentDate() {
        try {

            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).format(cDate);

            return (fDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String GetCurrentDateForAppont() {
        try {

            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(cDate);

            return (fDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            }
            return capitalize(manufacturer) + " " + model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isvalidamount(String amount) {
        try {
            float amountfinal = Float.parseFloat(amount);
            if (amountfinal > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String getDateFormat(String strDate, String inputFormat, String outputFormat) {
        Date utdt = null;
        String outputDate = null;
        try {
            if (strDate == null)
                return "-";

            SimpleDateFormat smft = new SimpleDateFormat(inputFormat);
            utdt = smft.parse(strDate);
            smft = new SimpleDateFormat(outputFormat);
            outputDate = smft.format(utdt);


        } catch (Exception e) {
            return "-";
            //			e.printStackTrace();
            //			log.error(e.getClass() + ": " +  e.getMessage(), e);
        }
        return outputDate;
    }
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public static boolean passwordFormatCheck(String text, Activity activity) {

        //TODO: testing if pass contains number
        if (text.matches(".*\\d+.*") && text.length() > 7) {

            if (text.matches(".*[a-zA-Z]+.*")) {
                Log.d("Pass contains", "Number");
                return true;
            } else {
                showPasswordFormatAlert(activity);
                return false;
            }

        } else {
            Log.d("Pass Does not contain", "Number");
            // EdtMobilePh.setError(getResources().getString(R.string.password_error_num));
            //Show Alert
            showPasswordFormatAlert(activity);
            return false;
        }
    }

    //Password alert
    public static void showPasswordFormatAlert(final Activity activity) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    //final String[] sortArray = activity.getResources().getStringArray(R.array.sortProviderListArray);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle("Invalid Password Format");
                    builder.setCancelable(false);
                    builder.setMessage("Password must contain eight characters. One of them a number. One of them alphanumeric.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public static void emailAlreadyRegisteredAlert(final Activity activity, final String emailEntered) {
//        activity.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    //final String[] sortArray = activity.getResources().getStringArray(R.array.sortProviderListArray);
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                    TextView myMsg = new TextView(activity);
//                    myMsg.setText("Email already exists.");
//                    myMsg.setGravity(Gravity.CENTER);
//                    myMsg.setTextSize(20);
//                    myMsg.setTextColor(Color.WHITE);
//                    myMsg.setBackgroundColor(Color.parseColor("#f05323"));
//                    myMsg.setHeight(90);
//                    builder.setCustomTitle(myMsg);
//                    builder.setCancelable(false);
//                    builder.setMessage("This email address " + emailEntered + " is already registered.");
//
//                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }








    public static void HideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static ProgressDialog createProgressDialog(Activity activity, String message) {
        ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);

        return pDialog;
    }

    public static String StringToDateConversion(String strdate) {
        try {


//            final String NEW_FORMAT = "ccc, MMM. d yyyy";
            final String NEW_FORMAT = "cccc, MMMM dd";
            final String OLD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
            String formatDate;
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH);
            Date d = null;
            try {
                d = sdf.parse(strdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sdf.applyPattern(NEW_FORMAT);
            formatDate = sdf.format(d);
            return formatDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Turn drawable resource into byte array.
     *
     * @param context parent context
     * @param id      drawable resource id
     * @return byte array
     */

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @SuppressWarnings("MissingPermission")
    public static boolean checkFingerPrintAvailability(@NonNull Context context) {
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }



}
