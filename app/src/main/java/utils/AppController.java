package utils;
import android.app.Application;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    public static int DrawerWidth = 460;
    public static int Height = 235;
    public static int width, height;
    public static int Categorylistrightpadding=120;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
            int width = getResources().getDisplayMetrics().widthPixels;
            width=(int)(width*(80.0f/100.0f));
           // width = metrics.widthPixels;
            height = metrics.heightPixels;
            Height = height;
            DrawerWidth = width;

            Categorylistrightpadding=(int)(width*(70.0f/100.0f));
        } catch (Exception e) {
            DrawerWidth = 460;
            e.printStackTrace();
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}