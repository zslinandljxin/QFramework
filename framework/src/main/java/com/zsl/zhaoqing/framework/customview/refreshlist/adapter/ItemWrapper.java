package com.zsl.zhaoqing.framework.customview.refreshlist.adapter;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ItemWrapper<T> {
    private int viewType;
    private T t;

    public ItemWrapper(T t) {
        this.t = t;
    }

    public ItemWrapper(int viewType, T t) {
        this.viewType = viewType;
        this.t = t;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
