package com.bnb.pursue;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bnb.wbasemodule.update.AUpdateDialog;
import com.bnb.wbasemodule.update.AUpdateHelper;
import com.bnb.wbasemodule.utils.ToastUtils;

/**
 * Author by wzl, Date on 2019/4/24.
 */
public class MyTestDialog extends AUpdateDialog {

    private TextView mTvStart;
    private ProgressBar mPb;

    public MyTestDialog(Context context, AUpdateHelper helper) {
        super(context, R.style.myDialog, helper);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_test);
        mTvStart = findViewById(R.id.tv_start);
        mPb = findViewById(R.id.pb);
        mTvStart.setOnClickListener(v -> {
            mHelper.doUpdate();
        });
    }

    @Override
    protected void onUpdateStart() {
        ToastUtils.show("start");
    }

    @Override
    protected void onUpdateProgress(int progress) {
        mPb.setProgress(progress);
    }

}
