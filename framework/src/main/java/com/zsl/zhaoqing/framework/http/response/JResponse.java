package com.zsl.zhaoqing.framework.http.response;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/31.
 */

public abstract class JResponse {
    private boolean isSuccess;
    private int statusCode;
    private JSONObject jsonObject;

    public JResponse(boolean isSuccess, int statusCode, JSONObject jsonObject) {
        this.isSuccess = isSuccess;
        this.jsonObject = jsonObject;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }

    public JSONObject getJsonObject(){
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
