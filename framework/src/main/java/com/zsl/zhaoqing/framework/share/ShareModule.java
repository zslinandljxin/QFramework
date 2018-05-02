package com.zsl.zhaoqing.framework.share;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsl on 2017/10/19.
 */

public class ShareModule {

    private static ShareModule mInstance;
    private Context mContext;
    private Map<String, Class> mPlatforms;

    private ShareModule(Context context){
        this.mContext = context.getApplicationContext();
        mPlatforms = new HashMap<>();
    }

    public static ShareModule getInstance(Context context){
        if (mInstance == null){
            synchronized (ShareModule.class){
                if (mInstance == null){
                    mInstance = new ShareModule(context);
                }
            }
        }
        return mInstance;
    }

    public void addPlatform(String key, Class platform){
        Class clazz = mPlatforms.get(key);
        if (clazz == null){
            mPlatforms.put(key, platform);
        }
    }

    public void share(String platform, String type, String title, String des, String url, Bitmap thumb){
        BaseShare share = choosePlatform(platform);
        share.share(type, title, des, url, thumb);
    }

    private BaseShare choosePlatform(String platform){
        Class clazz = mPlatforms.get(platform);
        if (clazz == null){
            throw new IllegalArgumentException(
                    "please ensure correctly set share platform:" + platform);
        }
        try {
            return (BaseShare) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
