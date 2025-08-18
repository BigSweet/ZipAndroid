package com.zip.zipandroid.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: jack
 * Description:管理所有的栈中的Activity
 */
public class ActivityCollector {

    /**
     * 存放activity的列表
     */
    public static List<Activity> activities = new CopyOnWriteArrayList<>();

    /**
     * 添加Activity
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
//        DebugLog.i("activities after add size: " + activities.size());
    }


    public static Activity getStackFirstActivity() {
        if (activities.size() > 0) {
            return activities.get(0);
        }
        return null;
    }

    public static Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (activities.size() > 0) {
            for (int i = activities.size() - 1; i > -1; i--) {
                currentActivity = activities.get(i);
                if (currentActivity.isFinishing()) {
                    activities.remove(i);
                } else {
                    break;
                }
            }
        }
        return currentActivity;
    }

    /**
     * 获得指定activity实例
     *
     * @return
     */
    public static <T extends Activity> T getActivity(String calssName) {
        for (Activity activity : activities) {
            if (activity.getClass().getSimpleName().equals(calssName)) {
                return (T) activity;
            }
        }
        return null;
    }

    public static boolean contains(List<String> className) {
        for (Activity activity : activities) {
            if (className.contains(activity.getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String className) {
        for (Activity activity : activities) {
            if (activity.getClass().getSimpleName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTop(Activity a) {
        int size = activities.size();
        return (a != null && size > 1 && a == activities.get(size - 1));
    }

    public static boolean popToActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        int size = activities.size();
        for (int i = size - 1; i > -1; i--) {
            Activity a = activities.get(i);
            if (a == activity) {
                return true;
            }
            a.finish();

        }

        return false;
    }

    /**
     * 移除activity,代替finish
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
//            DebugLog.i("activities after remove size: " + activities.size());
        }
    }


    /**
     * 移除所有的Activity
     */
    public static void removeAllActivity() {
        if (activities != null && activities.size() > 0) {
            for (Activity activity : activities) {
                if (!TextUtils.equals("LoginActivity", activity.getClass().getSimpleName()) && !activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activities.clear();
    }


    // 当前Acitity个数
    public static int activityAmount = 0;

    public static boolean isBackground() {
        return activityAmount == 0;
    }

    public static Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
//            if (activityAmount == 0) {
//                //app回到前台
//            }
            activityAmount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {


        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAmount--;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}
