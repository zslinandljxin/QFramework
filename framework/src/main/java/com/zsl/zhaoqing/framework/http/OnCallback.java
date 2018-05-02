package com.zsl.zhaoqing.framework.http;


import com.zsl.zhaoqing.framework.http.response.JResponse;

/**
 * Created by Administrator on 2017/3/31.
 */

public interface OnCallback {
    void onFailed(Exception e, JResponse response);
    void onSuccess(JResponse response);
}
