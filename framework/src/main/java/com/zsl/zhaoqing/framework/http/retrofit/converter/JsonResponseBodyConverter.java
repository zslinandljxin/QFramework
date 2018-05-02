package com.zsl.zhaoqing.framework.http.retrofit.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017/2/8.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    public JsonResponseBodyConverter() {
        super();
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(value.string());
            return (T) jsonObj;
        } catch (JSONException e) {
            return null;
        }
    }
}
