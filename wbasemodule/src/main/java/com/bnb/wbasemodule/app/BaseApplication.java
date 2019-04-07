package com.bnb.wbasemodule.app;

import android.app.Application;
import android.content.Context;

import com.bnb.wbasemodule.utils.CrashHandler;

public class BaseApplication extends Application {

  private static Context mContext;

  public static Context getContext() {
    return mContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = this;
    CrashHandler.getInstance().init(this);
  }
}
