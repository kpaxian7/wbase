package com.bnb.wbasemodule.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtils {

    private static WeakReference<Toast> sToast;
    private static WeakReference<Toast> sCustomViewToast;
    public static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void show(CharSequence content) {
        show(sContext, content);
    }

    /**
     * 居中显示
     *
     * @param contentView
     */
    public static void show(View contentView) {
        show(contentView, Gravity.CENTER, 0, 0);
    }

    public static void show(View contentView, int gravity, int xOffset, int yOffset) {
        if (contentView == null) {
            return;
        }
        if (sCustomViewToast == null || sCustomViewToast.get() == null) {
            sCustomViewToast = new WeakReference<>(Toast.makeText(sContext, "", Toast.LENGTH_SHORT));
        }
        sCustomViewToast.get().setView(contentView);
        sCustomViewToast.get().setGravity(gravity, xOffset, yOffset);
        sCustomViewToast.get().show();
    }

    public static void show(Context context, CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<>(Toast.makeText(context, content, Toast.LENGTH_SHORT));
        }
        sToast.get().setText(content);
        sToast.get().show();
    }

    public static void cancel() {
        if (sToast != null && sToast.get() != null) {
            sToast.get().cancel();
        }
        if (sCustomViewToast != null && sCustomViewToast.get() != null) {
            sCustomViewToast.get().cancel();
        }
    }

}
