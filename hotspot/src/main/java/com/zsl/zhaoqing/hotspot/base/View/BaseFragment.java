package com.zsl.zhaoqing.hotspot.base.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsl.zhaoqing.hotspot.base.presenter.IPresenter;


/**
 * Created by zsl on 2017/7/7.
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment {
    protected View mView;
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = onAttachPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = initView(inflater);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null){
            mPresenter.onCreate(savedInstanceState);
        }
        addListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null){
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null){
            mPresenter.onResume();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mPresenter != null){
            mPresenter.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null){
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null){
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if (mView != null){
            mView = null;
        }
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract P onAttachPresenter();

    public abstract void addListener();
}
