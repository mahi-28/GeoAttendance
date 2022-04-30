package utils;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper 
{

	Context con;

	public PreferenceHelper(Context con)
	{
		this.con = con;
	}


	public static SharedPreferences getSharedSettings(Context con)
	{
		return con.getSharedPreferences(SharedPreferenceConstant.SHARED_PREFERENCE_KEY,0);
	}

	public static void storeuserid(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.USERID, custname);
		prefEditor.commit();
	}

	public static String getuserid(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.USERID, null);
	}

	public static void storename(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.USERNAME, custname);
		prefEditor.commit();
	}

	public static String getusername(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.USERNAME, null);
	}

	//store user code
	public static void storeusercode(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.USERCODE, custname);
		prefEditor.commit();
	}

	public static String getusercode(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.USERCODE,null);
	}




	//store user code
	public static void storeuseruid(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.MYUID, custname);
		prefEditor.commit();  
	}
	
	public static String getuseruid(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.MYUID,null);
	}

	//store todayclock uid
	public static void storetodayClockUid(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.TODAYCLOCKUID, custname);
		prefEditor.commit();
	}

	public static String getTodayClockUid(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.TODAYCLOCKUID,"");
	}

	//store clock date
	public static void storeClockOnOffDate(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.CLOCKONOFFDATE, custname);
		prefEditor.commit();
	}

	public static String getClockOnOffDate(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.CLOCKONOFFDATE,"");
	}


	
//	//user name for the service
//	public static void storeUserImageLink(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.PROFILEIMGPATH, custname);
//		prefEditor.commit();
//	}
//
//	public static String getUserImageLink(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.PROFILEIMGPATH,null);
//	}
//	//login status
	public static void storeLoginstatus(Context ctx, boolean loginstatus)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putBoolean(SharedPreferenceConstant.LOGIN_STATUS, loginstatus);
		prefEditor.commit();  
	}

	public static boolean checkloginstate(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getBoolean(SharedPreferenceConstant.LOGIN_STATUS, false);
	}
//	//login status
//	public static void storeabletogeneratecode(Context ctx, boolean loginstatus)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putBoolean(SharedPreferenceConstant.ABLETOGENERATECODE, loginstatus);
//		prefEditor.commit();
//	}
//
//	public static boolean chkabletogeneratecode(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getBoolean(SharedPreferenceConstant.ABLETOGENERATECODE, false);
//	}
//	public static void StoreCategory(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.CATEGORY, custname);
//		prefEditor.commit();
//	}

//	public static String getCategory(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.CATEGORY, null);
//	}
//
//
//	public static void StoreLocationName(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.SAVELOCATIONNAME, custname);
//		prefEditor.commit();
//	}
//
//	public static String GetLocationName(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.SAVELOCATIONNAME, null);
//	}
//
//
//	public static void StatusToJumptoBookingListScreen(Context ctx, boolean loginstatus)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putBoolean(SharedPreferenceConstant.JUMPTOBOOKINGLIST, loginstatus);
//		prefEditor.commit();
//	}
//
//	public static boolean GetStatusIsJumpToBookingList(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getBoolean(SharedPreferenceConstant.JUMPTOBOOKINGLIST, false);
//	}
//
//	public static void StoreDeviceId(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.DEVICEID, custname);
//		prefEditor.commit();
//	}
//
//	public static String GetDeviceId(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.DEVICEID, null);
//	}
//
//
//    public static void StoreLocationasString(Context ctx, String custname)
//    {
//        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//        prefEditor.putString(SharedPreferenceConstant.LOCATIONSTRING, custname);
//        prefEditor.commit();
//    }
//
//    public static String GetLocation(Context ctx)
//    {
//        SharedPreferences sp = getSharedSettings(ctx);
//        return sp.getString(SharedPreferenceConstant.LOCATIONSTRING, null);
//    }
//
//
//
//    public static void StoreSelectedServiceToShow(Context ctx, String custname)
//    {
//        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//        prefEditor.putString(SharedPreferenceConstant.SHOWSELETEDSERVCIE, custname);
//        prefEditor.commit();
//    }
//
//    public static String GetSelectedServiceToShow(Context ctx)
//    {
//        SharedPreferences sp = getSharedSettings(ctx);
//        return sp.getString(SharedPreferenceConstant.SHOWSELETEDSERVCIE, null);
//    }
//
//    public static void StCt(Context ctx, String custname)
//    {
//        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//        prefEditor.putString(SharedPreferenceConstant.STNTCT, custname);
//        prefEditor.commit();
//    }
//
//    public static String GtCt(Context ctx)
//    {
//        SharedPreferences sp = getSharedSettings(ctx);
//        return sp.getString(SharedPreferenceConstant.STNTCT, null);
//    }
//
//
//    public static void StoreNotificationAppoID(Context ctx, String custname)
//    {
//        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//        prefEditor.putString(SharedPreferenceConstant.APPOINMENTID, custname);
//        prefEditor.commit();
//    }
//
//    public static String GetNotificationAppoID(Context ctx)
//    {
//        SharedPreferences sp = getSharedSettings(ctx);
//        return sp.getString(SharedPreferenceConstant.APPOINMENTID, null);
//    }
//
//    public static void StoreGoogleAnyMsg(Context ctx, String custname)
//    {
//        SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//        prefEditor.putString(SharedPreferenceConstant.GOOGLEANYMSG, custname);
//        prefEditor.commit();
//    }
//
//    public static String GetGoogleAnyMsg(Context ctx)
//    {
//        SharedPreferences sp = getSharedSettings(ctx);
//        return sp.getString(SharedPreferenceConstant.GOOGLEANYMSG, null);
//    }
//
//
//
//	public static void SToreMCategory(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.MAINCATEGORY, custname);
//		prefEditor.commit();
//	}
//
//	public static String GetMainCategory(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.MAINCATEGORY, null);
//	}
//
//	public static void StorePAthCategory(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.PATHCATEGORY, custname);
//		prefEditor.commit();
//	}
//
//	public static String GetPathCategory(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.PATHCATEGORY, null);
//	}
//
//	public static void StoreCategoryUrl(Context ctx, String custname)
//	{
//		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
//		prefEditor.putString(SharedPreferenceConstant.CATEGORYURL, custname);
//		prefEditor.commit();
//	}
//
//	public static String GetCategoryUrl(Context ctx)
//	{
//		SharedPreferences sp = getSharedSettings(ctx);
//		return sp.getString(SharedPreferenceConstant.CATEGORYURL, null);
//	}
	public static void StoreIntroShown(Context ctx, boolean loginstatus)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putBoolean(SharedPreferenceConstant.INTROSHOWN, loginstatus);
		prefEditor.commit();
	}

	public static boolean GetIntroShown(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getBoolean(SharedPreferenceConstant.INTROSHOWN, false);
	}


}
