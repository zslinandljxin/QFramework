package com.zsl.zhaoqing.qframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2017/12/12.
 */

public class UnderLineText extends AppCompatTextView {

    private Paint mLinePaint;
    private Paint mTextPaint;
    private int mLineHeight;
    private int mLineWidth;
    private float mPerCharWidth;
    private int mCharCount;
    private int mCountCharInLine;
    private float mTotalWidth;
    private int mLines;
    private int mUnderBaseLine;
    private float mDensity;

    private List<Integer> mPos;

    public UnderLineText(Context context) {
        super(context);
        init();
    }

    public UnderLineText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderLineText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mTextPaint = getPaint();
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.parseColor("#f98422"));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1 * getResources().getDisplayMetrics().density);
        mLineHeight = getLineHeight();
        mDensity = getResources().getDisplayMetrics().density;
        mPos = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLineWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTotalWidth = mTextPaint.measureText(getText().toString());
        mCharCount = getText().length();
        mPerCharWidth = mTotalWidth / mCharCount;
        mCountCharInLine = (int) (mLineWidth / mPerCharWidth);
        mLines = (int) (mTotalWidth / mLineWidth);
        mUnderBaseLine = (int) (getBaseline() + mDensity * 3);
        try {
            int start = 0;
            for (int i = 1; i <= mCharCount;){
                float width = mTextPaint.measureText(getText().toString(), start, i);
                if (width > mLineWidth){
                    mPos.add(i - 1);
                    start = i - 1;
                    continue;
                }
                if (i == mCharCount){
                    mPos.add(mCharCount);
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawUnderLine(canvas);
        drawUnderLine2(canvas);
    }

    private void drawUnderLine2(Canvas canvas){
        try {
            for (int i = 0; i <= mPos.size() - 1; i++){
                if (i == 0){
                    int start =(int) mTextPaint.measureText(getText().toString(), 0, 2);
                    int ws = (int) mTextPaint.measureText(getText().toString(), 2, mPos.get(0));
                    canvas.drawLine(getPaddingLeft() + start, mUnderBaseLine,
                            getPaddingLeft() + start + ws, mUnderBaseLine, mLinePaint);
                    continue;
                }
                int wso = (int) mTextPaint.measureText(getText(), mPos.get(i - 1), mPos.get(i));
                canvas.drawLine(getPaddingLeft(), mUnderBaseLine + mLineHeight * i,
                        getPaddingLeft() + wso, mUnderBaseLine + mLineHeight * i, mLinePaint);
            }
        }catch (Exception e){
            Log.i("underline",mPos.get(0)+"");
            e.printStackTrace();
        }

    }

    private void drawUnderLine(Canvas canvas){
        int i = 0;
        for (i = 0; i < mLines; i++){
            if (i == 0){
                canvas.drawLine(getPaddingLeft() + mPerCharWidth, mUnderBaseLine,
                        getPaddingLeft() + mCountCharInLine * mPerCharWidth, mUnderBaseLine, mLinePaint);
                continue;
            }
            canvas.drawLine(getPaddingLeft(), mUnderBaseLine + mLineHeight * i,
                        getPaddingLeft() + mCountCharInLine * mPerCharWidth, mUnderBaseLine + mLineHeight * i, mLinePaint);
        }
        int leaveChar = mCharCount - i * mCountCharInLine;
        canvas.drawLine(getPaddingLeft(), mUnderBaseLine + mLineHeight * i,
                getPaddingLeft() + leaveChar * mPerCharWidth, mUnderBaseLine + mLineHeight * i, mLinePaint);
    }
}
