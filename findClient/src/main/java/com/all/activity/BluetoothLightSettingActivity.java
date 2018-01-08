package com.all.activity;

import android.view.animation.TranslateAnimation;

import com.fengxun.base.BaseAppCompatActivity;
import com.http.response.bean.LightBluetoothMode;
import com.imp.BluetoothDeviceTag;
import com.utils.DensityUtil;
import com.zhy.sample.folderlayout.R;

/**
 * 灯设置界面的 activity
 */

public class BluetoothLightSettingActivity extends BaseAppCompatActivity {

    private String mac;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_light_setting_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        //上界面
        mac = getIntent().getStringExtra(BluetoothDeviceTag.KEY_DATA);
    }

    protected void startPickerViewAnimation(){
        int i = DensityUtil.dip2px(this, 220);  // view 的高度
        TranslateAnimation animation = new TranslateAnimation(0, 0, -i, 0);
//                    TranslateAnimation animation = new TranslateAnimation(0, 0, -1060, 0);
        animation.setDuration(400);
//        animation.setRepeatCount(0);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
//        mMap_tile_pager.startAnimation(animation);//开始动画
    }
}
