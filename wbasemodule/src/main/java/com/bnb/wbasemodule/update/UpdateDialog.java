package com.bnb.wbasemodule.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bnb.wbasemodule.R;

import java.io.File;

public class UpdateDialog extends Dialog implements UpdateListener {

    private static final String MODE_UPDATING = "MODE_UPDATING";
    private static final String MODE_ACTION = "MODE_ACTION";

    private static final int UPDATE_START = 101;
    private static final int UPDATE_ING = 102;
    private static final int UPDATE_FAIL = 103;
    private static final int UPDATE_FINISH = 104;

    private ImageView mIvIcon;
    private TextView mTvUpdateTips;
    private ProgressBar mProgress;
    private TextView mTvProgressNum;
    private LinearLayout mLlAction;
    private TextView mTvCancel;
    private TextView mTvSure;

    private Context mContext;
    private String mApkPath;
    private String mPkgName;

    private boolean mIsForce;
    private int mIconRes;
    private View.OnClickListener mSureListener;

    @SuppressWarnings("handlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_START:
                    mProgress.setProgress(0);
                    changeMode(MODE_UPDATING);
                    break;
                case UPDATE_ING:
                    int pro = (int) msg.obj;
                    mProgress.setProgress(pro);
                    mTvProgressNum.setText(pro + "%");
                    break;
                case UPDATE_FAIL:
                    break;
                case UPDATE_FINISH:
                    installApk(mApkPath);
//          dismiss();
                    break;
            }

        }
    };

    UpdateDialog(Context context, String apkPath, String pkgName) {
        super(context, R.style.BaseDialog);
        mContext = context;
        mApkPath = apkPath;
        mPkgName = pkgName;
    }

    UpdateDialog isForce(boolean isForce) {
        mIsForce = isForce;
        return this;
    }

    UpdateDialog setIconRes(int res) {
        mIconRes = res;
        return this;
    }

    UpdateDialog setOnSureListener(View.OnClickListener listener) {
        mSureListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        initView();
        initListener();

        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCancelable(!mIsForce);
        mTvCancel.setVisibility(mIsForce ? View.GONE : View.VISIBLE);
        mIvIcon.setImageResource(mIconRes);
    }

    private void initListener() {
        mTvCancel.setOnClickListener(v -> {
            dismiss();
        });
        mTvSure.setOnClickListener(mSureListener);
    }

    private void initView() {
        mIvIcon = findViewById(R.id.iv_icon);
        mTvUpdateTips = findViewById(R.id.tv_update_tips);
        mProgress = findViewById(R.id.pb);
        mTvProgressNum = findViewById(R.id.tv_pb_num);
        mLlAction = findViewById(R.id.ll_action);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvSure = findViewById(R.id.tv_sure);
    }

    @Override
    public void show() {
        if (mContext != null && !isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (mContext != null && isShowing()) {
            super.dismiss();
        }
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
        mHandler.sendEmptyMessage(UPDATE_FINISH);
    }

    public void installApk(final String path) {
        File apkFile = new File(path);
        if (!apkFile.exists()) {
            return;
        }
        Uri apkUri = FileProvider.getUriForFile(mContext, mPkgName, apkFile);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= 24) {//7.0+安装方式不同
            i.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            i.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(i);
    }

    private void changeMode(String mode) {
        switch (mode) {
            case MODE_ACTION:
                mLlAction.setVisibility(View.VISIBLE);
                mTvUpdateTips.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                mTvProgressNum.setVisibility(View.GONE);
                break;
            case MODE_UPDATING:
                mLlAction.setVisibility(View.GONE);
                mTvUpdateTips.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mTvProgressNum.setVisibility(View.VISIBLE);
                break;
        }
    }
}
