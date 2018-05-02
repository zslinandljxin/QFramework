package com.zsl.zhaoqing.framework.http;

/**
 * Created by zsl on 2017/6/20.
 */

public class NetConfig {
    private String baseUrl = "";
    private long connectTimeout = 10 * 10000;
    private long readTimeout = 10 * 1000;
    private boolean retryOnConnectionFailure = true;
    private RequestHeader headers;

    private NetConfig(Builder builder){
        this.baseUrl = builder.baseUrl;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.headers = builder.headers;
    }

    public static class Builder{
        private String baseUrl = "";
        private long connectTimeout = 10 * 10000;
        private long readTimeout = 10 * 1000;
        private boolean retryOnConnectionFailure = true;
        private RequestHeader headers;

        public NetConfig build(){
            return new NetConfig(this);
        }

        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder connectTimeout(long connectTimeout){
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(long readTimeout){
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retry){
            this.retryOnConnectionFailure = retry;
            return this;
        }

        public Builder addRequestHeader(RequestHeader header){
            this.headers = header;
            return this;
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public RequestHeader getHeaders() {
        return headers;
    }
}
