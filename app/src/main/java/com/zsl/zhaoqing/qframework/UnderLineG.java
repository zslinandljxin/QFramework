package com.zsl.zhaoqing.qframework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.LineBackgroundSpan;
import android.text.style.UpdateAppearance;

/**
 * Created by Administrator on 2017/12/23.
 */

public class UnderLineG extends CharacterStyle implements LineBackgroundSpan, UpdateAppearance {

    private Paint mPaint;

    public UnderLineG() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(Color.BLUE);
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {

        int widthone = 0;
        if (0 == lnum){
            widthone = (int) p.measureText(text, 3, end);
            int st = (int) p.measureText(text, 0, 2);
            c.drawLine(left + st, bottom, left+ st + widthone, bottom, mPaint );
        }else {
            widthone = (int) p.measureText(text, start, end);
            c.drawLine(left, bottom, left + widthone, bottom, mPaint );
        }


    }
}
