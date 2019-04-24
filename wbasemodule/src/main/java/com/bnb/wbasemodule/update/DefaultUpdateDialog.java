package com.bnb.wbasemodule.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

public class DefaultUpdateDialog extends AUpdateDialog {

    private static final String MODE_UPDATING = "MODE_UPDATING";
    private static final String MODE_ACTION = "MODE_ACTION";

    private ImageView mIvIcon;
    private TextView mTvUpdateTitle;
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
    private String mStrUpdateTitle;
    private String mStrUpdateDesc;

    DefaultUpdateDialog(AUpdateHelper helper, Context context, String apkPath, String pkgName) {
        super(context, R.style.BaseDialog, helper);
        mContext = context;
        mApkPath = apkPath;
        mPkgName = pkgName;
    }

    DefaultUpdateDialog isForce(boolean isForce) {
        mIsForce = isForce;
        return this;
    }

    DefaultUpdateDialog setIconRes(int res) {
        mIconRes = res;
        return this;
    }

    DefaultUpdateDialog setUpdateTitle(String str) {
        mStrUpdateTitle = str;
        return this;
    }

    DefaultUpdateDialog setUpdateDesc(String str) {
        mStrUpdateDesc = str;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        initView();
        initListener();
        setValue();

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
        mTvSure.setOnClickListener(v -> {
            mHelper.doUpdate();
        });
    }

    private void initView() {
        mIvIcon = findViewById(R.id.iv_icon);
        mTvUpdateTitle = findViewById(R.id.tv_update_title);
        mTvUpdateTips = findViewById(R.id.tv_update_tips);
        mProgress = findViewById(R.id.pb);
        mTvProgressNum = findViewById(R.id.tv_pb_num);
        mLlAction = findViewById(R.id.ll_action);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvSure = findViewById(R.id.tv_sure);
    }

    private void setValue() {
        mTvUpdateTitle.setText(mStrUpdateTitle);
        mTvUpdateTips.setText(mStrUpdateDesc);
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
    protected void onUpdateStart() {
        mProgress.setProgress(0);
        changeMode(MODE_UPDATING);
    }

    @Override
    protected void onUpdateProgress(int progress) {
        mProgress.setProgress(progress);
        mTvProgressNum.setText(progress + "%");
    }

    @Override
    protected void onUpdateFinish() {
        installApk(mApkPath);
    }

    protected void installApk(final String path) {
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
