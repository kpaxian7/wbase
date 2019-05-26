package com.bnb.wbasemodule.http;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpUtil {
    public static final String MEDIATYPE_JSON = "application/json; charset=utf-8";

    /**
     * map转换为RequestBody
     */
    public static RequestBody map2RequestBody(Map map) {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MediaType.parse(MEDIATYPE_JSON),
                    new JSONObject(new Gson().toJson(map)).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    /**
     * 创建空的RequestBody
     *
     * @return
     */
    public static RequestBody getEmptyRequestBody() {
        return RequestBody.create(MediaType.parse(MEDIATYPE_JSON), "{}");
    }

}
