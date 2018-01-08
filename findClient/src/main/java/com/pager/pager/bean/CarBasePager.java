package com.pager.pager.bean;

import android.view.View;

import com.all.activity.CarGuidanceSystemActivity;
import com.all.activity.LightingSystemActivity_test;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.SignInBean;

/**
 * 标签页的基类
 *
 */
public abstract class CarBasePager implements View.OnClickListener{

    public CarGuidanceSystemActivity mActivity;
    public String mBuild_id;
    public View mRootView;// 当前页面的布局对象

    public CarBasePager(CarGuidanceSystemActivity activity) {
        mActivity = activity;
        mRootView = initView();
        initListener();
    }

    protected   void initListener(){

    };
    protected  void request(String mBuild_id, SignInBean mSignInBean){}
    protected abstract View initView();


    // 初始化数据
    public void initData() {

    }

    public abstract void add(BuildingFloorBean bean, int groupPosition, int childPosition, String mBuild_id );
}
