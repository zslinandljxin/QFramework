package com.zsl.zhaoqing.qframework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsl.zhaoqing.framework.customview.draggrid.DragGridAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */

public class MyDragAdapter extends DragGridAdapter<String> {

    public Context mContext;

    public MyDragAdapter(Context context, List list){
        super(list);
        this.mContext = context;
    }

    public MyDragAdapter(List list) {
        super(list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent) {
        String text = getList().get(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(text);
        return convertView;
    }

    @Override
    public int getCount() {
        return getList().size();
    }

    @Override
    public Object getItem(int position) {
        return getList().get(position);
    }
}
