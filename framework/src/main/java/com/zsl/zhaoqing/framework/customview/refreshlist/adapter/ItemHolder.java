package com.zsl.zhaoqing.framework.customview.refreshlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by zsl on 2017/7/26.
 */

public abstract class ItemHolder extends RecyclerView.ViewHolder implements UpdateItem {

    protected Context mCtx;

    public ItemHolder(View itemView) {
        super(itemView);
        mCtx = itemView.getContext();
    }

}
