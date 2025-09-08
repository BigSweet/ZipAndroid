package com.zip.zipandroid.utils;

import android.animation.ObjectAnimator;
import android.view.View;


public class AnimationUtils {

    public static void tabAnim(View view) {
        ObjectAnimator toViewAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 0.95f, 1.1f, 1f, 1.05f, 1f);
        toViewAnimatorX.setDuration(500);
        toViewAnimatorX.start();
        ObjectAnimator toViewAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f, 0.95f, 1.1f, 1f, 1.05f, 1f);
        toViewAnimatorY.setDuration(500);
        toViewAnimatorY.start();
    }

}
