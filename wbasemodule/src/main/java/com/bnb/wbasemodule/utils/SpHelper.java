package com.bnb.wbasemodule.utils;

public class SpHelper {

    private static SpHelper sInstance;

    private SpHelper() {

    }

    public static SpHelper getInstance() {
        if (sInstance == null) {
            synchronized (SpHelper.class) {
                if (sInstance == null) {
                    sInstance = new SpHelper();
                }
            }
        }
        return sInstance;
    }

    public void saveStr() {

    }
}
