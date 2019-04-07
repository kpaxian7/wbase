package com.bnb.wbasemodule.base;

import android.os.Bundle;

public abstract class ABaseMvpActivity<P extends ABasePresenter> extends ABaseActivity {

  protected P mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mPresenter = createPresenter();
    if (mPresenter == null) {
      throw new IllegalArgumentException("Presenter == null");
    }
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onDestroy() {
    if (mPresenter != null) {
      mPresenter.release();
    }
    super.onDestroy();

  }

  protected abstract P createPresenter();
}
