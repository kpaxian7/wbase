package com.bnb.wbasemodule.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bnb.wbasemodule.R;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public abstract class ABaseFragment extends Fragment {
    private CompositeDisposable mCompositeDisposable;
    protected Activity mActivity;
    protected Context mContext;
    protected View mRootView;
    private View mFlLoading;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideSoftInput();
        int contentId = getContentId();
        if (contentId != -1) {
            mRootView = inflater.inflate(contentId, null);
        }
        initView();
        initListener();
        processLogic();
        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideSoftInput();
        }
    }

    @Override
    public void onDestroyView() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroyView();
    }

    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = mActivity.getCurrentFocus();
            if (focusView != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 嵌套Fragment title
     *
     * @return
     */
    public int getTitle() {
        return -1;
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
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }


    protected <T extends View> T findViewById(@IdRes int viewId) {
        return mRootView.findViewById(viewId);
    }

    protected void initListener() {

    }

    protected void processLogic() {

    }

    protected abstract int getContentId();

    protected abstract void initView();


}
