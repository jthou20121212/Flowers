package com.jthou.flowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

/**
 * Created by user on 2017/2/8.
 */

public class HeartView extends AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Bitmap bm = BitmapUtil.createHeart(context);
        setImageBitmap(bm);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int width = getDrawable().getIntrinsicWidth();
        int height = getDrawable().getIntrinsicHeight();
        // 控制自己在父控件中的位置
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        params.leftMargin = (ScreenUtils.getScreenWidth(getContext()) - width) / 2;
        // 这个值可以适当的改一下
        params.topMargin = ScreenUtils.getScreenHeight(getContext()) - height * 3;
        setLayoutParams(params);
        zoom();
    }

    /**
     * 放大动画
     */
    private void zoom() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleXAnim = scaleX(0.0f, 1.0f);
        ObjectAnimator scaleYAnim = scaleY(0.0f, 1.0f);
        set.play(scaleXAnim).with(scaleYAnim);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束之后，
                bezier();
            }
        });
    }

    /**
     * 缩小动画
     */
    private void shrink() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleXAnim = scaleX(1.0f, 0.0f);
        ObjectAnimator scaleYAnim = scaleY(1.0f, 0.0f);
        set.play(scaleXAnim).with(scaleYAnim);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束，移除控件
                if(getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();
                    parent.removeView(HeartView.this);
                }
            }
        });
    }


    private void bezier() {
        final int width = getDrawable().getIntrinsicWidth();
        final int height = getDrawable().getIntrinsicHeight();
        final int screentWidth = ScreenUtils.getScreenWidth(getContext());
        final int screenHeight = ScreenUtils.getScreenHeight(getContext());
        final Random random = new Random();
        // 设置贝塞尔曲线的起始坐标和控制坐标
        // 开始坐标与onAttachedToWindow中的初始坐标一致
        // 结束坐标
        float startX = (screentWidth- width) / 2;
        float startY = screenHeight - height * 3f;
        float stopX = random.nextInt(screentWidth);
        float stopY = 0;
        float controlX = random.nextInt(screentWidth);
        float controlY = ScreenUtils.getScreenHeight(getContext()) / 2;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, stopX, stopY);
        BezierEvaluator evaluator = new BezierEvaluator(new PointF(controlX, controlY));
        final PointF start = new PointF(startX, startY);
        final PointF stop = new PointF(stopX, stopY);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, start, stop);
        animator.setDuration(3000);
        animator.addUpdateListener(this);
        animator.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                 shrink();
            }
        });
        animator.start();
    }

    private ObjectAnimator scaleX(float start, float end) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(this, "scaleX", start, end);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }

    private ObjectAnimator scaleY(float start, float end) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(this, "scaleY", start, end);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        // 不断获取曲线上的点，更新控件坐标，实现控件随着曲线移动
        final PointF p = (PointF) animation.getAnimatedValue();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        params.leftMargin = (int) p.x;
        params.topMargin = (int) p.y;
        setLayoutParams(params);
    }

}
