package com.zsl.zhaoqing.framework.image.Gildes.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RotateTransformation extends BitmapTransformation{

    private int degree = 0;

    public RotateTransformation(Context context) {
        super(context);
    }

    public RotateTransformation(Context context, int degree) {
        super(context);
        this.degree = degree;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degree, width / 2, height / 2);
        Bitmap bitmap = Bitmap.createBitmap(toTransform, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    @Override
    public String getId() {
        return "RotateTransformation";
    }
}
