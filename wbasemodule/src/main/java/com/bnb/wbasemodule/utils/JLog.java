package com.bnb.wbasemodule.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JLog {

  public static void init() {
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .logStrategy(new LogCatStrategy())
        .methodCount(0)
        .methodOffset(7)
        .build();
    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
      @Override
      public boolean isLoggable(int priority, String tag) {
        return AppUtils.isDebug();
      }
    });
  }


  public static void v(String tag, String msg) {
    Logger.t(tag).v(msg);
  }

  public static void d(String tag, String msg) {
    Logger.t(tag).d(msg);
  }

  public static void i(String tag, String msg) {
    Logger.t(tag).i(msg);
  }

  public static void w(String tag, String msg) {
    Logger.t(tag).w(msg);
  }

  public static void e(String tag, String msg) {
    Logger.t(tag).e(msg);
  }

  public static void json(String tag, String json) {
    Logger.t(tag).d(formatJson(json));
  }

  public static String formatJson(String jsonStr) {
    try {
      jsonStr = jsonStr.trim();
      if (jsonStr.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.toString(2);
      }
      if (jsonStr.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(jsonStr);
        return jsonArray.toString(2);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return "Json格式有误: " + jsonStr;
  }

}
