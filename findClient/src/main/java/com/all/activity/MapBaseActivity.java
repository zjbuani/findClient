package com.all.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.fragment.FragmentTag;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.request.RequstUtils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 地图的base 类
 */

public abstract class MapBaseActivity extends AppCompatActivity {
    protected MapView mapView;
    public boolean isFirstRequest = false;
    protected boolean isOffHandler = false;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {   //这里做刷新请求
            int what = msg.what;
            switch (what) {
                case 1:    // 此处 刷新  摄像头 和 车位的 颜色
                    changeTheCamera();   // 请求摄像头的  颜色 列表
                    refreshCarState();   // 刷新 车位状态
                    mHandler.sendEmptyMessageDelayed(1, 5000);   // 5 秒钟 刷新一次
                    break;
            }
        }
    };

    protected void refreshCarState() {

    }
    //  摄像头的红绿变化
    protected void changeTheCamera() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToken();
        setContentView(initLayout());
        initView();
        initData(savedInstanceState);
        initListener();
    }

    protected abstract void initListener();

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    // 界面进入第一次的 数据
    protected abstract  void geiFirstDate();
    private void initToken() {
        //设置accessToken
        Mapbox.getInstance(this, getString(R.string.access_token));
        // Make us non-modal, so that others can receive touch events.
  /*      getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
  */
    }

    protected abstract int initLayout();
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (isOffHandler){
            mHandler.sendEmptyMessage(1);
            isOffHandler =false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (mHandler !=null) {
            mHandler.removeMessages(1);
            isOffHandler =true;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
