package com.zsl.zhaoqing.hotspot.items.meta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.items.meta.BaseItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zsl on 2017/7/24.
 */

public abstract class ListItem<T> extends RecyclerView.ViewHolder implements BaseItem<T> {

    protected Context mContext;
    protected Map<String, OnItemListener> mObservors = null;

    public ListItem(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mObservors = new HashMap<>();
    }

    public static View getView(Context context,int resId){
        return LayoutInflater.from(context).inflate(resId, null);
    }

    @Override
    public void updateItem(int position, T t){
    }

    public void addOnItemListener(String key, OnItemListener listener){
        if (TextUtils.isEmpty(key)){
            throw new IllegalArgumentException("Please provide the corret key!");
        }
        if (mObservors.get(key) != null){
            return;
        }
        mObservors.put(key, listener);
    }

}
