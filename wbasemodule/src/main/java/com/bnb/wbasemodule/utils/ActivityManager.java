package com.bnb.wbasemodule.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ActivityManager {
    private static final String TAG = ActivityManager.class.getSimpleName();
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    public static List getTaskList() {
        return mActivitys;
    }

    /*
        压栈
     */
    public static void pushActivity(Activity activity) {
        mActivitys.add(activity);
        Log.e(TAG, "Task Size = " + mActivitys.size());
    }

    /*
        指定className  弾栈
     */
    public static void popActivity(Class<?> cls) {
        for (int i = ActivityManager.mActivitys.size() - 1; i >= 0; i--) {
            Activity activity = mActivitys.get(i);
            if (activity != null) {
                if (activity.getClass().equals(cls)) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    ActivityManager.mActivitys.remove(activity);
                    break;
                }
            }
        }
        Log.e(TAG, "Task Size = " + ActivityManager.mActivitys.size());
    }


    /**
     * 清栈 保留当前
     */

    public static void finishAllActivity(Activity currentActivity) {
        for (Activity activity : mActivitys) {
            if (activity != null) {
                //保留当前
                if (!activity.getClass().equals(currentActivity.getClass())) {
                    activity.finish();
                    mActivitys.remove(activity);
                }
            }
        }
        Log.e(TAG, "Task Size = " + mActivitys.size());
    }

    /**
     * 清栈  退出app
     */
    public static void finishAllActivity() {
        for (Activity activity : mActivitys) {
            if (activity != null) {
                activity.finish();
                mActivitys.remove(activity);
            }
        }
        Log.e(TAG, "Task Size = " + mActivitys.size());
    }

    /**
     * 查询指定activity是否还在栈中
     *
     * @param clz
     * @return
     */
    public static boolean getIsLiveing(Class clz) {
        for (Activity activity : mActivitys) {
            if (activity != null) {
                if (activity.getClass().equals(clz)) {
                    return true;
                }
            }
        }
        return false;
    }

}
