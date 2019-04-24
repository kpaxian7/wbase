package com.bnb.pursue;

import android.content.Context;

import com.bnb.wbasemodule.update.AUpdateDialog;
import com.bnb.wbasemodule.update.AUpdateHelper;

public class MyUpdateHelper extends AUpdateHelper {

    private Context mContext;

    public MyUpdateHelper(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected String getPkgName() {
        return "com.bnb.pursue";
    }

    @Override
    protected int getIconRes() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected String getDownloadUrl() {
        return "http://www.8lala.top/download/traveller.apk";
    }

//    @Override
//    protected AUpdateDialog initDialog() {
//        return new MyTestDialog(mContext, this);
//    }
}
