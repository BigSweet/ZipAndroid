package com.zip.zipandroid.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zip.zipandroid.R;


public class ShapeConstraintLayout extends ConstraintLayout {
    protected int orientation;
    float[] r;
    public ShapeConstraintLayout(Context context) {
        super(context);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeConstraintLayout);
            orientation = a.getInt(R.styleable.ShapeConstraintLayout_sv_scl_orientation, 1);
            int color = a.getColor(R.styleable.ShapeConstraintLayout_sv_scl_background, Color.TRANSPARENT);
            int colorS = a.getColor(R.styleable.ShapeConstraintLayout_sv_scl_background_start, Color.TRANSPARENT);
            int colorE = a.getColor(R.styleable.ShapeConstraintLayout_sv_scl_background_end, Color.TRANSPARENT);
            float radius = a.getDimension(R.styleable.ShapeConstraintLayout_sv_scl_radius, 0);
            float radiusTopLeft = a.getDimension(R.styleable.ShapeConstraintLayout_sv_scl_radius_top_left, 0);
            float radiusTopRight = a.getDimension(R.styleable.ShapeConstraintLayout_sv_scl_radius_top_right, 0);
            float radiusBottomRight = a.getDimension(R.styleable.ShapeConstraintLayout_sv_scl_radius_bottom_right, 0);
            float radiusBottomLeft = a.getDimension(R.styleable.ShapeConstraintLayout_sv_scl_radius_bottom_left, 0);

            a.recycle();
            int colorArray[] = color != Color.TRANSPARENT? new int[]{color, color} : new int[]{colorS, colorE};

            if (radiusTopLeft != 0
                    || radiusTopRight != 0
                    || radiusBottomLeft != 0
                    || radiusBottomRight != 0
                    ) {
                r = new float[]{
                        radiusTopLeft
                        , radiusTopLeft
                        , radiusTopRight
                        , radiusTopRight
                        , radiusBottomLeft
                        , radiusBottomLeft
                        , radiusBottomRight
                        , radiusBottomRight};
            }else {
                r = new float[]{
                        radius
                        , radius
                        , radius
                        , radius
                        , radius
                        , radius
                        , radius
                        , radius};
            }
            setBackground(colorArray, r);
//            if (r != null) {
//                setBackground(colorArray, r);
//            } else {
//
//                setBackground(colorArray, radius);
//            }
        }
    }

    public void setBackground(int color, float radius) {
        setBackground(new int[] {color, color},  radius);
    }
    public void setBackground2(int color) {
        setBackground(new int[] {color, color},  r);
    }

    public void setBackground(int[] colors, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColors(colors);
        if(orientation == 1){
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        }else{
            gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        }
        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
    }

    public void setBackground(int[] colors, float[] radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColors(colors);
        if(orientation == 1){
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        }else{
            gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        }
        gradientDrawable.setCornerRadii(radius);
        setBackground(gradientDrawable);
    }
}
