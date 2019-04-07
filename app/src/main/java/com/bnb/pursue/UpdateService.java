package com.bnb.pursue;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface UpdateService {

  @Streaming
  @GET
  Call<ResponseBody> update(@Url String url);
}
