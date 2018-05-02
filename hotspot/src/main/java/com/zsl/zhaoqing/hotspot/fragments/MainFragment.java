package com.aspire.redu.hot.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zsl.zhaoqing.hotspot.presenters.MainPresenter;
import com.zsl.zhaoqing.hotspot.R;
import com.zsl.zhaoqing.hotspot.adapters.PageAdapter;
import com.zsl.zhaoqing.hotspot.base.View.BaseFragment;
import com.zsl.zhaoqing.hotspot.customui.TabIndicator;

import java.nio.channels.Channel;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public class MainFragment extends BaseFragment<MainPresenter> implements MainContract {

    private TabLayout mTabIndicator;
    private ViewPager mViewPager;
    private PageAdapter mAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.main_fragment, null);
        mTabIndicator = view.findViewById(R.id.tab_indicator);
        mViewPager = view.findViewById(R.id.content_view);
        return view;
    }

    @Override
    public MainPresenter onAttachPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void addListener() {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTabIndicator(List<Channel> channels) {
        if (mAdapter == null){
            mAdapter = new PageAdapter(channels);
            mViewPager.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        mTabIndicator.setupWithViewPager(mViewPager);
    }
}
