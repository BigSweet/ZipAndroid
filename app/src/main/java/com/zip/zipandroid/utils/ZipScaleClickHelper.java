package com.zip.zipandroid.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.zip.zipandroid.R;


public class ZipScaleClickHelper {

    public static void setScaleClick(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down(v);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        callBackAnim(v);
                        break;
                    case MotionEvent.ACTION_UP:
                        up(v);
                        break;
                }
                return false;
            }
        });
    }

    private static void down(View view) {

        view.setTag(R.id.id_key_tag, true);
        view.setPivotX(view.getWidth() / 2);  // X方向中点
        view.setPivotY(view.getHeight() / 2);   // Y方向中点
        animScale(view, 1f, 0.95f);

    }

    private static void up(View view) {
        view.setTag(R.id.id_key_tag, false);
        view.setPivotX(view.getWidth() / 2);  // X方向中点
        view.setPivotY(view.getHeight() / 2);   // Y方向中点
        animScale(view, 0.95f, 1f);
    }

    private static void animScale(View view, float... arg) {
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", arg);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", arg);
        animatorSet.setDuration(200);  //动画时间
        animatorSet.setInterpolator(new AccelerateInterpolator());  //设置插值器
        animatorSet.play(scaleX).with(scaleY);  //同时执行
        animatorSet.start();  //启动动画
    }

    private static void callBackAnim(View v) {
        Object tag = v.getTag(R.id.id_key_tag);
        if (tag != null && tag instanceof Boolean && (boolean) tag) {
            up(v);
        }

    }


}
