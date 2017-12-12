package delusion.achievers.eva.utils;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Created by arun.sharma on 22/4/15.
 */
public class Preferences {

    private static final String PREF_NAME = "Eva";


    private static final int MODE = Context.MODE_PRIVATE;
    //User Details
    public static final String LOGIN = "login";
    //    public static final String EMAIL = "email";
    public static final String OTP_SENT = "otp_sent";

//    public static final String REGISTRATION = "registration";
//    public static final String USER_ID = "user_id";

    public static final String LOGOUT = "Logout";
    public static final String FORGET_PASS = "forget_pass";
    //    public static final String PASSWORD = "password";
//    public static final String DEVICE_ID = "device_id";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String UUID = "uuid";
    public static final String MAC_ADDRESS = "mac_address";
    public static final String DEVICE_NAME = "device_name";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String FORMATTED_MOBILE_NUMBER = "formatted_mobile_number";
    public static final String BANKING_ENABLED = "banking_enabled";
    public static final String PASSCODE_TURN_ON = "passcode_turn_on";
    public static final String PASSCODE_VALUE = "passcode_value";
    public static final String LATER_CASE = "later_case";
    public static final String USER_NAME = "user_name";
    public static final String GCM_TOKEN = "gcm_token";

    //  GpPreferences.writeString(getApplicationContext(), Preferences.NAME, "dev");
    // GpPreferences.readString(getApplicationContext(), Preferences.NAME, "");


    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    // execute at signup time.
    public static void clearAllPreference(Context context) {
        // ModelManager.getInstance().clearManagerInstance();
        getEditor(context).putString(LOGIN, null).commit();
        getEditor(context).putString(UUID, null).commit();
        getEditor(context).putString(MAC_ADDRESS, null).commit();
        getEditor(context).putString(AUTH_TOKEN, null).commit();
        getEditor(context).putString(DEVICE_NAME, null).commit();
        getEditor(context).putString(OTP_SENT, null).commit();
        getEditor(context).putString(MOBILE_NUMBER, null).commit();
        getEditor(context).putString(FORMATTED_MOBILE_NUMBER, null).commit();
//        getEditor(context).putString(EMAIL, null).commit();
//        getEditor(context).putString(PASSWORD, null).commit();
//        getEditor(context).putString(DEVICE_ID, null).commit();
    }


}

