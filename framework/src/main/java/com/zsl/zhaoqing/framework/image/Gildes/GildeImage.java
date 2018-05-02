package com.zsl.zhaoqing.framework.image.Gildes;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.zsl.zhaoqing.framework.image.BitmapLoadingListener;
import com.zsl.zhaoqing.framework.image.Const;
import com.zsl.zhaoqing.framework.image.Gildes.transformation.BlurTransformation;
import com.zsl.zhaoqing.framework.image.Gildes.transformation.CropCircleTransformation;
import com.zsl.zhaoqing.framework.image.Gildes.transformation.GrayscaleTransformation;
import com.zsl.zhaoqing.framework.image.Gildes.transformation.RotateTransformation;
import com.zsl.zhaoqing.framework.image.Gildes.transformation.RoundedCornersTransformation;
import com.zsl.zhaoqing.framework.image.Image;
import com.zsl.zhaoqing.framework.image.ImageLoadConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.bumptech.glide.Glide.with;

/**
 * Created by Administrator on 2017/6/12.
 */
public class GildeImage implements Image {
    //默认配置
    private ImageLoadConfig defConfig = new ImageLoadConfig.Builder().
            setCropType(ImageLoadConfig.CENTER_CROP).
            setAsBitmap(true).
            setDiskCacheStrategy(ImageLoadConfig.DiskCache.SOURCE).
            setPrioriy(ImageLoadConfig.LoadPriority.HIGH).build();

    @Override
    public void displayUrl(ImageView view, String imageUrl, ImageLoadConfig config, BitmapLoadingListener listener) {
        load(view.getContext(), view, imageUrl, config, listener);
    }

    @Override
    public void displayFile(ImageView view, File file, ImageLoadConfig config, BitmapLoadingListener listener) {
        load(view.getContext(), view, file, config, listener);
    }

    @Override
    public void displayResId(ImageView view, Integer resourceId, ImageLoadConfig config, BitmapLoadingListener listener) {
        load(view.getContext(), view, resourceId, config, listener);
    }

    @Override
    public void displayUri(ImageView view, Uri uri, ImageLoadConfig config, BitmapLoadingListener listener) {
        load(view.getContext(), view, uri, config, listener);
    }

