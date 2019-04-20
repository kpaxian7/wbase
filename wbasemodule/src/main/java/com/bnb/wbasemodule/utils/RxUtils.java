package com.bnb.wbasemodule.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    public static <T> ObservableTransformer<T, T> getsubIoTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Disposable doOnMainThread(Runnable runnable) {
        return AndroidSchedulers.mainThread()
                .createWorker()
                .schedule(runnable);
    }

    public static Disposable doOnNewThread(Runnable runnable) {
        return Schedulers.newThread()
                .createWorker()
                .schedule(runnable);
    }

}
