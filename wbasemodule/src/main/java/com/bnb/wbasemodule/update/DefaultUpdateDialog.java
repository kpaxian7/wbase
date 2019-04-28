package com.bnb.wbasemodule.update;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bnb.wbasemodule.R;

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

    DefaultUpdateDialog(AUpdateHelper helper, Context context) {
        super(context, helper);
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
        setCancelable(!mHelper.isForce());
        mTvCancel.setVisibility(mHelper.isForce() ? View.GONE : View.VISIBLE);
        mIvIcon.setImageResource(mHelper.getIconRes());
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
        mTvUpdateTitle.setText(mHelper.getUpdateTitle());
        mTvUpdateTips.setText(mHelper.getUpdateDesc());
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
    protected void onUpdateFinish(String path) {
        super.onUpdateFinish(path);
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
