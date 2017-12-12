package delusion.achievers.eva.utils;

import android.util.Log;

import java.util.Arrays;

public class EvaLog {

    private static final boolean enableLogging = true;


    private static final String LOG_TAG = "MyRockingApp";

    /** @see <a href="http://stackoverflow.com/a/8899735" /> */
    private static final int ENTRY_MAX_LEN = 4000;




    public static void i(String tag, String msg) {
        if (enableLogging) {
            Log.i("" + tag, "" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (enableLogging) {
            Log.e("" + tag, "" + msg);
        }
    }

    public static void e(String tag, Throwable e) {
        if (enableLogging) {
            Log.e("" + tag, "" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void debugEntire(String message, Object... args) {
        log(Log.DEBUG, true, message, args);
    }


    public static void e(String tag, String msg, Throwable e) {
        if (enableLogging) {
            Log.e("" + tag, "" + msg, e);
            //e.printStackTrace();
        }
    }

    public static void w(String tag, String msg) {
        if (enableLogging) {
            Log.w("" + tag, "" + msg);
        }
    }

    public static void w(String tag, Throwable e) {
        if (enableLogging) {
            Log.w("" + tag, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void d(String tag, String msg) {
        if (enableLogging) {
            Log.d("" + tag, "" + msg);
        }
    }

    public static void d(String tag, Throwable e) {
        if (enableLogging) {
            Log.d("" + tag, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void log(int priority, boolean ignoreLimit, String message, Object... args) {
        String print;
        if (args != null && args.length > 0 && args[args.length-1] instanceof Throwable) {
            Object[] truncated = Arrays.copyOf(args, args.length - 1);
            Throwable ex = (Throwable) args[args.length-1];
            print = formatMessage(message, truncated) + '\n' + Log.getStackTraceString(ex);
        } else {
            print = formatMessage(message, args);
        }
        if (ignoreLimit) {
            while (!print.isEmpty()) {
                int lastNewLine = print.lastIndexOf('\n', ENTRY_MAX_LEN);
                int nextEnd = lastNewLine != -1 ? lastNewLine : Math.min(ENTRY_MAX_LEN, print.length());
                String next = print.substring(0, nextEnd /*exclusive*/);
                Log.println(priority, LOG_TAG, next);
                if (lastNewLine != -1) {
                    // Don't print out the \n twice.
                    print = print.substring(nextEnd+1);
                } else {
                    print = print.substring(nextEnd);
                }
            }
        } else {
            Log.println(priority, LOG_TAG, print);
        }
    }

    private static String formatMessage(String message, Object... args) {
        String formatted;
        try {
            /*
             * {} is used by SLF4J so keep it compatible with that as it's easy to forget to use %s when you are
             * switching back and forth between server and client code.
             */
            formatted = String.format(message.replaceAll("\\{\\}", "%s"), args);
        } catch (Exception ex) {
            formatted = message + Arrays.toString(args);
        }
        return formatted;
    }


    public static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }

}