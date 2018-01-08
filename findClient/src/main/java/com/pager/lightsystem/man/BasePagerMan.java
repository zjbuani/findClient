package com.pager.lightsystem.man;

import android.view.View;

import com.all.activity.LightManBeanManActivity;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.SignInBean;

/**
 * 标签页的基类
 *
 */
public abstract class BasePagerMan  {

    public LightManBeanManActivity mActivity;
    public View mRootView;// 当前页面的布局对象

    public BasePagerMan(LightManBeanManActivity activity) {
        mActivity = activity;
        mRootView = initView();
        initListener();
    }

    protected   void initListener(){

    };
    protected  void request(String mBuild_id){}
    protected abstract View initView();


    // 初始化数据
    public void initData() {

    }
    public   void updateUI(int rightGroupPosition, int right_chilidPosition){};
    public abstract void add(BuildingFloorBean bean, int right_groupPosition, int right_childPosition );
}
