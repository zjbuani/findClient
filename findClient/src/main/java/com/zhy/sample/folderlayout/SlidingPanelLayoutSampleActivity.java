package com.zhy.sample.folderlayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.utils.DateUtils;
import com.google.gson.Gson;
import com.request.RequstUtils;
import com.weather.bean.Daily;
import com.weather.bean.JsonRootBean;
import com.weather.bean.Results;
import com.zhy.home.activity.Fragment;

import java.util.List;

public class SlidingPanelLayoutSampleActivity extends FragmentActivity {

    private TextView mWeather;
    private ImageView mWeather_image;
    private TextView mTemperature;
    private TextView mTemperature_interval;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_slidingpanellayout);
        initView();
        initData();
    }


    private void initView() {
        //显示天气
        mWeather = (TextView) findViewById(R.id.weather);
        //显示天气图片
        mWeather_image = (ImageView) findViewById(R.id.weather_image);
        //显示温度
        mTemperature = (TextView) findViewById(R.id.temperature);
        //显示温度区间
        mTemperature_interval = (TextView) findViewById(R.id.temperature_interval);


        FragmentManager mFragmentManager = getFragmentManager();
        //动态添加的Fragment
        Fragment fragment = new Fragment();


        //通过beginTransaction()获取fragmentTransaction
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        //向view中添加指定的Fragment.
        ft.add(R.id.ll, fragment);
        //更新完成后不要忘记了commit()哦;
        ft.commit();
        getWeather();
    }

    public void click(View view) {
        Toast.makeText(this, "别着急功能正在开发中。。。", Toast.LENGTH_SHORT).show();
    }

    private void initData() {

    }

    public void getWeather() {
        RequstUtils result = new RequstUtils();
        result.loadGetWeather(RequstUtils.WEATHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                JsonRootBean jsonRootBean = gson.fromJson(s, JsonRootBean.class);
                List<Results> results = jsonRootBean.getResults();
                Daily daily = results.get(0).getDaily().get(0);
                mWeather.setText(daily.getText_day()); //显示天气 mypading
                mTemperature_interval.setText(daily.getLow()+"~" +daily.getHigh());
                int high =Integer.parseInt( daily.getHigh());
                int low =Integer.parseInt( daily.getLow());

                DateUtils.setTime(daily,mWeather_image);
                low =(high - low)/2 + low;
                mTemperature.setText(low + "℃");

            }
        });
    }
}
