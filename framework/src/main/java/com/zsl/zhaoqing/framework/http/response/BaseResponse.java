package com.zsl.zhaoqing.framework.http.response;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/20.
 */

public class BaseResponse extends JResponse {

    public BaseResponse(boolean isSuccess, int statusCode, JSONObject jsonObject) {
        super(isSuccess, statusCode, jsonObject);
    }

}
