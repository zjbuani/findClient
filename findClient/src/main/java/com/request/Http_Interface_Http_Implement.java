package com.request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.fengxun.base.MyApplication;

import java.util.Map;

/**
 * Created by fengx on 2017/6/21.
 */

public abstract class Http_Interface_Http_Implement implements Http_URl {

    public static void loadPostSign(String url, Map<String, String> userMap, Response.Listener listener, Response.ErrorListener errorListener) {
    }

    public static void loadGetWeather(String url, Response.Listener<String> listener) {
    }

    public static void request(StringRequest stringRequest) {
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2, 1.0F)); // 设置超时  默认是2.5秒 重复请求的次数默认是1次   最后一个参数 超时时间因子:1f
        MyApplication.getHttpQueues().add(stringRequest);
    }

    ;


}
