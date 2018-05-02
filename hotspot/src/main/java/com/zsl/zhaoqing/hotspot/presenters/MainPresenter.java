package com.zsl.zhaoqing.hotspot.presenters;

import android.os.Bundle;

import com.zsl.zhaoqing.hotspot.beans.Channel;
import com.zsl.zhaoqing.hotspot.fragments.MainContract;
import com.zsl.zhaoqing.hotspot.base.presenter.IPresenter;
import com.zsl.zhaoqing.hotspot.models.MainModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zsl on 2017/11/13.
 */

public class MainPresenter implements IPresenter {

    private MainContract mContract;
    private MainModel mModel;
    private Subscription mSubscription;

    public MainPresenter(MainContract contract){
        this.mContract = contract;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.mModel = new MainModel();
        mModel.requestUserChannels()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Channel>>() {
                    @Override
                    public void call(List<Channel> objects) {
                        if (mContract != null){
                            mContract.updateTabIndicator(objects);
                        }
                    }
                });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

    }
}
