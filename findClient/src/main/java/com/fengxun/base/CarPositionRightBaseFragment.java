package com.fengxun.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.all.activity.CarGuidanceSystemActivity;



/**
 * 主界面所有的 Imageview 打开的 activity 的 布局上的 Fragment
 */

public abstract class CarPositionRightBaseFragment extends Fragment {
    protected CarGuidanceSystemActivity mContext;

    // Fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarGuidanceSystemActivity activity =(CarGuidanceSystemActivity)getActivity();
        mContext = activity ; // 获取当前fragment所依赖的activity

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListner();
        initData();
    }





    // 初始化布局, 必须由子类实现
    public abstract View initView();

    // 初始化数据, 必须由子类实现
    public void initData() {
    }
    // 设置监听
    protected void initListner() {
    }

}
