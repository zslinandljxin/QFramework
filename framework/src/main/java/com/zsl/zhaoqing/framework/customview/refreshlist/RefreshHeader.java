package com.zsl.zhaoqing.framework.customview.refreshlist;

import android.view.View;

/**
 * Created by zsl on 2017/9/28.
 */

public interface RefreshHeader {
    final static int STATE_PULL_DOWN = 0;
    final static int STATE_RELEASE = 1;
    final static int STATE_REFRESHING = 2;
    final static int STATE_FINISH = 3;

    View getHeaderView(int state);
    void onBindHeader(View header, int state);
}
