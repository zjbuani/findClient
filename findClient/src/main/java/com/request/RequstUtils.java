package com.request;

import android.util.Log;

import com.all.activity.AddWMSMapActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.fengxun.base.MyApplication;


import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

/**
 * Created by fengx on 2017/6/20.
 */

public class RequstUtils extends Http_Interface_Http_Implement {

    public static void loadPostLightSetMode(String user_netlimit, String peanut, final Map<String, String> userMap, Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = peanut;
        }
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.LIGHT_SET_MODE, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.putAll(userMap);
                return map;
            }
        };
        Log.e("loadPostLightSetMode", userMap.toString());
        request(stringRequest);
    }

    public static void loadPostSign(String url, final Map<String, String> userMap, Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.putAll(userMap);
                return map;
            }
        };
        Log.e("loadPostDateJson", stringRequest.toString());
        request(stringRequest);
    }

    //请求rightMenuFloorAndAreaInfo  楼层和分区信息
    public static void postBuildFloors(String user_netlimit, String peanut, final Map<String, String> userMap, Response.Listener listener, Response.ErrorListener errorListener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = peanut;
        }
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.BUILDFLOORS, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.putAll(userMap);
                return map;
            }
        };
        Log.e("loadPostDateJson", stringRequest.toString());
        request(stringRequest);
    }

    /**
     * url      照明系统中 首页请求 使用的url
     *
     * @param userMap  照明系统中 首页 post的数据
     * @param listener 服务器响应的回调
     */
    public static void loadPostLightHome(String user_netlimit, String peanut, final Map<String, String> userMap, Response.Listener listener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = peanut;
        }
        Log.e(" 数据发送之前 ", "loadPostLightHome " + user_netlimit + url + Http_URl.LIGHTSYSTEM);
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.LIGHTSYSTEM, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return userMap;
            }
        };
        Log.e("loadPostDateJsonH ", "loadPostLightHome " + userMap.toString());
        request(stringRequest);
    }

    /**
     * 照明系统中 首页请求 使用的url
     *
     * @param userMap  照明系统中 首页 post的数据
     * @param listener 服务器响应的回调
     */
    public static void loadPostLightList(String user_netlimit, String peanut, final Map<String, String> userMap, Response.Listener listener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = peanut;
        }
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.LIGHTLIST, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return userMap;
            }
        };
        request(stringRequest);
    }

    /**
     *  摄像头 列表
     * @param user_netlimit
     * @param peanut
     * @param userMap
     * @param listener
     */
    public static void loadPostCameraList(String user_netlimit, String peanut, final Map<String, String> userMap, Response.Listener listener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = peanut;
        }
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.CAMERA_LIST, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return userMap;
            }
        };
        request(stringRequest);
    }
    /**
     *  刷新车位  状态列表
     * @param userMap
     * @param listener
     */
    public static void loadPostRefreshCarState(final Map<String, String> userMap, Response.Listener listener) {

        StringRequest stringRequest = new StringRequest(Method.POST, MAP_URL + Http_URl.CAR_STATE, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return userMap;
            }
        };
        request(stringRequest);
    }

    /**
     * 照明系统中 首页请求 使用的url
     *
     * @param userMap  照明系统中 首页 post的数据
     * @param listener 服务器响应的回调
     */
    public static void loadPostinpoint(String url, final Map<String, String> userMap, Response.Listener listener) {
        StringRequest stringRequest = new StringRequest(Method.POST, url, listener, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return userMap;
            }
        };
        request(stringRequest);
    }


    public static void loadGetWeather(String cityName, Response.Listener<String> listener) {
        String weatherUrl = RequstUtils.WEATHERKEY + cityName + RequstUtils.WEATHER_AND;
        StringRequest request = new StringRequest(Request.Method.GET, weatherUrl, listener, null);
        //设置取消http请求标签 Activity的生命周期中的onStiop()中调用
        request.setTag("volleyget");
        request(request);
    }

    public static void loadPostMapInfo(String user_netlimit, String build_peanut, final String mBuild_id, final String cityName, Response.Listener<String> listener, Response.ErrorListener errlistener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = build_peanut;
        }
        Log.e("onResponse", url + Http_URl.GET_MAP_DEVICE_ERROR);
        StringRequest stringRequest = new StringRequest(Method.POST, url + Http_URl.GET_MAP_DEVICE_ERROR, listener, errlistener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
//                map.putAll(userMap);
                map.put("buildingID", mBuild_id);
                map.put("cityName", cityName);
                return map;
            }
        };
        request(stringRequest);
    }

    public static void loadPostMapFloor(final String mBuild_id, final String cityName, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Method.POST, Http_URl.FlOOR, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("buildingID", mBuild_id);
                return map;
            }
        };
        request(stringRequest);
    }

    /**
     * @param user_netlimit 区别是否支持外网
     * @param build_peanut  花生壳地址
     * @param map           需要请求的参数
     * @param listener      响应返回的 body
     * @param errorListener 错误响应的body
     */
    public static void loadPostMarkerLayoutData(String user_netlimit, String build_peanut, final HashMap<String, String> map, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = build_peanut;
        }

        StringRequest request = new StringRequest(Method.POST, url + GET_MAP_DEVICE_ERROR, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        request(request);
    }


    public static void loadPostCarPositionLayoutData(String user_netlimit, String build_peanut, final HashMap<String, String> map, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = build_peanut;
        }
        StringRequest request = new StringRequest(Method.POST, url + GET_MAP_DEVICE_ERROR, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };
        request(request);
    }

    /**
     * @param user_netlimit 区别是否支持外网
     * @param build_peanut  花生壳地址
     * @param map           post请求参数
     * @param listener      返回响应body
     * @param errorListener 错误响应的body
     */
    public static void loadPostMarkerErrList(String user_netlimit, String build_peanut, final HashMap<String, String> map, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = Http_URl.LOCAL_URI;
        if ("1".equals(user_netlimit)) { //内网访问
            url = Http_URl.LOCAL_URI;
        } else if ("2".equals(user_netlimit)) {  //这里是外网
            url = build_peanut;
        }
        Log.e("onResponse",map.toString());
        Log.e("onResponse",url + GET_MAP_DEVICE_COORDINATE);
        StringRequest request = new StringRequest(Method.POST, url + GET_MAP_DEVICE_COORDINATE, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        request(request);
    }
}
