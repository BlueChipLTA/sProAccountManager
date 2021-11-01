package com.spro.sproauthenticator.utils;

import android.util.Log;

import com.spro.sproauthenticator.BuildConfig;

/**
 * Created by sonnd on 9/26/2016.
 */

public class Logger {
    private static final String DEBUG_TAG = "ttcn88.";
    public static void e(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(DEBUG_TAG+tag, tr != null ? tr.getMessage() : "Exception is null", tr);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
//            Log.d(DEBUG_TAG+tag, msg);
            Log.e(DEBUG_TAG+tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(DEBUG_TAG+tag, msg);
        }
    }

    public static void LogException(Throwable e){
    }
}
