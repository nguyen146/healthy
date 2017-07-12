package com.htnguyen.healthy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionUtil {
    public static final String ERR_DIALOG_TITLE = "No internet connection detected !";
    private static final String ERR_DIALOG_MSG = "Looks like our application is not able to detect an active internet connection, " +
            "please check your device's network settings.";
    private static final String ERR_DIALOG_POSITIVE_BTN = "Settings";
    private static final String ERR_DIALOG_NEGATIVE_BTN = "Dismiss";

    /**
     * Check if the device is connected to internet or not.
     *
     * @param context Current context of the application
     * @return <b>true</b> if device is connected to internet, otherwise <b>false</b>
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Check if the device is connected to internet via <i>wifi</i> or not.
     *
     * @param context Current context of the application
     * @return <b>true</b> if device is connected to internet via <i>wifi</i>, otherwise <b>false</b>
     */
    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Check if the device is connected to internet via <i>mobile network</i> or not.
     *
     * @param context Current context of the application
     * @return <b>true</b> if device is connected to internet via <i>mobile network</i>, otherwise <b>false</b>
     */
    public static boolean isConnectedToMobileNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

}
