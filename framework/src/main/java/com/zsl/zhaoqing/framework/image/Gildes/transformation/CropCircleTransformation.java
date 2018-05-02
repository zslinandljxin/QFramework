package com.zsl.zhaoqing.framework.image.Gildes.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CropCircleTransformation extends BitmapTransformation {
    public CropCircleTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        CircleDrawable circleDrawable = new CircleDrawable(toTransform);
        Bitmap.Config config = circleDrawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap circleBitmap = Bitmap.createBitmap(width, height,config);
        Canvas canvas = new Canvas(circleBitmap);
        circleDrawable.setBounds(0, 0, width, height);
        circleDrawable.draw(canvas);
        return circleBitmap;
    }

    @Override
    public String getId() {
        return "CropCircleTransformation";
    }
}
