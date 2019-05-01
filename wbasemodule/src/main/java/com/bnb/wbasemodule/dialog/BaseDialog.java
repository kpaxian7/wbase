package com.bnb.wbasemodule.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bnb.wbasemodule.R;

public class BaseDialog extends Dialog {

    private Context mContext;
    private Builder mBuilder;

    private TextView mTvTitle;
    private TextView mTvContent;
    private View mLine;
    private TextView mTvCancel;
    private TextView mTvSure;

    public BaseDialog(Context context, Builder builder) {
        super(context, R.style.BaseDialog);
        mContext = context;
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base);
        mTvTitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        mLine = findViewById(R.id.line);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvSure = findViewById(R.id.tv_sure);

        setupParams();
        initListener();

        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initListener() {
        mTvCancel.setOnClickListener(v -> {
            dismiss();
        });
        mTvSure.setOnClickListener(v -> {
            if (mBuilder.clickListener != null) {
                mBuilder.clickListener.onSure(this);
            } else {
                dismiss();
            }
        });
    }

    private void setupParams() {
        mTvTitle.setText(mBuilder.title);
        mTvContent.setText(mBuilder.content);
        mTvCancel.setText(mBuilder.cancel);
        mTvSure.setText(mBuilder.sure);
        setBold(mBuilder.isTitleBold, mTvTitle);
        setBold(mBuilder.isContentBold, mTvContent);

        mTvTitle.setVisibility(!TextUtils.isEmpty(mBuilder.title) ? View.VISIBLE : View.GONE);
        mTvContent.setVisibility(!TextUtils.isEmpty(mBuilder.content) ? View.VISIBLE : View.GONE);
        mTvCancel.setVisibility(mBuilder.isShowCancel ? View.VISIBLE : View.GONE);
        mLine.setVisibility(mBuilder.isShowCancel ? View.VISIBLE : View.GONE);
        setCancelable(mBuilder.isCancelable);
    }

    private void setBold(boolean isBold, TextView tv) {
        if (isBold) {
            TextPaint paint = tv.getPaint();
            paint.setFakeBoldText(true);
        }
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

    public static class Builder {

        Context context;
        OnSureClickListener clickListener;
        String title;
        String content;
        String cancel;
        String sure;
        boolean isTitleBold;
        boolean isContentBold;
        boolean isCancelable;
        boolean isShowCancel;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, OnSureClickListener clickListener) {
            this.context = context;
            this.clickListener = clickListener;
            title = context.getString(R.string.dialog_title);
            cancel = context.getString(R.string.cancel);
            sure = context.getString(R.string.sure);
            isTitleBold = true;
            isContentBold = false;
            isCancelable = true;
            isShowCancel = false;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder isTitleBold(boolean isTitleBold) {
            this.isTitleBold = isTitleBold;
            return this;
        }

        public Builder isContentBold(boolean isContentBold) {
            this.isContentBold = isContentBold;
            return this;
        }

        public Builder isCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public Builder isShowCancel(boolean isShowCancel) {
            this.isShowCancel = isShowCancel;
            return this;
        }

        public Builder setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setSure(String sure) {
            this.sure = sure;
            return this;
        }

        public BaseDialog build() {
            return new BaseDialog(context, this);
        }
    }

    public interface OnSureClickListener {
        void onSure(BaseDialog d);
    }
}
