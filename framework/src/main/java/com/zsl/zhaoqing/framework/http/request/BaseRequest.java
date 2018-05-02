package com.zsl.zhaoqing.framework.http.request;


import com.zsl.zhaoqing.framework.http.NetWorkManager;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/20.
 */

public class BaseRequest implements JRequest {
    @Override
    public int getMethod() {
        return NetWorkManager.METHOD_GET;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Override
    public String getAction() {
        return null;
    }

    @Override
    public Map<String, Object> getParams() {
        return null;
    }

    @Override
    public Map<String, File> postFile() {
        return null;
    }
}
