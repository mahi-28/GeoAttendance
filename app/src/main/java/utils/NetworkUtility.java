package utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.util.Log;


import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;


public class NetworkUtility
{
	public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        if(context!=null)
        {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (null != activeNetwork) {
                    if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                        return TYPE_WIFI;

                    if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                        return TYPE_MOBILE;
                }
                return TYPE_NOT_CONNECTED;
            }catch (Exception ex)
            {
                return TYPE_NOT_CONNECTED;
            }

        }

        return TYPE_NOT_CONNECTED;
    }
     
    public static Boolean getConnectivityStatusString(Context context) {
        boolean status = false;
        if(context!=null)
        {
            try {
                int conn = NetworkUtility.getConnectivityStatus(context);

                if (conn == NetworkUtility.TYPE_WIFI) {
                    status = true;
                } else if (conn == NetworkUtility.TYPE_MOBILE) {
                    status =true;
                } else if (conn == NetworkUtility.TYPE_NOT_CONNECTED) {
                    status = false;
                }
                return status;
            }catch (Exception ex)
            {
                return status;
            }

        }else {
            return status;
        }

    }
    @SuppressWarnings("deprecation")
	public static void showAlertDialog(final Context context, String title, String message, Boolean status) {
       /* CustumAlertBox alertDialog = new CustumAlertBox.Builder(context, R.style.MyAlertDialogStyle).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.error_24);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(context instanceof Activity){
            	//((Activity)context).finish();
            		dialog.dismiss();
            	}
            }
        });

        // Showing Alert Message
        alertDialog.show();*/


    }
    @SuppressWarnings("deprecation")
   	public static void showAlertDialogOk(final Context context, String title, String message, Boolean status) {







          /* CustumAlertBox alertDialog = new CustumAlertBox.Builder(context,R.style.MyAlertDialogStyle).create();
    
           // Setting Dialog Title
           alertDialog.setTitle(title);
    
           // Setting Dialog Message
           alertDialog.setMessage(message);
            
           // Setting alert dialog icon
           alertDialog.setIcon(R.drawable.warning_24);

           // Setting OK Button
           alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               	if(context instanceof Activity){
               	//((Activity)context).finish();
               		dialog.dismiss();
               		//((Activity) context).finish();
               	}
               }
           });
    
           // Showing Alert Message
           alertDialog.show();*/
       }




    private void isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        Iterator<ActivityManager.RunningAppProcessInfo> iter = runningAppProcesses.iterator();

        while(iter.hasNext()){
            ActivityManager.RunningAppProcessInfo next = iter.next();

            String pricessName = context.getPackageName() + ":service";

            if(next.processName.equals(pricessName)){

                Log.i("Killprocess","true"+""+pricessName);
                Process.killProcess(next.pid);
                break;
            }
        }
    }
}
