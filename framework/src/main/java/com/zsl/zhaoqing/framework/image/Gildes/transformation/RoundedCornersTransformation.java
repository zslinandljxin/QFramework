package com.zsl.zhaoqing.framework.image.Gildes.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RoundedCornersTransformation extends BitmapTransformation{

    private int XRadius = 0;
    private int YRadius = 0;

    public RoundedCornersTransformation(Context context) {
        super(context);
    }

    public RoundedCornersTransformation(Context context, int XRadius, int YRadius) {
        super(context);
        this.XRadius = XRadius;
        this.YRadius = YRadius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, XRadius, YRadius, paint);
        return bitmap;
    }

    @Override
    public String getId() {
        return "RoundedCornersTransformation";
    }
}
