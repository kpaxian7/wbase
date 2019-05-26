package com.bnb.wbasemodule.http;

import android.content.Context;

import com.bnb.wbasemodule.utils.AppUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreator {

    private static Context sContext;
    private static String sBaseUrl;

    private static Hashtable<String, Retrofit> sRetrofitMap = new Hashtable<>();
    private static OkHttpClient sOkhttpClient;

    public static void init(Context context, String baseUrl) {
        init(context, baseUrl, null);
    }

    public static void init(Context context, String baseUrl, OkHttpClient client) {
        sContext = context;
        sBaseUrl = baseUrl;
        sOkhttpClient = client == null ? getOkHttpClient() : client;
    }

    public static Retrofit getRetrofitInstance(OkHttpClient client) {
        return getRetrofitInstance(client, sBaseUrl);
    }

    public static Retrofit getRetrofitInstance(OkHttpClient client, String baseUrl) {
        Retrofit retrofit = sRetrofitMap.get(baseUrl);
        if (retrofit == null) {
            synchronized (RetrofitCreator.class) {
                retrofit = sRetrofitMap.get(baseUrl);
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .client(client == null ? sOkhttpClient : client)
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    sRetrofitMap.put(baseUrl, retrofit);
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        if (sOkhttpClient == null) {
            // OkHttp3
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
            if (AppUtils.isDebug()) {
                okHttpBuilder.addInterceptor(new LoggerInterceptor());
            }
            okHttpBuilder.retryOnConnectionFailure(false);
            try {
                final X509TrustManager trustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{trustManager}, new SecureRandom());
                SSLSocketFactory sslSocketFactory = sc.getSocketFactory();
                okHttpBuilder
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        .build();
            } catch (NoSuchAlgorithmException e) {

            } catch (KeyManagementException e) {

            }
        }
        return sOkhttpClient;
    }
}
