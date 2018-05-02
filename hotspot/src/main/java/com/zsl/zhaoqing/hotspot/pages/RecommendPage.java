package com.zsl.zhaoqing.hotspot.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.pages.meta.Page;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.Channel;

/**
 * Created by zsl on 2017/11/13.
 */

public class RecommendPage extends Page {

    private int mIndex;
    private Channel mChannel;

    public RecommendPage(int position, Channel channel){
        this.mIndex = position;
        this.mChannel = channel;
    }

    @Override
    public void onCreatePager(Context context) {
        super.onCreatePager(context);
        setPagerView(R.layout.recommend_page);
    }

    @Override
    public void onDestroyPager() {
        super.onDestroyPager();
    }
}
