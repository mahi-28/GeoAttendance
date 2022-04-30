package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelperOffline {
    Context con;
    public PrefHelperOffline(Context con)
    {
        this.con = con;
    }


    public static SharedPreferences getSharedSettings(Context con)
    {
        return con.getSharedPreferences(SharedPreferenceConstant.SHARED_PREFERENCE_KEY_CONTACT,0);
    }

    public static void storefuids(Context ctx, String custname)
    {
        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
        prefEditor.putString(SharedPreferenceConstant.FRIENDUID, custname);
        prefEditor.commit();
    }

    public static String getfrienduids(Context ctx)
    {
        SharedPreferences sp = getSharedSettings(ctx);
        return sp.getString(SharedPreferenceConstant.FRIENDUID,null);
    }



}
