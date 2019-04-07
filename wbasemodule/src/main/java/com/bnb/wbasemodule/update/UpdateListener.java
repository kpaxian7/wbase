package com.bnb.wbasemodule.update;

/**
 * Author by wzl, Date on 2019/4/3.
 */
public interface UpdateListener {
    void onStartDown();

    void onFail(String failMsg);

    void onProgress(int pro);

    void onFinish(String path);
}
