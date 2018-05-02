package com.zsl.zhaoqing.framework.utils;

import android.util.Log;

/**
 * Created by zsl on 2018/4/26.
 */

public class LogUtil {
    private static boolean mIsDebug = false;
    private static boolean mIsInit = false;

    public static synchronized void init(boolean debug){
        if (mIsInit){
            mIsDebug = debug;
            mIsInit = true;
        }
    }

    public static void d(String tag, String msg){
        if (mIsDebug){
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if (mIsDebug){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (mIsDebug){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (mIsDebug){
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg){
        if (mIsDebug){
            Log.v(tag, msg);
        }
    }
}
