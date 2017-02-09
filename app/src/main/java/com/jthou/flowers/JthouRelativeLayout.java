package com.jthou.flowers;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

/**
 * Created by user on 2016/12/15.
 */
public class JthouRelativeLayout extends RelativeLayout {

    public JthouRelativeLayout(Context context) {
        super(context);
    }

    public JthouRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JthouRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ImageView target = (ImageView) child;
        int width = target.getDrawable().getIntrinsicWidth();
        int height = target.getDrawable().getIntrinsicHeight();
        params.leftMargin = (ScreenUtils.getScreenWidth(getContext()) - width) / 2;
        params.topMargin = ScreenUtils.getScreenHeight(getContext()) - height;
        addView(child, params);
    }

    @Override
    public void addView(final View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleXAnim = scaleX(child);
        ObjectAnimator scaleYAnim = scaleY(child);
        set.play(scaleXAnim).with(scaleYAnim);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                 bezier(child);
            }
        });
    }

    private void bezier(final View target) {
        ImageView child = (ImageView) target;
        int width = child.getDrawable().getIntrinsicWidth();
        int height = child.getDrawable().getIntrinsicHeight();

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
                LayoutParams params = (LayoutParams) target.getLayoutParams();
                params.leftMargin = (int) p.x;
                params.topMargin = (int) p.y;
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(target);
            }
        });
        animator.start();
    }

    private ObjectAnimator scaleX(View target) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(target, "scaleX", 0.0f, 1.0f);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }

    private ObjectAnimator scaleY(View target) {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(target, "scaleY", 0.0f, 1.0f);
        scaleAnim.setDuration(2000);
        return scaleAnim;
    }
}
