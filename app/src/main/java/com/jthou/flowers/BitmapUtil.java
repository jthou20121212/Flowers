package com.jthou.flowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import java.util.Random;

/**
 * Created by user on 2017/2/8.
 */

public class BitmapUtil {

    private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private static final Canvas sCanvas = new Canvas();
    private static Random mRandom = new Random();

    public static Bitmap createHeart(Context context) {
        Bitmap heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        Bitmap heartBorder = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart_border);
        Bitmap bm = Bitmap.createBitmap(heartBorder.getWidth(), heartBorder.getHeight(), Bitmap.Config.ARGB_8888);
        if (bm == null) {
            return null;
        }
        Canvas canvas = sCanvas;
        canvas.setBitmap(bm);
        Paint p = sPaint;
        // 画边框
        canvas.drawBitmap(heartBorder, 0, 0, p);
        // 随机生成爱心颜色
        int color = Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
        // 设置ColorFilter
        p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        float dx = (heartBorder.getWidth() - heart.getWidth()) / 2f;
        float dy = (heartBorder.getHeight() - heart.getHeight()) / 2f;
        // 因为边框图片比爱心图片大，爱心会在边框的中间
        canvas.drawBitmap(heart, dx, dy, p);
        p.setColorFilter(null);
        canvas.setBitmap(null);
        return bm;
    }

}
