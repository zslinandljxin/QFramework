package com.zsl.zhaoqing.hotspot.pages;

import android.content.Context;

import com.zsl.zhaoqing.hotspot.pages.meta.Page;

import java.nio.channels.Channel;

/**
 * Created by zsl on 2017/11/13.
 */

public class PageFactory {

    public static Page createPage(int position, Channel channel){
        return new RecommendPage(position, channel);
    }
}
