package com.zsl.zhaoqing.framework.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsl.zhaoqing.framework.R;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zsl on 2017/7/4.
 */

public class TabIndicator extends HorizontalScrollView {
    public final static String TAG = "TabIndicator";

    // ######## Tab 的模式常量 ############
    public final static int TAB_MODE_LINE = 0;// 线提醒
    public final static int TAB_MODE_TRIANGLE = 1;// 三角形提醒
    public final static int TAB_MODE_RECT = 2;// 矩形提醒

    // ######## line 模式下的样式常量 ############
    public final static int LINE_STYLE_MATCH = 0;
    public final static int LINE_STYLE_WRAP = 1;

    // ######## rect 模式下的样式常量 ############
    public final static int RECT_STYLE_FILL = 0;
    public final static int RECT_STYLE_STROKE = 1;

    // ######## triangle 模式下的样式常量
    public final static int TRIANGLE_STYLE_FILL = 0;
    public final static int TRIANGLE_STYLE_STROKE = 1;

    private LinearLayout mTabContainer = null;
    private ViewPager mViewPager = null;// 当前tab对应的ViewPager
    private TabPageListener mTabPageListener = null;// 页面的监听器

    // ################### TAB 通用的属性 ################################
    private float mTabPaddingLeft = 15; // 每个tab的padding
    private float mTabPaddingRight = 15;
    private float mTabPaddingTop = 12;
    private float mTabPaddingBottom = 12;
    private int mTabBackground = R.drawable.tab_bg_selector;
    private int mTabTextColor = Color.BLACK;
    private int mTabSelectedTextColor = Color.RED;
    private float mTabTextSize = 18;
    private boolean mTabSelectedTextBlod = false;
    private float mUnderLineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
    private int mUnderLineColor = Color.parseColor("#33000000");
    private int mTabMode = TAB_MODE_LINE;

    // ################## TAB line模式下的属性 ###########################
    private float mLineHeight = 5;
    private int mLineColor = Color.RED;
    private int mLineStyle = LINE_STYLE_WRAP;

    // ################## TAB triangle模式下的属性 #######################
    private float mTriangleHeight = 8;
    private float mTriangleWidth = 20;
    private int mTriangleColor = Color.TRANSPARENT;
    private int mTriangleStyle = TRIANGLE_STYLE_FILL;
    private float mTriangleStrokeWidth = 2;

    // ################## TAB rect模式下的属性 ###########################
    private float mRectPaddingLeft = 8;
    private float mRectPaddingTop = 8;
    private float mRectPaddingRight = 8;
    private float mRectPaddingBottom = 8;
    private int mRectColor = Color.TRANSPARENT;
    private float mRectRadius = 0;
    private int mRectStyle = RECT_STYLE_FILL;
    private int mRectStrokeColor = Color.TRANSPARENT;
    private float mRectStrokeWidth = 0;

    private Paint mPaint = new Paint();
    private Path mTrianglePath = null;
    private GradientDrawable mRectDrawable = null;

    private float mPagerOffset = 0f;
    private int mCurrentPosition = 0;

    private boolean mEnableUnderLine = true;

    /**
     * 观察者集合
     */
    private List<OnTabChangeListener> mListeners = new LinkedList<OnTabChangeListener>();

    /**
     * viewpager的监听器
     */
    private OnTabChangeListener mListener;

