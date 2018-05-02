package com.zsl.zhaoqing.framework.http.retrofit.converter;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/2/8.
 */

public class JsonCovnerterFactory extends Converter.Factory {

    private JsonCovnerterFactory(){}

    public static JsonCovnerterFactory create(){
        return new JsonCovnerterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (JSONObject.class.equals(type)) {
            return new JsonResponseBodyConverter<JSONObject>();
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (JSONObject.class.equals(type)) {
            return new JsonRequestBodyConverter<JSONObject>();
        }
        return null;
    }
}
