package com.zsl.zhaoqing.hotspot.base.View.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.zsl.zhaoqing.hotspot.base.presenter.IPresenter;


public abstract class ToolBarActivity<P extends IPresenter> extends StatusBarActivity<P>{

	private View mToolBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		mToolBar = onCreateToolBar(LayoutInflater.from(this));
		super.onCreate(savedInstanceState);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		return null;
	}

	protected View onCreateToolBar(LayoutInflater inflater){
		return null;
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		setContentView(view);
	}

	@Override
	public void setContentView(View view) {
		setContentView(view, null);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		if (mToolBar != null){
			LinearLayout container = new LinearLayout(this);
			container.setOrientation(LinearLayout.VERTICAL);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			container.addView(mToolBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					DensityUtil.dip2px(this, 50)));
			if (params != null){
				container.addView(view, params);
			}else {
				container.addView(view);
			}
			super.setContentView(container,lp);
		}else {
			if (params != null){
				super.setContentView(view, params);
			}else {
				super.setContentView(view);
			}
		}
	}
}
