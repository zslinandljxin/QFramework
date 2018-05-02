package com.aspire.redu.hot.fragment;

import com.zsl.zhaoqing.hotspot.base.View.contract.IContract;
import com.zsl.zhaoqing.hotspot.beans.Channel;

import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public interface MainContract extends IContract {
    void updateTabIndicator(List<Channel> channels);
}
