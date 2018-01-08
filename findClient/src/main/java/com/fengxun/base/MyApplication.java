package com.fengxun.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by beefind on 2017/3/8.
 */

public class MyApplication extends Application{
    private Context  context = this;
    public String user_netlimit = "1" ;
    public String build_peanut = "" ;
    public static RequestQueue queues;
    @Override
    public void onCreate() {
        super.onCreate();
        //创建默认的ImageLoader  配置参数
        ImageLoaderConfiguration config= ImageLoaderConfiguration.createDefault(this);//设置图片以如何的编码方式显示  ;
        //初始化ImageLosder并且使用以上默认配置
        ImageLoader.getInstance().init(config);

        queues = Volley.newRequestQueue(getApplicationContext());

    }

    public static RequestQueue getHttpQueues() {
        return queues;

    }

}
