package com.zsl.zhaoqing.qframework;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

    private List<String> mData;

    public MyAdapter(List<String> data){
        this.mData = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.text.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView text;

        public VH(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
