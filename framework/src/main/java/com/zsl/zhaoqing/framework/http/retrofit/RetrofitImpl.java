package com.zsl.zhaoqing.framework.http.retrofit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zsl.zhaoqing.framework.http.INetWork;
import com.zsl.zhaoqing.framework.http.NetConfig;
import com.zsl.zhaoqing.framework.http.OnCallback;
import com.zsl.zhaoqing.framework.http.request.JRequest;
import com.zsl.zhaoqing.framework.http.response.BaseResponse;
import com.zsl.zhaoqing.framework.http.response.JResponse;
import com.zsl.zhaoqing.framework.http.retrofit.converter.JsonCovnerterFactory;

import org.json.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

import static com.zsl.zhaoqing.framework.http.NetWorkManager.METHOD_GET;
import static com.zsl.zhaoqing.framework.http.NetWorkManager.METHOD_POST;


/**
 * Created by Administrator on 2017/6/20.
 */

public class RetrofitImpl implements INetWork {

    private Retrofit mRetrofit;
    private OkHttpClient mClientSafe;
    private OkHttpClient mClientUnSafe;
    private Map<String, APIService> mApiServices;
    private NetConfig mConfig;

    public RetrofitImpl(NetConfig config){
        this.mConfig = config;
        realInit();
    }

    @Override
    public void addBaseUrl(String baseUrl){
        if (mApiServices.get(baseUrl) != null){
            return;
        }
//        if (baseUrl.startsWith("https:")) {
//            mRetrofit = createRetrofit(mClientSafe, baseUrl);
//        }else {
            mRetrofit = createRetrofit(mClientUnSafe, baseUrl);
//        }
        mApiServices.put(baseUrl, mRetrofit.create(APIService.class));
    }

    @Override
    public void sendRequest(JRequest request, OnCallback onCallback) {
        checkRequestIfAvaiable(request);
        int method = request.getMethod();
        if (METHOD_GET == method){
            if (request.getParams() != null) {
                getRequest(mApiServices.get(request.getBaseUrl()), request.getAction(), request.getParams(), onCallback);
            }else {
                getRequest(mApiServices.get(request.getBaseUrl()), request.getAction(), onCallback);
            }
        }else if (METHOD_POST == method){
            postRequest(mApiServices.get(request.getBaseUrl()), request.getAction(), request.postFile(), onCallback);
        }
    }

    @Override
    public Observable<JSONObject> sendObservableData(JRequest request) {
        checkRequestIfAvaiable(request);
        int method = request.getMethod();
        if (METHOD_POST == method){
            return postObservableData(request);
        }
        if (request.getParams() != null) {
            return mApiServices.get(request.getBaseUrl()).getObservableData(request.getAction(), request.getParams());
        }else {
            return mApiServices.get(request.getBaseUrl()).getObservableData(request.getAction());
        }
    }

    private void realInit(){
        mClientSafe = getsafeOkHttpClient(mConfig);
        mClientUnSafe = getUnsafeOkHttpClient(mConfig);
//        if (mConfig.getBaseUrl().startsWith("https:")){
//            mRetrofit = createRetrofit(mClientSafe, mConfig.getBaseUrl());
//        }else {
            mRetrofit = createRetrofit(mClientUnSafe, mConfig.getBaseUrl());
//        }
        mApiServices = new HashMap<>();
        mApiServices.put(mConfig.getBaseUrl(), mRetrofit.create(APIService.class));
    }

    private Observable<JSONObject> postObservableData(JRequest request) {
        if (request == null) Observable.error(new IllegalArgumentException("Request can not be empty."));
        if (TextUtils.isEmpty(request.getBaseUrl())) Observable.error(new IllegalArgumentException("BaseUrl can not be empty.")) ;
        Map<String, File> parts = request.postFile();
        if (parts != null && parts.size() > 0) {
            Set<String> keys = parts.keySet();
            if (keys != null && keys.size() > 0) {
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                List<String> keyLists = new ArrayList<>(keys);
                for (int i = 0; i < keyLists.size(); i++) {
                    File file = parts.get(keyLists.get(i));
                    RequestBody body = RequestBody.create(
                            MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart(keyLists.get(i), file.getName(), body);
                }
                List<MultipartBody.Part> multiparts = builder.build().parts();
                return mApiServices.get(request.getBaseUrl()).postObservableData(request.getAction(), multiparts);
            }
        }
        return Observable.error(new IllegalArgumentException("post data is null!"));
    }

    private void checkRequestIfAvaiable(JRequest request) {
        if (request == null) new IllegalArgumentException("Request can not be empty.");
        if (TextUtils.isEmpty(request.getBaseUrl())){
            throw new IllegalArgumentException("BaseUrl can not be empty.");
        }else {
            addBaseUrl(request.getBaseUrl());
        }
        if (TextUtils.isEmpty(request.getAction())) throw new IllegalArgumentException("Request's action is empty.");
    }

    private synchronized void getRequest(APIService apiService, String action, final OnCallback onCallback) {
        apiService.getData(action).enqueue(new Callback<JSONObject>() {
            //callbacks will be executed on the main thread
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                handleSuccess(response, onCallback);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                t.printStackTrace();
                handleFailure(t, onCallback);
            }
        });
    }

