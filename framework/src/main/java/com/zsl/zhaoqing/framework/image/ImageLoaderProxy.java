package com.zsl.zhaoqing.framework.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;


import com.zsl.zhaoqing.framework.image.Gildes.GildeImage;

import java.io.File;

/**
 * Created by zsl on 2017/3/15.
 */
 class ImageLoaderProxy implements Image{
    private final static String IMAGELOADER = "imageloader";
    private static ImageLoaderProxy mInstance;
    private Context mContext;
    private Image mImage;
    private int mMode ;
    private boolean isInit = false;

    private ImageLoaderProxy(){
        mImage = new GildeImage();
        mMode = getImageMode();
    }

    public static ImageLoaderProxy getInstance(){
        if (mInstance == null){
            synchronized (ImageLoaderProxy.class){
                if (mInstance == null){
                    mInstance = new ImageLoaderProxy();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void setMode(int mode) {
        if (mode != this.mMode){
            this.mMode = mode;
            setImageMode(mode);
        }
    }

    @Override
    public void setImageConfig(Context context, ImageLoadConfig config){
        synchronized (ImageLoaderProxy.class){
            try{
                if (isInit){
                    throw new Exception("Image can not been init twice.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            mImage.setImageConfig(context, config);
            this.mContext = context.getApplicationContext();
            isInit = true;
        }
    }

    /**
     * 加载String类型的资源
     * SD卡资源："file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg"<p/>
     * assets资源："file:///android_asset/f003.gif"<p/>
     * raw资源："Android.resource://com.frank.glide/raw/raw_1"或"android.resource://com.frank.glide/raw/"+R.raw.raw_1<p/>
     * drawable资源："android.resource://com.frank.glide/drawable/news"或load"android.resource://com.frank.glide/drawable/"+R.drawable.news<p/>
     * ContentProvider资源："content://media/external/images/media/139469"<p/>
     * http资源："http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg"<p/>
     * https资源："https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp"<p/>
     *
     * @param view
     * @param imageUrl
     * @param config
     * @param listener
     */
    @Override
    public void displayUrl(ImageView view, String imageUrl, ImageLoadConfig config,
                           BitmapLoadingListener listener) {
        checkInit();
        mImage.displayUrl(view, imageUrl, config, listener);
    }

    @Override
    public void displayFile(ImageView view, File file, ImageLoadConfig config,
                            BitmapLoadingListener listener) {
        checkInit();
        mImage.displayFile(view, file, config, listener);
    }

    @Override
    public void displayResId(ImageView view, Integer resourceId, ImageLoadConfig config,
                             BitmapLoadingListener listener) {
        checkInit();
        mImage.displayResId(view, resourceId, config, listener);
    }

    @Override
    public void displayUri(ImageView view, Uri uri, ImageLoadConfig config,
                           BitmapLoadingListener listener) {
        checkInit();
        mImage.displayUri(view, uri, config, listener);
    }

    @Override
    public void loadImageByUri(Context context, Uri uri, String detinationPath, ImageLoadConfig config,
                               final BitmapLoadingListener listener) {
        checkInit();
        mImage.loadImageByUri(context, uri, detinationPath,  config, listener);
    }

    @Override
    public void loadImageByUrl(Context context, String url, String detinationPath, ImageLoadConfig config, final BitmapLoadingListener listener) {
        checkInit();
        mImage.loadImageByUrl(context, url, detinationPath, config, listener);
    }

    @Override
    public void cancelAllTasks(Context context) {
        mImage.cancelAllTasks(context);
    }

    @Override
    public void resumeAllTasks(Context context) {
        mImage.resumeAllTasks(context);
    }

    @Override
    public void clearDiskCache(Context context) {
        mImage.clearDiskCache(context);
    }

    @Override
    public void cleanAll(Context context) {
        mImage.cleanAll(context);
    }

    private void setImageMode(int mode){
        if (mContext != null){
            mContext.getSharedPreferences(IMAGELOADER, Context.MODE_PRIVATE).edit().putInt("mode", mode).commit();
        }
    }

    private int getImageMode(){
        if (mContext != null){
            return mContext.getSharedPreferences(IMAGELOADER, Context.MODE_PRIVATE).getInt("mode", 0);
        }
        return 0;
    }

    private void checkInit() {
            if (!isInit){
                throw new IllegalStateException("Image does ot init and will use default config .");
            }

    }

}
