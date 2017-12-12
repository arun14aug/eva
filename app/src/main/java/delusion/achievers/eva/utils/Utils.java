package delusion.achievers.eva.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import delusion.achievers.eva.R;
import delusion.achievers.eva.httprequest.TLSSocketFactory;


/*
 * Created by arun on 16/12/15.
 */
public class Utils {

    private static ProgressDialog progressDialog;
    public static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    public static final int INITIAL_REQUEST = 1337;
    public static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;


    public static void showLoading(Activity act/*, String msg*/) {
//        progressDialog = ProgressDialog
//                .show(act, "", msg, true);
        progressDialog = ProgressDialog
                .show(act,
                        "",
                        act.getString(R.string.please_wait),
                        true);
    }

    public static void dismissLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static String commentTime(String dateString) {
        Date dat = null;
        String value = null;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dat = dateFormat.parse(dateString);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("d MMMM");
            value = format.format(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String capSentence(String string, boolean capitalize) {
        if (!Utils.isEmptyString(string)) {
            if (string.length() == 0) {
                return "";
            }
            String c = string.substring(0, 1);

            if (capitalize) {
                return c.toUpperCase() + capSentence(string.substring(1), c.equals(" "));
            } else {
                return c.toLowerCase() + capSentence(string.substring(1), c.equals(" "));
            }
        } else {
            return "";
        }
    }


    public static boolean isConnectingToInternet(Activity act) {
        ConnectivityManager connMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // fetch data
// display error
        return networkInfo != null && networkInfo.isConnected();
    }


    public static boolean isEmptyString(String str) {
        return str == null || str.equalsIgnoreCase("null")
                || str.equalsIgnoreCase("") || str.equalsIgnoreCase("0") || str.length() < 1;
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }


    public static void showAlert(Activity activity, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    public static void showMessage(Activity activity, String msg) {
        Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
    }


//    public static void defaultLoader(Activity act) {
//        progressDialog = ProgressDialog
//                .show(act,
//                        "",
//                        act.getString(R.string.please_wait),
//                        true);
//    }
//
//    public static void dismissDefaultLoader() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }

    public static int setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT < 23) {
            return activity.getResources().getColor(color);

        } else {
            return ContextCompat.getColor(activity, color);
        }
    }

    public static RequestQueue getVolleyRequestQueue(Activity activity) {
        HurlStack stack = null;
        if (Build.VERSION.SDK_INT >= 9) {
            try {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
// Use a socket factory that removes sslv3 and add TLS1.2
                    stack = new HurlStack(null, new TLSSocketFactory());
                } else {
                    stack = new HurlStack();
                }
            } catch (Exception e) {
                stack = new HurlStack();
                Log.i("NetworkClient", "can no create custom socket factory");
            }
        }
        return Volley.newRequestQueue(activity, stack);
    }

    public static RequestQueue getNewVolleyRequestQueue(Context activity) {
        HurlStack stack = null;
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
// Use a socket factory that removes sslv3 and add TLS1.2
                stack = new HurlStack(null, new TLSSocketFactory());
            } else {
                stack = new HurlStack();
            }
        } catch (Exception e) {
            stack = new HurlStack();
            Log.i("NetworkClient", "can no create custom socket factory");
        }
        return Volley.newRequestQueue(activity, stack);
    }

    public static String getDecimalTo2Points(String value) {
        String[] amt = value.split("\\.");
        String amount;
        if (amt.length > 1) {
            if (amt[1].length() > 1)
                amount = amt[0] + "." + amt[1];
            else
                amount = amt[0] + "." + amt[1] + "0";
        } else
            amount = amt[0] + ".00";
        return amount;
    }
}
