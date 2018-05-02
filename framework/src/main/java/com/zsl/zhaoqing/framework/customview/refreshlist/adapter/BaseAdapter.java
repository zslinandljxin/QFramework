package com.zsl.zhaoqing.framework.customview.refreshlist.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<ItemWrapper> mData;
    private OnLastItemListener mListener;

    public BaseAdapter(List<ItemWrapper> data){
        this.mData = data;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.UpdateItemView(mData.get(position));
    }

    public void setOnLastItemListener(OnLastItemListener lastItemListener){
        this.mListener = lastItemListener;
    }

    public interface OnLastItemListener{
        void lastItem(ItemHolder holder, int position);
    }
}
