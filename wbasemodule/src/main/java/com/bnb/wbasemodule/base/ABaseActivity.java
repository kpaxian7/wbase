package com.bnb.wbasemodule.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private View mFlLoading;

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

    /**
     * 布局中包含 R.id.fl_loading
     */
    protected void showLoadingView() {
        if (mFlLoading == null) {
            mFlLoading = findViewById(R.id.fl_loading);
        }
        mFlLoading.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingView() {
        mFlLoading.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.clear();
        }
        super.onDestroy();
    }

    /**
     * 是否保存fragment状态
     */
    protected boolean isSaveFragmentInstanceState() {
        return false;
    }

    protected abstract int getContentId();
}
