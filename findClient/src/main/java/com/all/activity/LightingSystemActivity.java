package com.all.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.fengxun.base.BaseActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.utils.LineChartShow;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

/**
 * 照明系统
 * Created by fengx on 2017/9/19.
 */

public class LightingSystemActivity extends BaseActivity implements RadioButton.OnCheckedChangeListener {
    private LineChart mChart;
    private RadioButton mRbHome;
    private RadioButton mRb_man;

    @Override
    protected int initLayout() {
        return R.layout.light_system_layout;

    }

    @Override
    protected void initView() {
        mChart = (LineChart) findViewById(R.id.chart1);
        mRbHome = (RadioButton) findViewById(R.id.rb_home);
        mRb_man = (RadioButton) findViewById(R.id.rb_man);

        View mLight_fault = findViewById(R.id.fault); //

    }

    @Override
    protected void initListener() {
        mRbHome.setOnCheckedChangeListener(this);
        mRb_man.setOnCheckedChangeListener(this);

    }
    @Override
    protected void initData() {

//        LineChartShow.showChart(this,mChart,null);

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ToastShowUtil.showToast(this, " " + isChecked);
        Intent mIntent = null;
        switch (((RadioButton) buttonView).getId()) {
            case R.id.rb_man:  // 这里点击的 是 管理中心
                if (mIntent == null) {
                    mIntent = new Intent(this, ManageMent.class);
                    startActivity(mIntent);
                    finish();
                }
                ToastShowUtil.showToast(this, "rb_man" + "isChecked  " + isChecked);
                break;
            case R.id.rb_home:
                if (mIntent == null) {
                    mIntent = new Intent(this, LightingSystemActivity.class);
                }
                ToastShowUtil.showToast(this, "rb_home" + "isChecked  " + isChecked);
                break;
        }
    }



    @Override
    protected void cancel() {   //取消titleBar
        cancelTitleBar();
    }


}

