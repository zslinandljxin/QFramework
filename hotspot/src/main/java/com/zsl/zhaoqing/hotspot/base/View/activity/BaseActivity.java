package com.zsl.zhaoqing.hotspot.base.View.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.zsl.zhaoqing.hotspot.base.presenter.IPresenter;


public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity {

	private View mView;
	protected P mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = initView(LayoutInflater.from(this));
		setContentView(mView);
		mPresenter = onAttachPresenter();
		addListener();
		if (mPresenter != null) {
			mPresenter.onCreate(savedInstanceState);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mPresenter != null){
			mPresenter.onStart();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (mPresenter != null){
			mPresenter.onRestart();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPresenter != null){
			mPresenter.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mPresenter != null){
			mPresenter.onPause();
		}
	}


	@Override
	protected void onStop() {
		super.onStop();
		if (mPresenter != null){
			mPresenter.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mView != null){
			mView = null;
		}
		if (mPresenter != null){
			mPresenter.onDestroy();
			mPresenter = null;
		}
	}

	public abstract View initView(LayoutInflater inflater);

	public abstract P onAttachPresenter();

	public abstract void addListener();

}
