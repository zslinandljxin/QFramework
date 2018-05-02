package com.zsl.zhaoqing.framework.http.request;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/31.
 */

public interface JRequest {

    int getMethod();
    String getBaseUrl();
    String getAction();
    Map<String, Object> getParams();
    Map<String, File> postFile();


}
