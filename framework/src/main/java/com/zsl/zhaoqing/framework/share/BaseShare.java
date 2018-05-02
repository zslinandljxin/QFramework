package com.zsl.zhaoqing.framework.share;

import android.graphics.Bitmap;

/**
 * Created by zsl on 2017/10/19.
 */
interface BaseShare {
    void share(String type, String title, String des, String url, Bitmap thumb);
}
