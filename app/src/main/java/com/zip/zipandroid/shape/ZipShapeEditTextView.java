package com.zip.zipandroid.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.zip.zipandroid.R;


public class ZipShapeEditTextView extends AppCompatEditText {
    protected int orientation;
    protected float[] radius;
    int[] colors = null;
    int textColor = 0;
    int textDisableColor = 0;
    int[] disableColors = null;
    boolean isEnablePlus = true;

    String disableText;
    String enableText;
    public ZipShapeEditTextView(Context context) {
        super(context);
    }

    public ZipShapeEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ZipShapeEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeEditTextView);

            orientation = a.getInt(R.styleable.ShapeEditTextView_setv_orientation, 0);
            int color = a.getColor(R.styleable.ShapeEditTextView_setv_background, Color.TRANSPARENT);
            int colorS = a.getColor(R.styleable.ShapeEditTextView_setv_background_start, Color.TRANSPARENT);
            int colorE = a.getColor(R.styleable.ShapeEditTextView_setv_background_end, Color.TRANSPARENT);
            float r  = a.getDimension(R.styleable.ShapeEditTextView_setv_radius, 0);
            int colorD = a.getColor(R.styleable.ShapeEditTextView_setv_disable_background, Color.TRANSPARENT);
            int colorDS = a.getColor(R.styleable.ShapeEditTextView_setv_disable_background_start, Color.TRANSPARENT);
            int colorDE = a.getColor(R.styleable.ShapeEditTextView_setv_disable_background_end, Color.TRANSPARENT);


            float radiusTopLeft = a.getDimension(R.styleable.ShapeEditTextView_setv_radius_top_left, 0);
            float radiusTopRight = a.getDimension(R.styleable.ShapeEditTextView_setv_radius_top_right, 0);
            float radiusBottomRight = a.getDimension(R.styleable.ShapeEditTextView_setv_radius_bottom_right, 0);
            float radiusBottomLeft = a.getDimension(R.styleable.ShapeEditTextView_setv_radius_bottom_left, 0);

            a.recycle();

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
            } else if (r != 0) {
                radius = new float[]{
                        r
                        , r
                        , r
                        , r
                        , r
                        , r
                        , r
                        , r};
            }
            if (color != Color.TRANSPARENT) {
                colors = new int[]{color};
            } else {
                colors = new int[]{colorS, colorE};
            }
            if (colorD != Color.TRANSPARENT) {
                disableColors = new int[]{colorD};
            } else {
                disableColors = new int[]{colorDS, colorDE};
            }
            if (colors != null || radius != null) {
                setBackground(orientation,radius, isEnablePlus ? colors : disableColors);
            }

            if (isEnablePlus) {
                if (textColor != Color.TRANSPARENT) {

                    setTextColor(textColor);
                }
            } else {
                if (textDisableColor != Color.TRANSPARENT) {
                    setTextColor(textDisableColor);
                }
            }

        }
    }
    public void setBackground(int ...color) {
        setBackground(orientation,radius, color);
    }
    public void setBackground(int orientation, float[] radius, int... colors) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (colors != null && colors.length > 1) {
            if(orientation == 1){
                gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            }else{
                gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            }

            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setColors(colors);
        } else if (colors.length == 1) {
            gradientDrawable.setColor(colors[0]);
        }
        gradientDrawable.setCornerRadii(radius);
//        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
    }

    public void changeBackground(int... colors) {
        setBackground( orientation,radius, colors);
    }

    public void setBackground(int color, float[] radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadii(radius);
        setBackground(gradientDrawable);
    }



    public void setShapeBackground(int... colors) {
        this.colors = colors;

        if (colors != null || radius != null) {
            setBackground(orientation, radius, colors);
        }
    }
    public void setEnableBackground(int... colors) {
        this.colors = colors;
    }

    public void setDisableBackground(int... colors) {
        this.disableColors = colors;
    }

    public String getDisableText() {
        return disableText;
    }

    public void setDisableText(String disableText) {
        this.disableText = disableText;
    }

    public String getEnableText() {
        return enableText;
    }

    public void setEnableText(String enableText) {
        this.enableText = enableText;
    }

    public void setEnabledPlus(boolean enabled) {
        super.setEnabled(enabled);
        setEnableCanClick(enabled);
    }

    public void setEnableCanClick(boolean enabled) {
        if (disableText != null && enableText != null) {

            setText(enabled ? enableText : disableText);
        }
        if (enabled) {
            if (colors != null || radius != null) {
                setBackground(orientation, radius, colors);
            }
            if (textColor != 0){
                setTextColor(textColor);
            }
        } else {
            if (disableColors != null || radius != null) {
                setBackground(orientation, radius, disableColors);
            }
            if (textDisableColor != Color.TRANSPARENT) {
                setTextColor(textDisableColor);
            }
        }
    }
}
