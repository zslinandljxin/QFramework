package com.zsl.zhaoqing.framework;

import android.app.Application;

/**
 * Created by Administrator on 2017/9/22.
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static BaseApplication getInstance(){
        return mInstance;
    }
}