    private synchronized void getRequest(APIService apiService, String action, @NonNull Map<String, Object> params, final OnCallback onCallback) {
        apiService.getData(action, params).enqueue(new Callback<JSONObject>() {
            //callbacks will be executed on the main thread
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                handleSuccess(response, onCallback);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                handleFailure(t, onCallback);
            }
        });
    }

    private void postRequest(APIService apiService, String action, Map<String, File> parts, final OnCallback onCallback) {
        if (parts != null && parts.size() > 0) {
            Set<String> keys = parts.keySet();
            if (keys != null && keys.size() > 0) {
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                List<String> keyLists = new ArrayList<>(keys);
                for (int i = 0; i < keyLists.size(); i++) {
                    File file = parts.get(keyLists.get(i));
                    RequestBody body = RequestBody.create(
                            MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart(keyLists.get(i), file.getName(), body);
                }
                List<MultipartBody.Part> multiparts = builder.build().parts();
                apiService.postData(action, multiparts).enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        handleSuccess(response, onCallback);
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        handleFailure(t, onCallback);
                    }
                });
            }
        }
    }

    private void handleFailure(Throwable t, OnCallback onCallback) {
        if (onCallback != null) {
            onCallback.onFailed(new Exception(t), new BaseResponse(false, 0, null));
        }
    }

    private void handleSuccess(Response<JSONObject> response, OnCallback onCallback) {
        if (onCallback != null) {
            int code = response.code();
            JSONObject jsonObject = response.body();
            if (code != HttpURLConnection.HTTP_PARTIAL &&
                    code != HttpURLConnection.HTTP_OK){
                onCallback.onFailed(new Exception("The request is going wrong."),
                        new BaseResponse(false, code, jsonObject));
                return;
            }
            JResponse resp = new BaseResponse(true, code, jsonObject);
            onCallback.onSuccess(resp);
        }
    }

    private Retrofit createRetrofit(OkHttpClient client, String baseUrl){
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JsonCovnerterFactory.create())
                .build();
    }

    private OkHttpClient getUnsafeOkHttpClient(NetConfig config){
        return new OkHttpClient.Builder()
                .addInterceptor(new AddHeaderInterceptor(config.getHeaders()))
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure())
                .build();
    }

    private OkHttpClient getsafeOkHttpClient(NetConfig config) {
        final String PUB_KEY = "3082010a0282010100d52ff5dd432b3a05113ec1a7065fa5a80308810e4e181cf14f7598c8d553cccb7d5111fdcdb55f6ee84fc92cd594adc1245a9c4cd41cbe407a919c5b4d4a37a012f8834df8cfe947c490464602fc05c18960374198336ba1c2e56d2e984bdfb8683610520e417a1a9a5053a10457355cf45878612f04bb134e3d670cf96c6e598fd0c693308fe3d084a0a91692bbd9722f05852f507d910b782db4ab13a92a7df814ee4304dccdad1b766bb671b6f8de578b7f27e76a2000d8d9e6b429d4fef8ffaa4e8037e167a2ce48752f1435f08923ed7e2dafef52ff30fef9ab66fdb556a82b257443ba30a93fda7a0af20418aa0b45403a2f829ea6e4b8ddbb9987f1bf0203010001";
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                    if (chain == null) {
                        throw new IllegalArgumentException("checkServerTrusted:x509Certificate array is null");
                    }

                    if (!(chain.length > 0)) {
                        throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                    }

                    if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
                        throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                    }

                    // Perform customary SSL/TLS checks
                    try {
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                        tmf.init((KeyStore) null);
                        for (TrustManager trustManager : tmf.getTrustManagers()) {
                            ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                        }
                    } catch (Exception e) {
                        throw new CertificateException(e);
                    }
                    // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                    // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                    RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();

                    String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);
                    // Pin it!
                    final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);

                    if (!expected) {
                        throw new CertificateException("checkServerTrusted: Expected public key: "
                                + PUB_KEY + ", got public key:" + encoded);
                    }
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .addInterceptor(new AddHeaderInterceptor(config.getHeaders()))
                    .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(config.isRetryOnConnectionFailure())
                    .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

