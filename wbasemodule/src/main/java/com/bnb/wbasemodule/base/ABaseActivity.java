package com.bnb.wbasemodule.base;

import android.content.Context;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bnb.wbasemodule.R;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class ABaseActivity extends AppCompatActivity {

  private CompositeDisposable mDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentId());
    initView();
    initListener();
    processLogic();
  }

  @Override
  protected void onResume() {
    super.onResume();
    hideSoftInputActivity();
  }

  protected void initView() {

  }

  protected void initListener() {

  }

  protected void processLogic() {

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

  abstract int getContentId();
}
