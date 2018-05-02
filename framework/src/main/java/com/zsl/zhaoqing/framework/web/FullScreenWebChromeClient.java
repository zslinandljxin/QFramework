package com.zsl.zhaoqing.framework.web;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import static android.view.View.GONE;

/**
 * Created by zsl on 2017/4/6.
 */

public class FullScreenWebChromeClient extends WebChromeClient {

    private X5WebView mWebView;
    private Activity mActivity;
    private View mCustomView;
    private ProgressBar mProgressBar;
    private FrameLayout mCustomViewContainer;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

    public FullScreenWebChromeClient(X5WebView webView) {
        this.mWebView = webView;
        mProgressBar = mWebView.getProgressBar();
        if (webView.getContext() instanceof Activity) {
            mActivity = (Activity) webView.getContext();
        }
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        if (mProgressBar != null){
            if (i == 100){
                if (mProgressBar.getVisibility() == View.VISIBLE){
                    mProgressBar.setVisibility(GONE);
                    mProgressBar.setProgress(0);
                }
            }else {
                if (mProgressBar.getVisibility() == GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(i);
            }
        }
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        if (mCustomView != null) {
            customViewCallback.onCustomViewHidden();
            return;
        }
        mWebView.setVisibility(GONE);
        if (null != mActivity && null != mActivity.getWindow()) {
            mCustomViewContainer = new FrameLayout(mActivity);
            mCustomViewContainer.setBackgroundColor(Color.BLACK);
            mCustomViewContainer.addView(view);
            mActivity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ViewGroup group = (ViewGroup) mActivity.getWindow().getDecorView();
            group.addView(mCustomViewContainer, 0 , new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        mCustomViewContainer.bringToFront();
        mCustomView = view;
        mCustomViewCallback = customViewCallback;
    }

    @Override
    public void onHideCustomView() {
        if (mCustomView == null){
            return;
        }
        mCustomViewCallback.onCustomViewHidden();
        mCustomView.setVisibility(GONE);
        mCustomViewContainer.removeView(mCustomView);
        if (null != mActivity && null != mActivity.getWindow()) {
            mActivity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup group = (ViewGroup) mActivity.getWindow().getDecorView();
            group.removeView(mCustomViewContainer);
        }
        mWebView.setVisibility(View.VISIBLE);
        mCustomView = null;
        mCustomViewCallback = null;
        mCustomViewContainer = null;
    }

}
