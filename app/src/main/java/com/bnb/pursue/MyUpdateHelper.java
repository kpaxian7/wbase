package com.bnb.pursue;

import android.content.Context;
import android.os.Environment;

import com.bnb.wbasemodule.update.AUpdateHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MyUpdateHelper extends AUpdateHelper {

    public MyUpdateHelper(Context context) {
        super(context);
    }

    @Override
    protected Call<ResponseBody> getUpdateCall() {
        return getUpdateRetrofit()
                .create(UpdateService.class)
                .update("/download/traveller.apk");
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
    protected String getBaseUrl() {
        return "http://www.8lala.top";
    }
}
