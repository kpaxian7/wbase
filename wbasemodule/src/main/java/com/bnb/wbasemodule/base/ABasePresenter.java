package com.bnb.wbasemodule.base;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public abstract class ABasePresenter<V> implements IPresenter {

  protected Context mContext;
  protected V mView;
  protected CompositeDisposable mCompositeDisposable;//管理所有的订阅

  public ABasePresenter(V view, Context ctx) {
    mView = view;
    mContext = ctx;
  }

  /**
   * Presenter释放资源
   */
  @Override
  public void release() {
    if (mCompositeDisposable != null)
      mCompositeDisposable.clear();
    if (mView != null) {
      mView = null;
    }
    if (mContext != null) {
      mContext = null;
    }
  }

  protected <D> void addSubscription(Observable<D> observable, DisposableObserver<D> Observer) {
    if (mCompositeDisposable == null) {
      mCompositeDisposable = new CompositeDisposable();
    }
    mCompositeDisposable.add(observable.subscribeWith(Observer));
  }

  protected void addSubscription(DisposableObserver Observer) {
    if (mCompositeDisposable == null) {
      mCompositeDisposable = new CompositeDisposable();
    }
    mCompositeDisposable.add(Observer);
  }

}
