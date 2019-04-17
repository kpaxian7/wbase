package com.bnb.wbasemodule.base;

import com.bnb.wbasemodule.bean.BaseBean;

import io.reactivex.observers.DisposableObserver;

public class BaseObserver<T extends BaseBean> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
