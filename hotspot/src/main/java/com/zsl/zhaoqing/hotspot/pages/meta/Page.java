package com.zsl.zhaoqing.hotspot.pages.meta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zsl on 2017/7/13.
 */

public abstract class Page implements BasePage {

    protected Context mContext;
    private View mView;

    @Override
    public void onCreatePager(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDestroyPager() {
        this.mContext = null;
        this.mView = null;
    }

    protected void setPagerView(int resId){
        mView = LayoutInflater.from(mContext).inflate(resId, null, false);
    }

    protected void setPagerView(View view){
        this.mView = view;
    }

    @NotNull
    @Override
    public View getContentView() {
        if (mView == null){
            throw new RuntimeException("Page view is null.");
        }
        return mView;
    }
}
