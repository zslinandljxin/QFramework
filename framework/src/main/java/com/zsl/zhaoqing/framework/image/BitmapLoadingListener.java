package com.zsl.zhaoqing.framework.image;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface BitmapLoadingListener {
    void onSuccess(Bitmap b);
    void onError();
}
