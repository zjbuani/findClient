package com.request;

/**
 * Created by fengx on 2017/6/20.
 */

public interface Http_URl {
    String MAP_URL = "http://139.196.5.153:8080";

    String LOCAL_URI = "http://192.168.0.100:8421";
    String REMOTE_URI = "http://1n7o321771.51mypc.cn:12804";
    String WEATHER = "https://api.seniverse.com/v3/weather/daily.json?key=g0np7ibg5obi2r0g&location=上海&language=zh-Hans&unit=c&start=0&days=1";

    String WEATHERKEY = "https://api.seniverse.com/v3/weather/daily.json?key=g0np7ibg5obi2r0g&location=";
    String WEATHER_AND = "&language=zh-Hans&unit=c&start=0&days=1";
    //用户登陆接口
    String SIGNIN = MAP_URL + "/gos/gosapp/login";
    //请求项目列表
    String BUILDLIST = "/buildingList";
    //请求楼层和分区数组
    String BUILDFLOORS = "/buildingFloors";
    //向服务器请求项目指定范围内的照明系统的总体参数
    String LIGHTSYSTEM = "/lightSystem";
    //向服务器请求项目指定范围内照明系统的设备参数列表
    String LIGHTLIST = "/lightList";
    String CAMERA_LIST = "/cameraList";
    //设置运行模式
    String LIGHT_SET_MODE = "/lightSetMode";

    String FlOOR = MAP_URL + "/mapserve/floor";

    String GET_MAP_DEVICE_ERROR = "/getMapDeviceError";

    String GET_MAP_DEVICE_COORDINATE = "/getDeviceCoordinate";//请求 此楼层 全部  设备坐标
    String POST_INPOINT = "/mapserve/carposinpoint";
    String CAR_STATE = "/mapserve/refreshCarstate";


}
