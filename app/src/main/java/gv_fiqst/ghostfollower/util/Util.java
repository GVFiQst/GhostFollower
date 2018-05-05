package gv_fiqst.ghostfollower.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Util {
    private static int sStatusBarHeight = -1;
    private static final int SECONDS = 1000;
    private static final int MINUTES = 60000;
    private static final int HOURS = 3600000;

    public static <T> List<T> fillList(List<T> list, T item, int count) {
        for (int i = 0; i < count; i++) {
            list.add(item);
        }

        return list;
    }

    public static int dpToPx(DisplayMetrics dm, int i) {
        return (int) (dm.density * i);
    }

    public static int dpToPx(Resources res, int i) {
        return dpToPx(res.getDisplayMetrics(), i);
    }

    public static int dpToPx(Context context, int i) {
        return dpToPx(context.getResources(), i);
    }

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight > 0) {
            return sStatusBarHeight;
        }

        Resources res = context.getResources();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");

        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        } else

            return sStatusBarHeight = (int) Math.ceil(
                    dpToPx(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25)
            );
    }

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    public static AlertDialog showDialog(Context context, String message, DialogInterface.OnClickListener yesListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", null)
                .show();
    }

    public static ColorStateList tint(int color) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                color, color, color, color
        };

        return new ColorStateList(states, colors);
    }

    //2018-01-11T13:40:55+0000
    public static long parseFacebookTime(String timeToParse) {
        if (timeToParse == null || timeToParse.isEmpty()) {
            return 0;
        }

        String[] splited = timeToParse.split("T");
        String date = splited[0];
        String time = splited[1];

        splited = date.split("-");
        String year = splited[0];
        String month = splited[1];
        String day = splited[2];

        splited = time.split("\\+");
        time = splited[0];
        String millis = splited[1];

        splited = time.split(":");
        String hours = splited[0];
        String minutes = splited[1];
        String seconds = splited[2];

        return parseTime(day, Integer.parseInt(month) - 1, year, hours, minutes, seconds, millis);
    }

    private static long parseTime(String day, int month, String year, String hours, String minutes, String seconds, String millis) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        cal.set(Calendar.MINUTE, Integer.parseInt(minutes));
        cal.set(Calendar.SECOND, Integer.parseInt(seconds));
        cal.set(Calendar.MILLISECOND, Integer.parseInt(millis));
        return cal.getTimeInMillis();
    }

    //21h or Jan 12
    public static long parseTwitterTime(String timeToParse) {
        Calendar cal = Calendar.getInstance();
        try {
            if (timeToParse.equals("now")) {
                // Do nothing and return Calendar.getInstance()
            } else

            if (timeToParse.endsWith("h")) {
                int hours = Integer.parseInt(timeToParse.substring(0, timeToParse.length() - 1));
                int now = cal.get(Calendar.HOUR_OF_DAY);
                int toSet = now - hours;
                cal.set(Calendar.HOUR_OF_DAY, toSet < 0 ? 24 + toSet : toSet);
            } else

            if (timeToParse.endsWith("m")) {
                int minutes = Integer.parseInt(timeToParse.substring(0, timeToParse.length() - 1));
                int now = cal.get(Calendar.MINUTE);
                int toSet = now - minutes;
                cal.set(Calendar.MINUTE, toSet < 0 ? 60 + toSet : toSet);
            } else

            if (timeToParse.endsWith("s")) {
                int seconds = Integer.parseInt(timeToParse.substring(0, timeToParse.length() - 1));
                int now = cal.get(Calendar.SECOND);
                int toSet = now - seconds;
                cal.set(Calendar.SECOND, toSet < 0 ? 60 + toSet : toSet);
            } else

            {
                String[] splited = timeToParse.split(" ");
                int month = switchMonth(splited.length == 2 ? splited[0] : splited[1]);
                String day = splited.length == 2 ? splited[1] : splited[0];

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                if (splited.length == 3) {
                    int year = Integer.parseInt(splited[2]);
                    year += year < 2000 ? 2000 : 0;

                    cal.set(Calendar.YEAR, year);
                } else {
                    int nowYear = cal.get(Calendar.YEAR);
                    int nowMonth = cal.get(Calendar.MONTH);
                    cal.set(Calendar.YEAR, nowMonth < month ? nowYear - 1 : nowYear);
                }

                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) + 1);
            }

            return cal.getTimeInMillis();
        } catch (Exception e) {
            Log.e("lox", "parseTwitterTime(" + timeToParse + ")\n", e);
            throw e;
        }
    }

    private static int switchMonth(String month) {
        switch (month) {
            default: Log.d("lox", "MONTH NOT FOUND: " + month);
            case "Jan": return 0;
            case "Feb": return 1;
            case "Mar": return 2;
            case "Apr": return 3;
            case "May": return 4;
            case "June": return 5;
            case "July": return 6;
            case "Aug": return 7;
            case "Sept": return 8;
            case "Oct": return 9;
            case "Nov": return 10;
            case "Dec": return 11;
        }
    }

    public static String format(String form, long millis) {
        return format(form, new Date(millis));
    }

    public static String format(String form, Date date) {
        return new SimpleDateFormat(form, Locale.getDefault()).format(date);
    }


    public static String formatTime(long time, boolean withMillis) {
        int h = 0;
        int m = 0;
        int s = 0;
        String result = "";

        if (time > HOURS) {
            h = (int) (time / (float) HOURS);
            time -= h * HOURS;
        }

        if (time > MINUTES) {
            m = (int) (time / (float) MINUTES);
            time -= m * MINUTES;
        }

        if (time > SECONDS) {
            s = (int) (time / (float) SECONDS);
            time -= s * SECONDS;
        }

        if (h > 0) {
            result += h > 9 ? h + "" : "0" + h + ":";
        }

        result += (m > 9 ? m + "" : "0" + m) + ":"
                + (s > 9 ? s + "" : "0" + s);

        if (withMillis) {
            int millis = ((int) (time / 10));
            result += "." + (millis > 9 ? millis + "" : "0" + millis);
        }

        return result;
    }

    public static String getString(JSONObject obj, String key) throws JSONException {
        return obj.has(key) ? obj.getString(key) : "";
    }

    public static int getThemeColor(Resources.Theme theme, int attrName) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = theme.obtainStyledAttributes(typedValue.data, new int[] { attrName });
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }
}
