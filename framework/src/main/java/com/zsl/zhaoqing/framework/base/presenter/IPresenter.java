package com.zsl.zhaoqing.framework.base.presenter;

import android.os.Bundle;

/**
zsl */

public interface IPresenter {
    void onCreate(Bundle bundle);
    void onStart();
    void onRestart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

    /**
     * thie method is only implemented in fragment.
     * @param hidden
     */
    void onHiddenChanged(boolean hidden);
}
