package com.bnb.wbasemodule.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppUtils {

  private static Boolean sIsDebug = null;

  public static boolean isDebug() {
    return sIsDebug == null ? false : sIsDebug.booleanValue();
  }

  public static void syncsIsDebug(Context context) {
    if (sIsDebug == null) {
      sIsDebug = context.getApplicationInfo() != null &&
          (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
  }

  /**
   * 获取app版本号
   *
   * @return
   */
  public static int getVersionCode(Context ctx) {
    int versionCode = 0;
    try {
      versionCode = ctx.getPackageManager()
          .getPackageInfo(ctx.getPackageName(), 0).versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      Log.i("wx", "获取版本号出错");
    }
    return versionCode;
  }
}
