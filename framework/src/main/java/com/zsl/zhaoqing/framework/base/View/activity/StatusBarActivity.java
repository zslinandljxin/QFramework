package com.zsl.zhaoqing.framework.base.View.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zsl.zhaoqing.framework.base.presenter.IPresenter;
import com.zsl.zhaoqing.framework.utils.StatusBarUtils;


public abstract class StatusBarActivity<T extends IPresenter> extends BaseActivity<T>{
	private View mStatusBarView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 提供remove方法，在不需要状态栏时可以移除
	 */
	protected void removeStatusView() {
		ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
		if (mStatusBarView != null) {
			decorView.removeView(mStatusBarView);
			mStatusBarView = null;
			// 设置根布局的参数
			ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rootView.getLayoutParams();
			lp.topMargin = 0;
		}
		if (StatusBarUtils.isFlyme()){
			StatusBarUtils.setFlymeStatusBarTextColor(this, 2);
		}else if( StatusBarUtils.isMIUIV6()){
			StatusBarUtils.setMIUIStatusBarTextColor(this, 2);
		}
	}

	/**
	 * 设置状态栏颜色
	 * This method should be called after setContentView.
	 * @param color
	 *            状态栏颜色值
	 */
	protected void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
			if (mStatusBarView != null) {
				decorView.removeView(mStatusBarView);
			}
			mStatusBarView = createStatusView(color);
			decorView.addView(mStatusBarView);
			// 设置根布局的参数
			ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
//            View view = rootView.getChildAt(0);
//            if (view != null){
//                view.setFitsSystemWindows(true);
//            }
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rootView.getLayoutParams();
			lp.topMargin = getStatusBarHeight();
			if (StatusBarUtils.isMIUIV6() || StatusBarUtils.isFlyme()) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // && !withoutUseStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
					getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
				}
			}
		}
	}

	/**
	 * 生成一个和状态栏大小相同的矩形条
	 *
	 * @param color
	 *            状态栏颜色值
	 * @return 状态栏矩形条
	 */
	private View createStatusView(int color) {
		// 获得状态栏高度
		int statusBarHeight = getStatusBarHeight();
		// 绘制一个和状态栏一样高的矩形
		View statusView = new View(this);
		LinearLayout.LayoutParams params =
				new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
		statusView.setLayoutParams(params);
		statusView.setBackgroundColor(color);
		return statusView;
	}

	private int getStatusBarHeight() {
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		return getResources().getDimensionPixelSize(resourceId);
	}
}
