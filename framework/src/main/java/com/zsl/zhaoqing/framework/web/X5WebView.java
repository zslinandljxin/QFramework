package com.zsl.zhaoqing.framework.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


/**
 * Created by zsl on 2017/4/6.
 */

public class X5WebView extends WebView {

    private ProgressBar mProgressbar;

    public X5WebView(Context context) {
        super(context);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void hideProgressBar() {
        if (null != mProgressbar) {
            mProgressbar.setVisibility(GONE);
        }
    }

    public void showProgressBar(){
        if (null != mProgressbar) {
            mProgressbar.setVisibility(VISIBLE);
        }
    }

    public ProgressBar getProgressBar(){
        return mProgressbar;
    }

    private void init(){
        mProgressbar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        mProgressbar.setLayoutParams(new ViewGroup.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 5));
        mProgressbar.setMax(100);
        addView(mProgressbar);
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);

        //默认隐藏
        hideProgressBar();
        initWebViewSettings();
    }

    private void initWebViewSettings(){
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
        setWebChromeClient(new FullScreenWebChromeClient(this));
        setDownloadListener(new WebViewDownloadListener(getContext()));
    }

}