    private Handler mHandler = new Handler();

    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 滑动条不可见
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);
        setBackgroundColor(Color.TRANSPARENT);

        // 创建tab容器
        mTabContainer = new LinearLayout(context, attrs);
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        // mTabContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mTabContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 初始化自定义属性
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet set) {
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.TabIndicator);

        // 通用的属性获取
        mTabPaddingLeft = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingLeft, mTabPaddingLeft);
        mTabPaddingTop = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingTop, mTabPaddingTop);
        mTabPaddingRight = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingRight, mTabPaddingRight);
        mTabPaddingBottom = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingBottom, mTabPaddingBottom);

        mTabBackground = ta.getResourceId(R.styleable.TabIndicator_tiTabBackground, mTabBackground);
        mTabTextColor = ta.getColor(R.styleable.TabIndicator_tiTabTextColor, mTabTextColor);
        mTabSelectedTextColor = ta.getColor(R.styleable.TabIndicator_tiTabSelectedTextColor, mTabSelectedTextColor);
        mTabTextSize = ta.getDimensionPixelSize(R.styleable.TabIndicator_tiTabTextSize, (int) mTabTextSize);
        mTabTextSize = mTabTextSize / getResources().getDisplayMetrics().density;
        mTabSelectedTextBlod = ta.getBoolean(R.styleable.TabIndicator_tiTabSelectedTextBlod, mTabSelectedTextBlod);

        mUnderLineHeight = ta.getDimension(R.styleable.TabIndicator_tiUnderLineHeight, mUnderLineHeight);
        mUnderLineColor = ta.getColor(R.styleable.TabIndicator_tiUnderLineColor, mUnderLineColor);
        mEnableUnderLine = ta.getBoolean(R.styleable.TabIndicator_tiEnableUnderLine, false);

        mTabMode = ta.getInt(R.styleable.TabIndicator_tiTabMode, mTabMode);

        mLineHeight = ta.getDimension(R.styleable.TabIndicator_tiLineHeight, mLineHeight);
        mLineColor = ta.getColor(R.styleable.TabIndicator_tiLineColor, mLineColor);
        mLineStyle = ta.getInt(R.styleable.TabIndicator_tiLineStyle, mLineStyle);

        mTriangleHeight = ta.getDimension(R.styleable.TabIndicator_tiTriangleHeight, mTriangleHeight);
        mTriangleWidth = ta.getDimension(R.styleable.TabIndicator_tiTriangleWidth, mTriangleWidth);
        mTriangleColor = ta.getColor(R.styleable.TabIndicator_tiTriangleColor, mTriangleColor);
        mTriangleStyle = ta.getInt(R.styleable.TabIndicator_tiTriangleStyle, mTriangleStyle);
        mTriangleStrokeWidth = ta.getDimension(R.styleable.TabIndicator_tiTriangleStrokeWidth, mTriangleStrokeWidth);

        mRectPaddingLeft = ta.getDimension(R.styleable.TabIndicator_tiRectPaddingLeft, mRectPaddingLeft);
        mRectPaddingTop = ta.getDimension(R.styleable.TabIndicator_tiRectPaddingLeft, mRectPaddingTop);
        mRectPaddingRight = ta.getDimension(R.styleable.TabIndicator_tiRectPaddingLeft, mRectPaddingRight);
        mRectPaddingBottom = ta.getDimension(R.styleable.TabIndicator_tiRectPaddingLeft, mRectPaddingBottom);
        mRectColor = ta.getColor(R.styleable.TabIndicator_tiRectColor, mRectColor);
        mRectRadius = ta.getDimension(R.styleable.TabIndicator_tiRectRadius, mRectRadius);
        mRectStyle = ta.getInt(R.styleable.TabIndicator_tiRectStyle, mRectStyle);
        mRectStrokeWidth = ta.getDimension(R.styleable.TabIndicator_tiRectStrokeWidth, mRectStrokeWidth);
        mRectStrokeColor = ta.getColor(R.styleable.TabIndicator_tiRectStrokeColor, mRectStrokeColor);

        ta.recycle();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState ss = new SavedState(parcelable);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画underLine
        drawUnderLine(canvas);
        if (mTabContainer.getChildCount() == 0) {
            return;
        }
        switch (mTabMode) {
            case TAB_MODE_LINE:
                // 画line
                drawLine(canvas);
                break;
            case TAB_MODE_TRIANGLE:
                // 画triangle
                drawTriangle(canvas);
                break;
            case TAB_MODE_RECT:
                // 画rect
                drawRect(canvas);
                break;
            default:
                break;
        }

    }

    /**
     * draw line
     *
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas) {
        // 重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineColor);

        // 计算当前的left和right
        float[] clr = getCurrentLeftAndRight();
        float left = clr[0];
        float right = clr[1];

        if (mLineStyle == LINE_STYLE_WRAP) {
            left += mTabPaddingLeft;
            right -= mTabPaddingRight;
        }

        float top = getMeasuredHeight() - mLineHeight;
        float bottom = getMeasuredHeight();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * draw triangle
     *
     * @param canvas 画布
     */
    private void drawTriangle(Canvas canvas) {
        // 重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTriangleColor);

        // 计算当前的left和right
        float[] clr = getCurrentLeftAndRight();
        float left = clr[0];
        float right = clr[1];

        float x1 = (left + right) / 2f;
        float y1 = getMeasuredHeight() - mTriangleHeight;
        float x2 = x1 - mTriangleWidth / 2f;
        float y2 = getMeasuredHeight();
        float x3 = x1 + mTriangleWidth / 2f;
        float y3 = getMeasuredHeight();

        if (mTriangleStyle == TRIANGLE_STYLE_FILL) {
            drawFillTriangle(x1, y1, x2, y2, x3, y3, canvas);
        } else {
            drawStrokeTriangle(x1, y1, x2, y2, x3, y3, canvas);
        }
    }

    /**
     * draw stroke style triangle
     *
     * @param canvas 画布
     */
    private void drawStrokeTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mTriangleStrokeWidth);

        float x0 = 0;
        float y0 = getMeasuredHeight();
        float x4 = mTabContainer.getMeasuredWidth();
        float y4 = getMeasuredHeight();

        canvas.drawLine(x0, y0, x2, y2, mPaint);
        canvas.drawLine(x2, y2, x1, y1, mPaint);
        canvas.drawLine(x1, y1, x3, y3, mPaint);
        canvas.drawLine(x3, y3, x4, y4, mPaint);
    }

    /**
     * draw fill style triangle
     *
     * @param canvas 画布
     */
    private void drawFillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Canvas canvas) {
        if (mTrianglePath == null) {
            mTrianglePath = new Path();
        }

        mTrianglePath.reset();
        mTrianglePath.moveTo(x1, y1);
        mTrianglePath.lineTo(x2, y2);
        mTrianglePath.lineTo(x3, y3);
        mTrianglePath.lineTo(x1, y1);
        mTrianglePath.close();

        canvas.drawPath(mTrianglePath, mPaint);
    }

    /**
     * draw rect
     *
     * @param canvas 画布
     */
    private void drawRect(Canvas canvas) {
        // 计算当前的left和right
        float[] clr = getCurrentLeftAndRight();
        float left = clr[0] + mRectPaddingLeft;
        float right = clr[1] - mRectPaddingRight;

        float underLine = mEnableUnderLine ? mUnderLineHeight : 0;
        float top = mRectPaddingTop + underLine;
        float bottom = getMeasuredHeight() - mRectPaddingBottom;

        if (mRectDrawable == null) {
            mRectDrawable = new GradientDrawable();
        }

        mRectDrawable.setShape(GradientDrawable.RECTANGLE);
        mRectDrawable.setColor(mRectColor);
        mRectDrawable.setCornerRadius(mRectRadius);

        if (mRectStyle == RECT_STYLE_STROKE) {
            mRectDrawable.setStroke((int) mRectStrokeWidth, mRectStrokeColor);
        } else {
            mRectDrawable.setStroke((int) mRectStrokeWidth, Color.TRANSPARENT);
        }
        mRectDrawable.setBounds((int) left, (int) top, (int) right, (int) bottom);

        mRectDrawable.draw(canvas);
    }

    /**
     * 计算当前滑动的left和right
     *
     * @return 获得动态的left和right，结果为float[],0为left，1为right
     */
    private float[] getCurrentLeftAndRight() {
        // 获得当前的tab的left和right
        if (mCurrentPosition > mTabContainer.getChildCount() - 1) {
            mCurrentPosition = 0;
        }
        View currentTab = mTabContainer.getChildAt(mCurrentPosition);
        float left = currentTab.getLeft();
        float right = currentTab.getRight();

        if (mPagerOffset > 0f && mCurrentPosition < mViewPager.getAdapter().getCount() - 1) {
            View nextTab = mTabContainer.getChildAt(mCurrentPosition + 1);
            if (mTabMode != TAB_MODE_TRIANGLE) {
                // 获得下一个的tab的left和right
                float nextLeft = nextTab.getLeft();
                float nextRight = nextTab.getRight();
                // 计算偏移后的left和right
                if (mPagerOffset <= 0.5) {
                    right = right + 2 * mPagerOffset * (nextRight - right);
                } else {
                    left = left + 2 * (mPagerOffset - 0.5f) * (nextLeft - left);
                    right = nextRight;
                }
            } else {
                // 获得下一个的tab的left和right
                float nextLeft = nextTab.getLeft();
                float nextRight = nextTab.getRight();
                // 计算偏移后的left和right
                left = nextLeft * mPagerOffset + (1 - mPagerOffset) * left;
                right = nextRight * mPagerOffset + (1 - mPagerOffset) * right;
            }
        }
        return new float[]{left, right};
    }

    /**
     * draw underline
     *
     * @param canvas 画布
     */
    private void drawUnderLine(Canvas canvas) {
        if (!mEnableUnderLine) return;

        if (mTabMode == TAB_MODE_TRIANGLE && mTriangleStyle == TRIANGLE_STYLE_STROKE) {
            return;
        }

        // 重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mUnderLineColor);

        float left = 0;
        float top = getMeasuredHeight() - mUnderLineHeight;
        float right = mTabContainer.getMeasuredWidth();
        float bottom = getMeasuredHeight();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 设置ViewPager
     *
     * @param pager viewpager
     */
    public void setViewPager(ViewPager pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager还没有调用setAdapter()来设置数据");
        }

        // 保存实例
        this.mViewPager = pager;
        // 设置监听
        if (mTabPageListener == null) {
            mTabPageListener = new TabPageListener();
        }
        this.mViewPager.addOnPageChangeListener(mTabPageListener);

        // 更新Tab的显示
        updateTabs();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int viewX = mTabContainer.getChildAt(mCurrentPosition).getLeft() + mTabContainer.getChildAt(mCurrentPosition).getWidth() - getScrollX();
                if (viewX > getWidth()) {
                    scrollBy((viewX - getWidth()), 0);
                }
            }
        }, 300);
    }

    /**
     * 设置viewpager的滑动监听
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnTabChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * add listener
     *
     * @param listener
     */
    public void addOnPageChangeListener(OnTabChangeListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    /**
     * remove listener
     *
     * @param listener
     */
    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListeners.remove(listener);
    }

    private void notifyOnPageSelected(int position) {
        ListIterator<OnTabChangeListener> iterator = mListeners.listIterator();
        while (iterator.hasNext()) {
            ViewPager.OnPageChangeListener next = iterator.next();

            if (next != null) {
                next.onPageSelected(position);
            }
        }

        // 通知listener
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    private void notifyOnPageReSelected(int position) {
        ListIterator<OnTabChangeListener> iterator = mListeners.listIterator();
        while (iterator.hasNext()) {
            OnTabChangeListener next = iterator.next();

            if (next != null) {
                next.onPageReSelected(position);
            }
        }

        // 通知listener
        if (mListener != null) {
            mListener.onPageReSelected(position);
        }
    }

    private void notifyOnPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ListIterator<OnTabChangeListener> iterator = mListeners.listIterator();
        while (iterator.hasNext()) {
            ViewPager.OnPageChangeListener next = iterator.next();

            if (next != null) {
                next.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        // 通知listener
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void notifyOnPageScrollStateChanged(int state) {
        ListIterator<OnTabChangeListener> iterator = mListeners.listIterator();
        while (iterator.hasNext()) {
            ViewPager.OnPageChangeListener next = iterator.next();

            if (next != null) {
                next.onPageScrollStateChanged(state);
            }
        }

        // 通知listener
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * update tabs
     */
    void updateTabs() {
        // 清空
        mTabContainer.removeAllViews();

        PagerAdapter adapter = mViewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            CharSequence title = adapter.getPageTitle(i);
            addTab(title, i);
        }

        invalidate();
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                notifyOnPageSelected(0);
//                int count = mTabContainer.getChildCount();
//                if (count > 0) {
//                    ((TextView) mTabContainer.getChildAt(0)).setTextColor(mTabSelectedTextColor);
//                }
//                getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
    }

    /**
     * 添加tab
     *
     * @param title tab显示的title
     * @param index tab的index
     */
    private void addTab(CharSequence title, final int index) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.getPaint().setFakeBoldText(false);
        tab.setPadding((int) mTabPaddingLeft, (int) mTabPaddingTop, (int) mTabPaddingRight, (int) mTabPaddingBottom);
//        tab.setBackgroundResource(mTabBackground);
        if (index == mCurrentPosition) {
            tab.getPaint().setFakeBoldText(true);
        } else {
            tab.getPaint().setFakeBoldText(false);
        }
        if (index == mCurrentPosition) {
            tab.setTextColor(mTabSelectedTextColor);
        } else {
            tab.setTextColor(mTabTextColor);
        }
        tab.setTextSize(mTabTextSize);
        // tab.setSelected(index == 0);//设置默认
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == mViewPager.getCurrentItem() && mTabPageListener != null) {
                    mTabPageListener.onPageReSelected(index);
                    return;
                }
                mViewPager.setCurrentItem(index, false);
            }
        });

        mTabContainer.addView(tab, index, getTabLayoutParam());
    }

    private LinearLayout.LayoutParams getTabLayoutParam() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.bottomMargin = (int) (mUnderLineHeight + 0.5f);
        return params;
    }

    private void scrollTabs() {
        //向右滑动
        View nextView = mTabContainer.getChildAt(mCurrentPosition + 1);
        int nextViewRight = 0;
        int dst = 0;
        if (nextView != null) {
            nextViewRight = nextView.getRight();
            if (nextViewRight > getMeasuredWidth()) {
                dst = nextViewRight - getScrollX() - getMeasuredWidth();
                if (dst > 0) {
                    scrollBy((int) (mPagerOffset * dst), 0);
                }
            }
        }

        //向左滑动
        View view = mTabContainer.getChildAt(mCurrentPosition);
        if (view == null) return;
        int endX = (int) (view.getMeasuredWidth() * mPagerOffset + view.getLeft() + 0.5f);
        if (endX <= getScrollX()) {
            scrollTo(endX, 0);
        }
    }

    /**
     * @return Tab padding left
     */
    public float getTabPaddingLeft() {
        return mTabPaddingLeft;
    }

    /**
     * @return Tab padding top
     */
    public float getTabPaddingTop() {
        return mTabPaddingTop;
    }

    /**
     * @return Tab padding right
     */
    public float getTabPaddingRight() {
        return mTabPaddingRight;
    }

    /**
     * @return Tab padding Bottom
     */
    public float getTabPaddingBottom() {
        return mTabPaddingBottom;
    }

    /**
     * set tab padding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTabPadding(int left, int top, int right, int bottom) {
        if (left >= 0) {
            this.mTabPaddingLeft = left;
        }
        if (top >= 0) {
            this.mTabPaddingTop = top;
        }
        if (right >= 0) {
            this.mTabPaddingRight = right;
        }
        if (bottom >= 0) {
            this.mTabPaddingBottom = bottom;
        }
    }

    /**
     * set tab background selector
     *
     * @param resId
     */
    public void setTabBackground(int resId) {
        this.mTabBackground = resId;
        invalidate();
    }

    /**
     * set tab text color selector
     *
     * @param resId
     */
    public void setTabTextColor(int resId) {
        this.mTabTextColor = resId;
    }

    public void setTabSelectedColor(int resId) {
        this.mTabSelectedTextColor = resId;
    }

    /**
     * @return tab text size
     */
    public float getTabTextSize() {
        return mTabTextSize;
    }

    /**
     * set tab text size
     *
     * @param size
     */
    public void setTabTextSize(float size) {
        if (size < 0) return;
        this.mTabTextSize = size;
        invalidate();
    }


    /**
     * set tab text blod
     *
     * @param blod
     */
    public void setTabTextBlod(boolean blod) {
        this.mTabSelectedTextBlod = blod;
        invalidate();
    }

    /**
     * @return get under line height
     */
    // public float getUnderLineHeight()
    // {
    // return mUnderLineHeight;
    // }

    /**
     * set under line height
     *
     * @param underLineHeight
     */
    // public void setUnderLineHeight(float underLineHeight)
    // {
    // this.mUnderLineHeight = underLineHeight;
    // invalidate();
    // }

    /**
     * @return get under line color
     */
    public int getUnderLineColor() {
        return mUnderLineColor;
    }

    /**
     * set under line color
     *
     * @param underLineColor
     */
    public void setUnderLineColor(int underLineColor) {
        this.mUnderLineColor = underLineColor;
        invalidate();
    }

    /**
     * @return get tab mode
     */
    public int getTabMode() {
        return mTabMode;
    }

    /**
     * set tab mode
     *
     * @param tabMode
     */
    public void setTabMode(int tabMode) {
        if (tabMode != TAB_MODE_LINE && tabMode != TAB_MODE_RECT && tabMode != TAB_MODE_TRIANGLE) {
            tabMode = TAB_MODE_LINE;
        }
        this.mTabMode = tabMode;
        invalidate();
    }

    /**
     * @return get line height
     */
    public float getLineHeight() {
        return mLineHeight;
    }

    /**
     * set line height
     *
     * @param lineHeight
     */
    public void setLineHeight(float lineHeight) {
        this.mLineHeight = lineHeight;
        invalidate();
    }

    /**
     * @return line color
     */
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * set line color
     *
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        invalidate();
    }

    /**
     * @return line style
     * @see {@link TabIndicator#LINE_STYLE_MATCH}
     * @see {@link TabIndicator#LINE_STYLE_WRAP}
     */
    public int getLineStyle() {
        return mLineStyle;
    }

    /**
     * set line style
     *
     * @param lineStyle
     * @see {@link TabIndicator#LINE_STYLE_MATCH}
     * @see {@link TabIndicator#LINE_STYLE_WRAP}
     */
    public void setLineStyle(int lineStyle) {
        if (lineStyle == LINE_STYLE_MATCH) {
            this.mLineStyle = lineStyle;
        } else {
            this.mLineStyle = LINE_STYLE_WRAP;
        }
        invalidate();
    }

    /**
     * @return get triangle height
     */
    public float getTriangleHeight() {
        return mTriangleHeight;
    }

    /**
     * set triangle height
     *
     * @param triangleHeight
     */
    public void setTriangleHeight(float triangleHeight) {
        this.mTriangleHeight = triangleHeight;
        invalidate();
    }

    /**
     * @return get triangle width
     */
    public float getTriangleWidth() {
        return mTriangleWidth;
    }

    /**
     * set triangle width
     *
     * @param triangleWidth
     */
    public void setTriangleWidth(float triangleWidth) {
        this.mTriangleWidth = triangleWidth;
        invalidate();
    }

    /**
     * @return get triangle color
     */
    public int getTriangleColor() {
        return mTriangleColor;
    }

    /**
     * set triangle color
     *
     * @param triangleColor
     */
    public void setTriangleColor(int triangleColor) {
        this.mTriangleColor = triangleColor;
        invalidate();
    }

    /**
     * @return
     * @see {@link TabIndicator#TRIANGLE_STYLE_FILL}
     * @see {@link TabIndicator#TRIANGLE_STYLE_STROKE}
     */
    public int getTriangleStyle() {
        return mTriangleStyle;
    }

    /**
     * set triangle style
     *
     * @param triangleStyle
     */
    public void setTriangleStyle(int triangleStyle) {
        if (triangleStyle == TRIANGLE_STYLE_FILL) {
            this.mTriangleStyle = triangleStyle;
        } else {
            this.mTriangleStyle = TRIANGLE_STYLE_STROKE;
        }
        invalidate();
    }

    /**
     * @return rect padding left
     */
    public float getRectPaddingLeft() {
        return mRectPaddingLeft;
    }

    /**
     * @return rect padding top
     */
    public float getRectPaddingTop() {
        return mRectPaddingTop;
    }

    /**
     * @return rect padding right
     */
    public float getRectPaddingRight() {
        return mRectPaddingRight;
    }

    /**
     * @return rect padding bottom
     */
    public float getRectPaddingBottom() {
        return mRectPaddingBottom;
    }

    /**
     * set rect padding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setRectPadding(float left, float top, float right, float bottom) {
        if (left >= 0) {
            this.mRectPaddingLeft = left;
        }
        if (top >= 0) {
            this.mRectPaddingTop = top;
        }
        if (right >= 0) {
            this.mRectPaddingRight = right;
        }
        if (bottom >= 0) {
            this.mRectPaddingBottom = bottom;
        }
    }

    /**
     * @return rect color
     */
    public int getRectColor() {
        return mRectColor;
    }

    /**
     * set rect color
     *
     * @param color
     */
    public void setRectColor(int color) {
        this.mRectColor = color;
        invalidate();
    }

    /**
     * @return rect radius
     */
    public float getRectRadius() {
        return mRectRadius;
    }

    /**
     * set rect radius
     *
     * @param radius
     */
    public void setRectRadius(float radius) {
        if (radius < 0) return;
        this.mRectRadius = radius;
        invalidate();
    }

    /**
     * @return rect style
     */
    public int getRectStyle() {
        return this.mRectStyle;
    }

    /**
     * set rect style
     *
     * @param style
     */
    public void setRectStyle(int style) {
        if (style == RECT_STYLE_FILL) {
            this.mRectStyle = style;
        } else {
            this.mRectStyle = RECT_STYLE_STROKE;
        }
        invalidate();
    }

    /**
     * @return rect stroke color
     */
    public int getRectStrokeColor() {
        return mRectStrokeColor;
    }

    /**
     * set rect stroke color
     *
     * @param color
     */
    public void setRectStrokeColor(int color) {
        this.mRectStrokeColor = color;
        invalidate();
    }

    /**
     * @return rect stroke width
     */
    public float getRectStrokeWidth() {
        return mRectStrokeWidth;
    }

    /**
     * set rect stroke width
     *
     * @param width
     */
    public void setRectStrokeWidth(float width) {
        if (width < 0) return;
        this.mRectStrokeWidth = width;
        invalidate();
    }

    /**
     * @return triangle stroke width
     */
    public float getTriangleStrokeWidth() {
        return mTriangleStrokeWidth;
    }

    public void enableUnderLine(boolean enable) {
        this.mEnableUnderLine = enable;
        invalidate();
    }

    /**
     * set triangle stroke width
     *
     * @param triangleStrokeWidth
     */
    public void setTriangleStrokeWidth(float triangleStrokeWidth) {
        this.mTriangleStrokeWidth = triangleStrokeWidth;
    }

    public interface OnTabChangeListener extends ViewPager.OnPageChangeListener {
        void onPageReSelected(int position);
    }

    /**
     * Page 改变的监听器
     */
    private class TabPageListener implements OnTabChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 存储position,offset
            mCurrentPosition = position;
            mPagerOffset = positionOffset;
            // 滚动
            scrollTabs();

            // 触发绘制
            invalidate();

            // notify
            notifyOnPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
//            mCurrentPosition = position;
            int count = mTabContainer.getChildCount();
            for (int i = 0; i < count; i++) {
                TextView view = (TextView) mTabContainer.getChildAt(i);
                if (i == position) {
                    view.setTextColor(mTabSelectedTextColor);
                    if (mTabSelectedTextBlod) {
                        view.getPaint().setFakeBoldText(true);
                    } else {
                        view.getPaint().setFakeBoldText(false);
                    }
                    int viewX = view.getLeft() + view.getWidth() - getScrollX();
                    if (viewX > getWidth()) {
                        scrollBy((viewX - getWidth()), 0);
                    }
                } else {
                    view.setTextColor(mTabTextColor);
                    view.getPaint().setFakeBoldText(false);
                }
            }

            // notify
            notifyOnPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // notify
            notifyOnPageScrollStateChanged(state);
        }

        @Override
        public void onPageReSelected(int position) {
            notifyOnPageReSelected(position);
        }
    }

    private static class SavedState extends BaseSavedState {

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
    }
}