    @Override
    public void loadImageByUri(Context context, Uri uri, final String detinationPath, ImageLoadConfig config, final BitmapLoadingListener listener) {
        if (uri == null) {
            if (listener != null) {
                listener.onError();
            }
        } else {
            with(context).
                    load(uri).
                    asBitmap().
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    dontAnimate().
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            BufferedOutputStream os = null;
                            try {
                                File file = new File(detinationPath);
                                if (file.exists()){
                                    file.delete();
                                }
                                os = new BufferedOutputStream(new FileOutputStream(file));
                                resource.compress(Bitmap.CompressFormat.PNG, 100, os);
                                os.flush();
                                os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                if (os != null){
                                    try {
                                        os.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    os = null;
                                }
                            }
                            if (listener != null) {
                                listener.onSuccess(resource);
                            }
                        }
                    });
        }
    }

    @Override
    public void loadImageByUrl(Context context, String url, final String detinationPath, ImageLoadConfig config, final BitmapLoadingListener listener) {
        loadImageByUri(context, Uri.parse(url), detinationPath, config, listener);
    }

    @Override
    public void setMode(int mode) {

    }

    @Override
    public void setImageConfig(Context context, ImageLoadConfig config){
        this.defConfig = config;
    }

    private void load(Context context, ImageView view, Object objUrl,
                      ImageLoadConfig config, final BitmapLoadingListener listener) {
        if (null == objUrl) {
            throw new IllegalArgumentException("objUrl is null");
        }
        if (null == config) {
            config = defConfig;
        }
        try {
            GenericRequestBuilder builder = null;
            DrawableTypeRequest request = null;
            if (context == null){
                throw new IllegalArgumentException("context is null");
            }
            request = with(context).load(objUrl);
            if (config.isAsGif()) {//gif类型
                GifRequestBuilder gifRequest = request.asGif();
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    gifRequest.centerCrop();
                } else {
                    gifRequest.fitCenter();
                }
                builder = gifRequest;
            } else if (config.isAsBitmap()) {  //bitmap 类型
                BitmapRequestBuilder bitmapRequest = request.asBitmap();
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    bitmapRequest.centerCrop();
                } else {
                    bitmapRequest.fitCenter();
                }
                //transform bitmap
                if (config.isRoundedCorners()) {
                    bitmapRequest.transform(new RoundedCornersTransformation(context, 50, 50));
                } else if (config.isCropCircle()) {
                    bitmapRequest.transform(new CropCircleTransformation(context));
                } else if (config.isGrayscale()) {
                    bitmapRequest.transform(new GrayscaleTransformation(context));
                } else if (config.isBlur()) {
                    bitmapRequest.transform(new BlurTransformation(context, 8));
                } else if (config.isRotate()) {
                    bitmapRequest.transform(new RotateTransformation(context, config.getRotateDegree()));
                }
                builder = bitmapRequest;
            }else if (config.isCrossFade()) { // 渐入渐出动画
                DrawableRequestBuilder drawableRequest = request.crossFade();
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    drawableRequest.centerCrop();
                } else {
                    drawableRequest.fitCenter();
                }
                builder = drawableRequest;
            }else {
                builder = request;
            }
            //缓存设置
            builder.diskCacheStrategy(config.getDiskCacheStrategy().getStrategy()).
                    skipMemoryCache(config.isSkipMemoryCache()).
                    priority(config.getPrioriy().getPriority());
            builder.dontAnimate().into(view);
            if (null != config.getTag()) {
                builder.signature(new StringSignature(config.getTag()));
            } else {
                builder.signature(new StringSignature(objUrl.toString()));
            }
            if (null != config.getAnimator()) {
                builder.animate(config.getAnimator());
            } else if (null != config.getAnimResId()) {
                builder.animate(config.getAnimResId());
            }
            if (config.getThumbnail() > 0.0f) {
                builder.thumbnail(config.getThumbnail());
            }
            if (null != config.getErrorResId()) {
                builder.error(config.getErrorResId());
            }
            if (null != config.getPlaceHolderResId()) {
                builder.placeholder(config.getPlaceHolderResId());
            }
            if (null != config.getSize()) {
                builder.override(config.getSize().getWidth(), config.getSize().getHeight());
            }
            if (null != listener) {
                setListener(builder, listener);
            }
            if (null != config.getThumbnailUrl()) {
                BitmapRequestBuilder thumbnailRequest = with(context).load(config.getThumbnailUrl()).asBitmap();
                builder.thumbnail(thumbnailRequest).into(view);
            } else {
                setTargetView(builder, config, view);
            }
        } catch (Exception e) {
            if (config.getErrorResId() != null){
                view.setImageResource(config.getErrorResId());
            }
        }
    }

    private void setListener(GenericRequestBuilder request, final BitmapLoadingListener listener) {
        request.listener(new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                if (!e.getMessage().equals("divide by zero")) {
                    listener.onError();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                listener.onSuccess(null);
                return false;
            }
        });
    }

    private void setTargetView(GenericRequestBuilder request, ImageLoadConfig config, ImageView view) {
        //set targetView
        if (null != config.getSimpleTarget()) {
            request.into(config.getSimpleTarget());
        } else if (null != config.getViewTarget()) {
            request.into(config.getViewTarget());
        } else if (null != config.getNotificationTarget()) {
            request.into(config.getNotificationTarget());
        } else if (null != config.getAppWidgetTarget()) {
            request.into(config.getAppWidgetTarget());
        } else {
            request.into(view);
        }
    }

    /**
     * 加载bitmap
     *
     * @param context
     * @param url
     * @param listener
     */


    /**
     * 高优先级加载
     *
     * @param url
     * @param imageView
     * @param listener
     */
    public void loadImageWithHighPriority(Object url, ImageView imageView, final BitmapLoadingListener listener) {
        if (url == null) {
            if (listener != null) {
                listener.onError();
            }
        } else {
            with(imageView.getContext()).
                    load(url).
                    asBitmap().
                    priority(Priority.HIGH).
                    dontAnimate().
                    listener(new RequestListener<Object, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            if (null != listener) {
                                listener.onError();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (null != listener) {
                                listener.onSuccess(resource);
                            }
                            return false;
                        }
                    }).into(imageView);
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    @Override
    public void cancelAllTasks(Context context) {
        with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    @Override
    public void resumeAllTasks(Context context) {
        with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    @Override
    public void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }
    /**
     * 清除所有缓存
     * @param context
     */
    @Override
    public void cleanAll(Context context) {
        clearDiskCache(context);
        Glide.get(context).clearMemory();
    }
    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    public synchronized long getDiskCacheSize(Context context) {
        long size = 0L;
//        File cacheDir = PathUtils.getDiskCacheDir(context, CacheConfig.IMG_DIR);
        File cacheDir = new File(Const.IMG_DIR);
        if (cacheDir != null && cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                File[] arr$ = files;
                int len$ = files.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File imageCache = arr$[i$];
                    if (imageCache.isFile()) {
                        size += imageCache.length();
                    }
                }
            }
        }

        return size;
    }

    public void clearTarget(Context context, String uri) {
//        if (SimpleGlideModule.cache != null && uri != null) {
//            SimpleGlideModule.cache.delete(new StringSignature(uri));
//            Glide.get(context).clearMemory();
//        }
    }

    public void clearTarget(View view) {
        Glide.clear(view);
    }

//    public static File getTarget(Context context, String uri) {
//        return SimpleGlideModule.cache != null && uri != null ? SimpleGlideModule.cache.get(new StringSignature(uri)) : null;
//    }
}
