package com.zsl.zhaoqing.hotspot.base.presenter;

import android.os.Bundle;

/**
 * Created by zsl on 2017/4/7.
 */

public interface IPresenter {
    void onCreate(Bundle bundle);
    void onStart();
    void onRestart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void onHiddenChanged(boolean hidden);
}
