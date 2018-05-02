package com.zsl.zhaoqing.hotspot.pages.meta;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.NotNull;


/**
 * Created by zsl on 2017/7/13.
 */
 interface BasePage {
    void onCreatePager(Context context);
    void onDestroyPager();
    @NotNull
    View getContentView();
}
