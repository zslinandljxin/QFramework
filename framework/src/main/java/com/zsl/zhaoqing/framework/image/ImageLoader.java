package com.zsl.zhaoqing.framework.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by zsl on 2017/6/12.
 */

public class ImageLoader {

    public static final int MODE_ENABLE = 0;
    public static final int MODE_DISABLE = 1;

    public static void init(Context context, ImageLoadConfig config){
        ImageLoaderProxy.getInstance().setImageConfig(context, config);
    }

    public static void setMode(int mode){
        ImageLoaderProxy.getInstance().setMode(mode);
    }

    public static void displayResId(ImageView view, int resId, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().displayResId(view, resId, config, listener);
    }

    public static void displayUri(ImageView view, Uri uri, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().displayUri(view, uri, config, listener);
    }

    public static void displayUrl(ImageView view, String url, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().displayUrl(view, url, config, listener);
    }

    public static void displayFile(ImageView view, File file, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().displayFile(view, file, config, listener);
    }

    public static void loadImageByUri(Context context, Uri uri, String detinationPath, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().loadImageByUri(context, uri, detinationPath, config, listener);
    }

    public static void loadImageByUrl(Context context, String url, String detinationPath, ImageLoadConfig config, BitmapLoadingListener listener){
        ImageLoaderProxy.getInstance().loadImageByUrl(context, url, detinationPath, config, listener);
    }

    public void cancelAllTasks(Context context){
        ImageLoaderProxy.getInstance().cancelAllTasks(context);
    }

    public void resumeAllTasks(Context context){
        ImageLoaderProxy.getInstance().resumeAllTasks(context);
    }
    public void clearDiskCache(final Context context){
        ImageLoaderProxy.getInstance().clearDiskCache(context);
    }

    public void cleanAll(Context context){
        ImageLoaderProxy.getInstance().cleanAll(context);
    }

}
