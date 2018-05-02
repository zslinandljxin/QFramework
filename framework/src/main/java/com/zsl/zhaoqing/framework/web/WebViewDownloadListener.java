package com.zsl.zhaoqing.framework.web;


import android.content.Context;
import android.widget.Toast;

import com.tencent.smtt.sdk.DownloadListener;


/**
 * Created by zsl on 2017/4/6.
 */

public class WebViewDownloadListener implements DownloadListener {

    private Context mContext;

    public WebViewDownloadListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
        String suffix = null;
        if (url.lastIndexOf(".") != -1) {
            suffix = url.substring(url.lastIndexOf("."));
        } else {
            suffix = url;
        }
        int ret = WebviewDownloadUtils.downloadFile(mContext, url, suffix);
        switch (ret) {
            case -2:
                Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
                break;
            case -1:
                Toast.makeText(mContext, "下载失败，请开启下载管理器", Toast.LENGTH_LONG).show();
                break;
            case 0:
                Toast.makeText(mContext, "正在下载应用", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(mContext, "开始下载应用", Toast.LENGTH_LONG).show();
        }
    }
}

