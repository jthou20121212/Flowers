package com.jthou.flowers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

/**
 * Created by user on 2017/2/8.
 */

public class HeartView extends ImageView {

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
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int width = getDrawable().getIntrinsicWidth();
        int height = getDrawable().getIntrinsicHeight();
        params.leftMargin = (ScreenUtils.getScreenWidth(getContext()) - width) / 2;
        // 这个值可以适当的改一下
        params.topMargin = ScreenUtils.getScreenHeight(getContext()) - height * 3;
        setLayoutParams(params);
        animator();
    }

    private void animator() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleXAnim = scaleX();
        ObjectAnimator scaleYAnim = scaleY();
        set.play(scaleXAnim).with(scaleYAnim);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                 bezier();
            }
        });
    }

    private void bezier() {
        int width = getDrawable().getIntrinsicWidth();
        int height = getDrawable().getIntrinsicHeight();

        float startX = (ScreenUtils.getScreenWidth(getContext()) - width) / 2;
        float startY = ScreenUtils.getScreenHeight(getContext()) - height;
        float stopX = new Random().nextInt(ScreenUtils.getScreenWidth(getContext()));
        float stopY = 0;
        float controlX = new Random().nextInt(ScreenUtils.getScreenWidth(getContext()));
        float controlY = ScreenUtils.getScreenHeight(getContext()) / 2;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, stopX, stopY);
        BezierEvaluator evaluator = new BezierEvaluator(new PointF(controlX, controlY));
        final PointF start = new PointF(startX, startY);
        final PointF stop = new PointF(stopX, stopY);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, start, stop);
        animator.setDuration(3000);
        // animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final PointF p = (PointF) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
                params.leftMargin = (int) p.x;
                params.topMargin = (int) p.y;
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                if(getParent() != null && getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();
                    parent.removeView(HeartView.this);
                }
            }
        });
        animator.start();
    }

    private ObjectAnimator scaleX() {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1.0f);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }

    private ObjectAnimator scaleY() {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1.0f);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }


}
