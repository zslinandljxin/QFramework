package com.zsl.zhaoqing.framework.customview.draggrid;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zsl on 2017/11/20.
 */

public abstract class DragGridAdapter<T> extends BaseAdapter {

    private static final String TAG = "DragGridAdapter";

    private boolean isEditStatus = false;

    private boolean isMove = false;

    private int movePosition = -1;

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public DragGridAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getItemView(position, convertView, parent);
        if (position == movePosition && isMove) {
            view.setVisibility(View.INVISIBLE);
            isMove = false;
        }else{
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    /**
     * 给item交换位置
     *
     * @param originalPosition item原先位置
     * @param nowPosition      item现在位置
     */
    public void exchangePosition(int originalPosition, int nowPosition, boolean isMove) {
        T t = list.get(originalPosition);
        list.remove(originalPosition);
        list.add(nowPosition, t);
        movePosition = nowPosition;
        this.isMove = isMove;
        notifyDataSetChanged();
    }

    public void setEditStatus(boolean edit){
        this.isEditStatus = edit;
        notifyDataSetChanged();
    }

    public boolean getEditStatus(){
        return isEditStatus;
    }

    public abstract boolean canItemDrag(int position);

}
