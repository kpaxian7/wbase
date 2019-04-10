package com.bnb.wbasemodule.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.bnb.wbasemodule.http.RetrofitCreator;
import com.bnb.wbasemodule.utils.ActivityManager;
import com.bnb.wbasemodule.utils.CrashHandler;
import com.bnb.wbasemodule.utils.ToastUtils;

public class BaseApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        registerActivityCallBlackListener();
        ToastUtils.init(this);
        CrashHandler.getInstance().init(this);
    }

    /**
     * 注册Activity生命周期
     */
    private void registerActivityCallBlackListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.popActivity(activity.getClass());
            }
        });
    }

}
