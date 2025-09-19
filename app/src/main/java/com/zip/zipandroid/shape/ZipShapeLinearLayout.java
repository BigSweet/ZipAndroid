package com.zip.zipandroid.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zip.zipandroid.R;


public class ZipShapeLinearLayout extends LinearLayout {
    protected int orientation;
    float[] radius;
    public ZipShapeLinearLayout(Context context) {
        super(context);
    }

    public ZipShapeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ZipShapeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZipShapeLinearLayout);
            orientation = a.getInt(R.styleable.ZipShapeLinearLayout_sv_orientation, 0);
            int color = a.getColor(R.styleable.ZipShapeLinearLayout_sv_background, Color.TRANSPARENT);
            int colorS = a.getColor(R.styleable.ZipShapeLinearLayout_sv_background_start, Color.TRANSPARENT);
            int colorE = a.getColor(R.styleable.ZipShapeLinearLayout_sv_background_end, Color.TRANSPARENT);
            float r0 = a.getDimension(R.styleable.ZipShapeLinearLayout_sv_radius, 0);
            float radiusTopLeft = a.getDimension(R.styleable.ZipShapeLinearLayout_sv_radius_top_left, 0);
            float radiusTopRight = a.getDimension(R.styleable.ZipShapeLinearLayout_sv_radius_top_right, 0);
            float radiusBottomRight = a.getDimension(R.styleable.ZipShapeLinearLayout_sv_radius_bottom_right, 0);
            float radiusBottomLeft = a.getDimension(R.styleable.ZipShapeLinearLayout_sv_radius_bottom_left, 0);

            a.recycle();
            int colorArray[] = color != Color.TRANSPARENT? new int[]{color, color} : new int[]{colorS, colorE};

            if (radiusTopLeft != 0
                    || radiusTopRight != 0
                    || radiusBottomLeft != 0
                    || radiusBottomRight != 0
                    ) {
                radius = new float[]{
                        radiusTopLeft
                        , radiusTopLeft
                        , radiusTopRight
                        , radiusTopRight
                        , radiusBottomLeft
                        , radiusBottomLeft
                        , radiusBottomRight
                        , radiusBottomRight};
            }
            if (radius == null) {

                radius = new float[]{
                  r0
                  ,r0
                  ,r0
                  ,r0
                  ,r0
                  ,r0
                  ,r0
                  ,r0
                };
            }
            setBackground(colorArray, radius);
        }
    }

    public void setBackground(int color) {

        setBackground(new int[] {color, color},  radius);
    }
    public void setBackground(int color, float radius) {
        setBackground(new int[] {color, color},  radius);
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
