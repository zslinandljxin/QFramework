package com.zsl.zhaoqing.framework.http.retrofit;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zsl on 2017/3/15.
 */

public interface APIService {

    @GET("{path}")
    Call<JSONObject> getData(@Path("path") String action, @QueryMap Map<String, Object> params);

    @GET("{path}")
    Call<JSONObject> getData(@Path("path") String action);

    @Multipart
    @POST("{path}")
    Call<JSONObject> postData(@Path("path") String action, @Part List<MultipartBody.Part> partList);

    @GET("{path}")
    Observable<JSONObject> getObservableData(@Path("path") String action, @QueryMap Map<String, Object> params);

    @GET("{path}")
    Observable<JSONObject> getObservableData(@Path("path") String action);

    @Multipart
    @POST("{path}")
    Observable<JSONObject> postObservableData(@Path("path") String action, @Part List<MultipartBody.Part> partList);

}
