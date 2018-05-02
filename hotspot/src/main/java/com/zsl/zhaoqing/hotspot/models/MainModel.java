package com.zsl.zhaoqing.hotspot.models;

import com.zsl.zhaoqing.hotspot.base.model.IModel;
import com.zsl.zhaoqing.hotspot.beans.Channel;

import java.util.List;
import java.util.Objects;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zsl on 2017/11/13.
 */

public class MainModel implements IModel {

    private List<Object> mUserChannels;
    private List<Object> mALLChannels;

    public MainModel(){
        initAllChannels();
    }

    private void initAllChannels(){

    }

    public Observable<List<Channel>> requestUserChannels(){
        Observable fromCache = Observable.create(new Observable.OnSubscribe<List<Object>>() {
            @Override
            public void call(Subscriber<? super List<Object>> subscriber) {
                if (mUserChannels != null && mUserChannels.size() > 0){
                    //缓存有数据，直接返回
                    subscriber.onNext(mUserChannels);
                    subscriber.onCompleted();
                }
            }
        });
        Observable fromAllChoice = Observable.create(new Observable.OnSubscribe<List<Object>>() {
            @Override
            public void call(Subscriber<? super List<Object>> subscriber) {
                //1、读取用户选择的频道列表
                List<String> channelNames = null;
                if (channelNames != null && channelNames.size() > 0){
                    if (mALLChannels != null && mALLChannels.size() > 0){
                        Observable.from(channelNames).filter(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String o) {
                                //根据频道名称过滤
                                return null;
                            }
                        }).flatMap(new Func1<String, Observable<Channel>>() {
                            @Override
                            public Observable<Channel> call(String s) {
                                //得到频道
                                return null;
                            }
                        });
                    }
                }
            }
        });

        return Observable.concat(fromCache, fromAllChoice).first();
    }

    public List<Object> requestAllChannels(){
        return mALLChannels;
    }

}
