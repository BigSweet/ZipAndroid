package com.zip.zipandroid.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.zip.zipandroid.R;


public class AnimationUtils {
    public static void updateHeight(View view, int height) {
        int currentHeight = view.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(currentHeight, height);
        animator.addUpdateListener(valueAnimator -> {
            //获取当前的height值
            //动态更新view的高度
            view.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
            view.requestLayout();
        });
        animator.setDuration(300);
        animator.start();
    }

    public static void updateAlpha(View view, float alpha) {
        float currentAlpha = view.getAlpha();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", currentAlpha, alpha);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (alpha == 0) {
                    view.setVisibility(View.GONE);
                } else if (alpha == 1) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(300);
        animator.start();
    }


    public static void updateSize(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animator.setDuration(1000);
        animator.start();
        ObjectAnimator toViewAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        toViewAnimatorX.setDuration(1000);
        toViewAnimatorX.start();
        ObjectAnimator toViewAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
        toViewAnimatorY.setDuration(1000);
        toViewAnimatorY.start();
    }

    public static void tabAnim(View view) {
        ObjectAnimator toViewAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 0.95f, 1.1f, 1f, 1.05f, 1f);
        toViewAnimatorX.setDuration(500);
        toViewAnimatorX.start();
        ObjectAnimator toViewAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f, 0.95f, 1.1f, 1f, 1.05f, 1f);
        toViewAnimatorY.setDuration(500);
        toViewAnimatorY.start();
    }

    public static void piaSettlementIconAnim(View view) {
        ObjectAnimator toViewAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.5f, 1.1f, 0.9f, 1f);
        toViewAnimatorX.setDuration(500);
        toViewAnimatorX.start();
        ObjectAnimator toViewAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 0.5f, 1.1f, 0.9f, 1f);
        toViewAnimatorY.setDuration(500);
        toViewAnimatorY.start();
    }

    public static void piaSettlementNameAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animator.setDuration(800);
        animator.start();
    }

    public static void piaSettlementCountAnim(View view) {
        Animation anim = new RotateAnimation(0f, 3f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        anim.setFillAfter(true);
        anim.setDuration(50);
        anim.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(anim);

        Animation anim2 = new RotateAnimation(3f, 0, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        anim.setFillAfter(true);
        anim2.setDuration(50);
        anim2.setStartOffset(50);
        anim2.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(anim2);
    }

    public static void showUp(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_view_up_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void showDown(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_view_down_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void hideDown(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_view_down_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void hideUp(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_view_up_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void rotation(View view, int duration, int count) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotation.setDuration(duration);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(count);
        rotation.start();
    }

    public static void stopAnimation(View view) {
        if (view.getAnimation() != null) {
            view.getAnimation().cancel();
        }
    }
}
