package com.bnb.wbasemodule.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;

import com.bnb.wbasemodule.R;

import java.io.File;

public abstract class AUpdateDialog extends Dialog implements UpdateListener {

    private static final int UPDATE_START = 101;
    private static final int UPDATE_ING = 102;
    private static final int UPDATE_FAIL = 103;
    private static final int UPDATE_FINISH = 104;

    protected Context mContext;
    protected AUpdateHelper mHelper;

    @SuppressWarnings("handlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_START:
                    onUpdateStart();
                    break;
                case UPDATE_ING:
                    int pro = (int) msg.obj;
                    onUpdateProgress(pro);
                    break;
                case UPDATE_FAIL:
                    onUpdateFail();
                    break;
                case UPDATE_FINISH:
                    String path = (String) msg.obj;
                    onUpdateFinish(path);
                    break;
            }

        }
    };

    public AUpdateDialog(Context context, AUpdateHelper helper) {
        this(context, R.style.BaseDialog, helper);
    }

    public AUpdateDialog(Context context, int themeResId, AUpdateHelper helper) {
        super(context, themeResId);
        mContext = context;
        mHelper = helper;
    }

    @Override
    public void onStartDown() {
        mHandler.sendEmptyMessage(UPDATE_START);
    }

    @Override
    public void onFail(String failMsg) {
        mHandler.sendEmptyMessage(UPDATE_FAIL);
    }

    @Override
    public void onProgress(int pro) {
        Message msg = new Message();
        msg.obj = pro;
        msg.what = UPDATE_ING;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFinish(String path) {
        Message msg = new Message();
        msg.obj = path;
        msg.what = UPDATE_FINISH;
        mHandler.sendMessage(msg);
    }

    protected abstract void onUpdateStart();

    protected void onUpdateFail() {

    }

    protected abstract void onUpdateProgress(int progress);

    protected void onUpdateFinish(String path) {
        mHelper.installApk(path);
    }

}
