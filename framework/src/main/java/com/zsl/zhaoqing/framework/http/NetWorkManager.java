package com.zsl.zhaoqing.framework.http;


import com.zsl.zhaoqing.framework.http.request.JRequest;
import com.zsl.zhaoqing.framework.http.retrofit.RetrofitImpl;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by zsl on 2017/3/15.
 */

public class NetWorkManager {

    public static final int METHOD_GET = 0x8888;
    public static final int METHOD_POST = 0x9999;
    private static INetWork mInstance;
    private static NetConfig mNetConfig;
    private static volatile boolean mInitialize = false;

    private NetWorkManager(){}

    public static void init(NetConfig config){
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mNetConfig = config;
                    mInstance = new RetrofitImpl(config);
                    mInitialize = true;
                }
            }
        }
    }

    public static void addUrl(String baseUrl){
        checkIfInit();
        mInstance.addBaseUrl(baseUrl);
    }

    public static void sendRequest(JRequest request, OnCallback onCallback){
        checkIfInit();
        mInstance.sendRequest(request, onCallback);
    }

    public static Observable<JSONObject> sendObservableData(JRequest request){
        checkIfInit();
        return mInstance.sendObservableData(request);
    }

    private static void checkIfInit(){
        synchronized (NetWorkManager.class){
            if (!mInitialize){
                try {
                    throw new Exception("The NetWorkManager is not initialize.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
