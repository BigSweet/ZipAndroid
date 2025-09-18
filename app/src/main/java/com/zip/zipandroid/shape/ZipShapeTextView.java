package com.zip.zipandroid.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.zip.zipandroid.R;
import com.zip.zipandroid.utils.ZipScaleClickHelper;


public class ZipShapeTextView extends AppCompatTextView {
    protected int orientation;
    protected float[] radius;
    int[] colors = null;
    int textColor = 0;
    int textDisableColor = 0;
    int[] disableColors = null;
    boolean isEnablePlus = true;
    boolean needScale = true;

    String disableText;
    String enableText;

    public ZipShapeTextView(Context context) {
        super(context);
    }

    public ZipShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ZipShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeTextView);

            orientation = a.getInt(R.styleable.ShapeTextView_orientation, 1);
            int color = a.getColor(R.styleable.ShapeTextView_sv_tx_background, Color.TRANSPARENT);
            int colorS = a.getColor(R.styleable.ShapeTextView_sv_tx_background_start, Color.TRANSPARENT);
            int colorE = a.getColor(R.styleable.ShapeTextView_sv_tx_background_end, Color.TRANSPARENT);
            float r = a.getDimension(R.styleable.ShapeTextView_sv_tx_radius, 0);
            int colorD = a.getColor(R.styleable.ShapeTextView_sv_tx_disable_background, Color.TRANSPARENT);
            int colorDS = a.getColor(R.styleable.ShapeTextView_sv_tx_disable_background_start, Color.TRANSPARENT);
            int colorDE = a.getColor(R.styleable.ShapeTextView_sv_tx_disable_background_end, Color.TRANSPARENT);
            textColor = a.getColor(R.styleable.ShapeTextView_sv_tx_color, Color.TRANSPARENT);
            textDisableColor = a.getColor(R.styleable.ShapeTextView_sv_tx_disable_color, Color.TRANSPARENT);
            isEnablePlus = a.getBoolean(R.styleable.ShapeTextView_sv_tx_enable, true);
            needScale = a.getBoolean(R.styleable.ShapeTextView_sv_tx_need_scale, true);
            disableText = a.getString(R.styleable.ShapeTextView_sv_tx_disable_text);
            enableText = a.getString(R.styleable.ShapeTextView_sv_tx_enable_text);


            float radiusTopLeft = a.getDimension(R.styleable.ShapeTextView_sv_tx_radius_top_left, 0);
            float radiusTopRight = a.getDimension(R.styleable.ShapeTextView_sv_tx_radius_top_right, 0);
            float radiusBottomRight = a.getDimension(R.styleable.ShapeTextView_sv_tx_radius_bottom_right, 0);
            float radiusBottomLeft = a.getDimension(R.styleable.ShapeTextView_sv_tx_radius_bottom_left, 0);

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
                setBackground(orientation, radius, isEnablePlus ? colors : disableColors);
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

            setEnabledPlus(isEnablePlus);
            if (needScale) {
                ZipScaleClickHelper.setScaleClick(this);
            }
        }
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setBackground(int orientation, float r, int... colors) {
        setBackground(orientation, new float[]{
                r
                , r
                , r
                , r
                , r
                , r
                , r
                , r}, colors);

    }

    public void setBackground(int... color) {
        setBackground(orientation, radius, color);
    }

    public void setBackground(int orientation, float[] radius, int... colors) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (colors != null && colors.length > 1) {
            if (orientation == 1) {
                gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            } else {
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
        setBackground(orientation, radius, colors);
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


    public void setRadius(float radiu) {
        int r = ConvertUtils.dp2px(radiu);
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
            if (textColor != 0) {
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
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Drawable[] drawables = getCompoundDrawables();
//        if (drawables != null) {
//            Drawable leftDrawable = drawables[0]; //drawableLeft
//            Drawable rightDrawable = drawables[2];//drawableRight
//            if (leftDrawable != null || rightDrawable != null) {
//                //1,获取text的width
//                float textWidth = getPaint().measureText(getText().toString());
//                //2,获取padding
//                int drawablePadding = getCompoundDrawablePadding();
//                int drawableWidth;
//                float bodyWidth;
//                if (leftDrawable != null) {
//                    //3,获取drawable的宽度
//                    drawableWidth = leftDrawable.getIntrinsicWidth();
//                    //4,获取绘制区域的总宽度
//                    bodyWidth = textWidth + drawablePadding + drawableWidth;
//                } else {
//                    drawableWidth = rightDrawable.getIntrinsicWidth();
//                    bodyWidth = textWidth + drawablePadding + drawableWidth;
//                    //图片居右设置padding
//                    setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);
//                }
//                canvas.translate((getWidth() - bodyWidth) / 2, 0);
//            }
//        }
//        super.onDraw(canvas);
//    }
}
