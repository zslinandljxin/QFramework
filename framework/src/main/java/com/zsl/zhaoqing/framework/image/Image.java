package com.zsl.zhaoqing.framework.image;


import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;


/**
 * Created by Administrator on 2017/6/12.
 */
 public interface Image {
    void setMode(int mode);
    void setImageConfig(Context context, ImageLoadConfig config);
    void displayUrl(ImageView view, String imageUrl, ImageLoadConfig config, BitmapLoadingListener listener);
    void displayFile(ImageView view, File file, ImageLoadConfig config, BitmapLoadingListener listener);
    void displayResId(ImageView view, Integer resourceId, ImageLoadConfig config, BitmapLoadingListener listener);
    void displayUri(ImageView view, Uri uri, ImageLoadConfig config, BitmapLoadingListener listener);
    void loadImageByUri(Context context, Uri uri, String detinationPath, ImageLoadConfig config, BitmapLoadingListener listener);
    void loadImageByUrl(Context context, String url, String detinationPath, ImageLoadConfig config, final BitmapLoadingListener listener);
    void cancelAllTasks(Context context);
    void resumeAllTasks(Context context);
    void clearDiskCache(final Context context);
    void cleanAll(Context context);
}
