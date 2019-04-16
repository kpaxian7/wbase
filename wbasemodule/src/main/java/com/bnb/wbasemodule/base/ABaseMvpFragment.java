package com.bnb.wbasemodule.base;

import android.os.Bundle;

public abstract class ABaseMvpFragment<P extends IPresenter> extends ABaseFragment {

    protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter == null) {
            throw new IllegalArgumentException("Presenter == null");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();
}
