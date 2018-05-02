package com.zsl.zhaoqing.framework.http;

import com.zsl.zhaoqing.framework.http.request.JRequest;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by zsl on 2017/6/20.
 */

public interface INetWork {
    void addBaseUrl(String baseUrl);
    void sendRequest(JRequest request, OnCallback onCallback);
    Observable<JSONObject> sendObservableData(JRequest request);
}
