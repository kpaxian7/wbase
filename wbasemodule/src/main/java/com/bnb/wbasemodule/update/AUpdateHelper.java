package com.bnb.wbasemodule.update;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.bnb.wbasemodule.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public abstract class AUpdateHelper {

    private Context mContext;
    private String mPath;
    private String mBaseUrl;
    private String mUrlPath;
    private String mPkgName;
    private AUpdateDialog mDialog;

    private static int sBufferSize = 8192;

    public AUpdateHelper(Context context) {
        mContext = context;
        mPath = getFilePath();
        mPkgName = getPkgName();
        convertDownloadUrl(getDownloadUrl());
    }

    private void convertDownloadUrl(String downloadUrl) {
        HttpUrl httpUrl = HttpUrl.parse(downloadUrl);
        if (httpUrl != null) {
            String host = httpUrl.host();
            mBaseUrl = downloadUrl.substring(0, downloadUrl.indexOf(host) + host.length());
            mUrlPath = downloadUrl.substring(mBaseUrl.length());
        }
    }

    private Retrofit getUpdateRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    private Call<ResponseBody> getCall() {
        return getUpdateRetrofit()
                .create(IUpdateService.class)
                .update(mUrlPath);
    }

    public void startUpdate() {
        if (!TextUtils.isEmpty(mBaseUrl)) {
            mDialog = initDialog();
            mDialog.show();
        }
    }

    public void doUpdate() {
        getCall().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                writeResponseToDisk(mPath, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void writeResponseToDisk(String path, Response<ResponseBody> response) {
        writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength(), mDialog);
    }

    private void writeFileFromIS(File file, InputStream is, long totalLength, UpdateListener downloadListener) {
        //开始下载
        downloadListener.onStartDown();

        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onFail("createNewFile IOException");
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            downloadListener.onFail("IOException");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 需要自定义dialog，重写该方法，return一个AUpdateDialog子类
     */
    protected AUpdateDialog initDialog() {
        return new DefaultUpdateDialog(this, mContext, mPath, mPkgName)
                .setIconRes(getIconRes())
                .isForce(isForce())
                .setUpdateTitle(getUpdateTitle())
                .setUpdateDesc(getUpdateDesc());
    }

    private String getFilePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/" + getPkgName() + "/down/newVersion.apk";
    }

    protected String getUpdateTitle() {
        return mContext.getString(R.string.new_version_found);
    }

    protected String getUpdateDesc() {
        return mContext.getString(R.string.update_desc);
    }

    protected boolean isForce() {
        return true;
    }

    protected abstract String getPkgName();

    protected abstract int getIconRes();

    protected abstract String getDownloadUrl();
}
