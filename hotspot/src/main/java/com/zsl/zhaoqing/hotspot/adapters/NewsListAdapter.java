package com.zsl.zhaoqing.hotspot.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.zsl.zhaoqing.hotspot.items.ItemFactory;
import com.zsl.zhaoqing.hotspot.items.meta.ListItem;
import com.zsl.zhaoqing.hotspot.items.meta.OnItemListener;

import java.util.List;

/**
 * Created by zsl on 2017/7/18.
 */

public class NewsListAdapter extends RecyclerView.Adapter<ListItem> {

    private List<Object> mDatas;

    public NewsListAdapter(List<Object> data){
        this.mDatas = data;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;

    }

    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItem holder = ItemFactory.create(parent.getContext(), viewType);
        holder.addOnItemListener("delete", new OnItemListener() {
            @Override
            public void onStatusChanged(int position) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ListItem holder, int position) {
        holder.updateItem(position, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
