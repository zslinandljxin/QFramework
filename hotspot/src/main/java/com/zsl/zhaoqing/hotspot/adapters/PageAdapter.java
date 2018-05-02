package com.zsl.zhaoqing.hotspot.adapters;

import android.graphics.pdf.PdfDocument;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zsl.zhaoqing.hotspot.pages.PageFactory;
import com.zsl.zhaoqing.hotspot.pages.meta.Page;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zsl on 2017/11/13.
 */

public class PageAdapter extends PagerAdapter {

    private List<Channel> mData;
    private Map<String, Page> mCachePage;

    public PageAdapter(List<Channel> data) {
       this.mData = data;
        mCachePage = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Page page = mCachePage.get(generateKey(position));
        if (page == null) {
            page = PageFactory.createPage(0, mData.get(position));
            mCachePage.put(generateKey(position), page);
            page.onCreatePager(container.getContext());
        }
        container.addView(page.getContentView());
        return page.getContentView();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    private String generateKey(int position){
        return String.valueOf(position);
    }
}
