package com.zsl.zhaoqing.framework.base.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsl.zhaoqing.framework.base.presenter.IPresenter;
import com.zsl.zhaoqing.framework.utils.LogUtil;


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
        LogUtil.i(getClass().getSimpleName() + ":","<------onCreate------>");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(getClass().getSimpleName() + ":","<------onCreateView------>");
        if (mView == null){
            mView = initView(inflater);
            addListener();
            if (mPresenter != null){
                mPresenter.onCreate(savedInstanceState);
            }
        }else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null){
                parent.removeView(mView);
            }
        }
        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null){
            mPresenter.onStart();
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onStart------>");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null){
            mPresenter.onResume();
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onResume------>");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null){
            mPresenter.onPause();
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onPause------>");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null){
            mPresenter.onStop();
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onStop------>");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onDestroy------>");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mPresenter != null) {
            mPresenter.onHiddenChanged(hidden);
        }
        LogUtil.i(getClass().getSimpleName() + ":","<------onHiddenChanged------>");
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract P onAttachPresenter();

    public abstract void addListener();
}
