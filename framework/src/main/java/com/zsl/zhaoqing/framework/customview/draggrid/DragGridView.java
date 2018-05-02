package com.zsl.zhaoqing.framework.customview.draggrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * Created by zsl on 2017/11/20.
 */

public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {

    private static final String TAG = "DragGridView";
    private WindowManager mWindowManager;

    private static final int MODE_DRAG = 1;
    private static final int MODE_NORMAL = 2;

    private int mode = MODE_NORMAL;
    private View view;
    private ImageView dragView;
    // 要移动的item原先位置
    private int position;

    private int tempPosition;

    private WindowManager.LayoutParams layoutParams;
    // view的x差值
    private float mX;
    // view的y差值
    private float mY;
    // 手指按下时的x坐标(相对于整个屏幕)
    private float mWindowX;
    // 手指按下时的y坐标(相对于整个屏幕)
    private float mWindowY;
    // 手指按下时的y坐标(相对于View)
    private float mDownX;
    private float mDownY;
    private float mStatusHeight = 0;
    private float mScale = 1.1f;

    private OnItemLongPressListener mListener;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStatusHeight = getStatusHeight();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (getAdapter() instanceof DragGridAdapter){
//            DragGridAdapter adapter = (DragGridAdapter) getAdapter();
//            if (!adapter.getEditStatus()) {
//                return super.onInterceptTouchEvent(ev);
//            }
//        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWindowX = ev.getRawX();
                mWindowY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode == MODE_DRAG) {
            return false;
        }
        if (getAdapter() instanceof DragGridAdapter){
            DragGridAdapter adapter = (DragGridAdapter) getAdapter();
//            if (!adapter.getEditStatus()) {
//                return false;
//            }
//            if (!adapter.canItemDrag(position)) {
//                return false;
//            }
            if (mListener != null && mListener.onItemLongPress(position)){
                return true;
            }
        }
//        if (getAdapter() instanceof DragGridAdapter){
//            DragGridAdapter adapter = (DragGridAdapter) getAdapter();
//            if (!adapter.canItemDrag(position)) {
//                return true;
//            }
//            adapter.setEditStatus(true);
//        }
//        this.view = view;
//        this.position = position;
//        this.tempPosition = position;
//        mX = view.getWidth() / 2;
//        mY = view.getHeight() / 2;
////        mX = mWindowX - view.getLeft() - this.getLeft();
////        mY = mWindowY - view.getTop() - this.getTop()- mStatusHeight;
//        // 如果是Android 6.0 要动态申请权限
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (Settings.canDrawOverlays(getContext())) {
//                initWindow();
//            } else {
//                // 跳转到悬浮窗权限管理界面
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
//                getContext().startActivity(intent);
//            }
//        } else {
//            // 如果小于Android 6.0 则直接执行
//            initWindow();
//        }
        return true;
    }

    public void setOnItemLongPressListener(OnItemLongPressListener listener){
        this.mListener = listener;
    }

    /**
     * 初始化window
     */
    private void initWindow() {
        if (dragView == null) {
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(true);
            Bitmap tmp = Bitmap.createBitmap(view.getDrawingCache());
            dragView = new ImageView(getContext());
            dragView.setImageBitmap(tmp);
        }
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.width = (int) (view.getWidth() * mScale);
            layoutParams.height = (int) (view.getHeight() * mScale);
//            layoutParams.x = (int) mWindowX;  //悬浮窗X的位置
//            layoutParams.y = (int) mWindowY;  //悬浮窗Y的位置
            layoutParams.x = (int) (mWindowX - mX);
            layoutParams.y = (int) (mWindowY - mY);
            view.setVisibility(INVISIBLE);
        }

        mWindowManager.addView(dragView, layoutParams);
        mode = MODE_DRAG;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (getAdapter() instanceof DragGridAdapter){
//            DragGridAdapter adapter = (DragGridAdapter) getAdapter();
//            if (!adapter.getEditStatus()) {
//                return super.onTouchEvent(ev);
//            }
//        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                DragGridAdapter adapter = (DragGridAdapter) getAdapter();
                if (mode == MODE_NORMAL && adapter.getEditStatus()){
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    if (Math.abs(mDownX - x) < ViewConfiguration.get(getContext()).getScaledTouchSlop() &&
                            Math.abs(mDownY - y) < ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                        break;
                    }
                    this.position = pointToPosition(x, y);
                    if (this.position != -1){
                        this.view = getChildAt(this.position);
                        this.tempPosition = this.position;
                        if (getAdapter() instanceof DragGridAdapter){
                            if (!adapter.canItemDrag(this.position)) {
                                return false;
                            }
                            mX = view.getWidth() / 2;
                            mY = view.getHeight() / 2;
//                            if (Build.VERSION.SDK_INT >= 23) {
//                                if (Settings.canDrawOverlays(getContext())) {
//                                    initWindow();
//                                } else {
//                                    // 跳转到悬浮窗权限管理界面
//                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
//                                    getContext().startActivity(intent);
//                                }
//                            } else {
//                                // 如果小于Android 6.0 则直接执行
                                initWindow();
//                            }
                        }
                    }

                }
                if (mode == MODE_DRAG) {
                    updateWindow(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == MODE_DRAG) {
                    closeWindow(ev.getX(), ev.getY());
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isMove(){
        if (mode == MODE_DRAG){
            return true;
        }
        return false;
    }

    /**
     * 触摸移动时，window更新
     *
     * @param ev
     */
    private void updateWindow(MotionEvent ev) {
        if (mode == MODE_DRAG) {
            float x = ev.getRawX() - mX;
            float y = ev.getRawY() - mY;
            if (layoutParams != null) {
                layoutParams.x = (int) x;
                layoutParams.y = (int) y;
                mWindowManager.updateViewLayout(dragView, layoutParams);
            }
            float mx = ev.getX();
            float my = ev.getY();
            int dropPosition = pointToPosition((int) mx, (int) my);
            if (dropPosition == tempPosition || dropPosition == GridView.INVALID_POSITION) {
                return;
            }
            if (getAdapter() instanceof DragGridAdapter){
                DragGridAdapter adapter = (DragGridAdapter) getAdapter();
                if (!adapter.canItemDrag(dropPosition)) {
                    return;
                }
            }
            itemMove(dropPosition);
        }
    }

    /**
     * 判断item移动
     *
     * @param dropPosition
     */
    private void itemMove(int dropPosition) {
        TranslateAnimation translateAnimation;
        if (dropPosition < tempPosition) {
            for (int i = dropPosition; i < tempPosition; i++) {
                View view = getChildAt(i);
                View nextView = getChildAt(i + 1);
                float xValue = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == tempPosition - 1) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        } else {
            for (int i = tempPosition + 1; i <= dropPosition; i++) {
                View view = getChildAt(i);
                View prevView = getChildAt(i - 1);
                float xValue = (prevView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (prevView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == dropPosition) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        }
        tempPosition = dropPosition;
    }

    /**
     * 动画监听器
     */
    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // 在动画完成时将adapter里的数据交换位置
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, true);
            }
            position = tempPosition;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 关闭window
     *
     * @param x
     * @param y
     */
    private void closeWindow(float x, float y) {
        if (dragView != null) {
            mWindowManager.removeView(dragView);
            dragView = null;
            layoutParams = null;
        }
        itemDrop();
        mode = MODE_NORMAL;
    }

    /**
     * 手指抬起时，item下落
     */
    private void itemDrop() {
        if (tempPosition == position || tempPosition == GridView.INVALID_POSITION) {
            getChildAt(position).setVisibility(VISIBLE);
        } else {
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, false);
            }
        }
    }

//    private void addAlignAuto(int position){
//        TranslateAnimation translateAnimation;
//        ListAdapter adapter = getAdapter();
//        for (int i = adapter.getCount() - 2; i >= position; i--) {
//            View view = getChildAt(i);
//            View nextView = getChildAt(i + 1);
//            float xValue = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
//            float yValue = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
//            translateAnimation =
//                    new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
//            translateAnimation.setInterpolator(new LinearInterpolator());
//            translateAnimation.setFillAfter(true);
//            translateAnimation.setDuration(300);
//            if (i == tempPosition - 1) {
//                translateAnimation.setAnimationListener(animationListener);
//            }
//            view.startAnimation(translateAnimation);
//        }
//    }

    /**
     * 获取状态栏高度——方法1
     * */
    private int getStatusHeight(){
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public interface OnItemLongPressListener{
        boolean onItemLongPress(int position);
    }

}
