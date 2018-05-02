package com.zsl.zhaoqing.framework.http.retrofit;



import com.zsl.zhaoqing.framework.http.RequestHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/4/1.
 */

public class AddHeaderInterceptor implements Interceptor {
    
    RequestHeader headers;
    
    public AddHeaderInterceptor(RequestHeader headers) {
        this.headers = headers;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Map<String, String> requestHeader = headers.getRequestHeaders();
        if (requestHeader == null || requestHeader.size() == 0) {
            return chain.proceed(originRequest);
        }
        Request.Builder builder = originRequest.newBuilder();
        List<String> keys = new ArrayList<>(requestHeader.keySet());
        if (keys.size() > 0) {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                builder.addHeader(key, requestHeader.get(key));
            }
        }
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
