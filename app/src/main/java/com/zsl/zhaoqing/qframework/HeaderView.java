package com.zsl.zhaoqing.qframework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zsl.zhaoqing.framework.customview.refreshlist.RefreshHeader;

/**
 * Created by Administrator on 2017/9/28.
 */

public class HeaderView implements RefreshHeader {

    private Context mContext;
    private LayoutInflater inflater;

    public HeaderView(Context context){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getHeaderView(int state) {
        switch (state){
            case RefreshHeader.STATE_PULL_DOWN:
                return inflater.inflate(R.layout.xiala, null);

            case RefreshHeader.STATE_RELEASE:
                return inflater.inflate(R.layout.sifang, null);

            case RefreshHeader.STATE_REFRESHING:
                return inflater.inflate(R.layout.jiazai, null);

            case RefreshHeader.STATE_FINISH:
                return inflater.inflate(R.layout.jiazai, null);

        }
        return null;
    }

    @Override
    public void onBindHeader(View header, int state) {

    }
}
