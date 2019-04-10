package com.bnb.wbasemodule.base;

import android.content.Context;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bnb.wbasemodule.R;
import com.bnb.wbasemodule.utils.ReflectionUtils;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class ABaseActivity extends AppCompatActivity {

    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && !isSaveFragmentInstanceState()) {
            savedInstanceState.remove(ReflectionUtils.getFragmentsTag());
        }
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initView();
        initListener();
        processLogic();
    }

    protected void initView() {

    }

    protected void initListener() {

    }

    protected void processLogic() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftInputActivity();
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.clear();
        }
        super.onDestroy();
    }

    protected <T> void addSubscription(Observable<T> observable, DisposableObserver<T> observer) {
        addSubscription(observable.subscribeWith(observer));
    }

    protected void addSubscription(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    protected void hideSoftInputActivity() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 是否保存fragment状态
     */
    protected boolean isSaveFragmentInstanceState() {
        return false;
    }

    abstract int getContentId();
}
