package com.jthou.flowers;

import android.graphics.PointF;

import com.nineoldandroids.animation.TypeEvaluator;

/**
 * Created by user on 2017/2/7.
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mControlPointF;

    public BezierEvaluator(PointF controlPointF) {
        mControlPointF = controlPointF;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(fraction, startValue, mControlPointF, endValue);
    }

}
