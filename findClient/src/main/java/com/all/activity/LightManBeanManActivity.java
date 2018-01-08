package com.all.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adapter.LightingSystemAdapter_Man;
import com.fengxun.base.BaseActivity;
import com.fragment.FragmentTag;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.pager.CustomViewPager;
import com.pager.lightsystem.man.BasePagerMan;
import com.pager.lightsystem.man.LightEnergyManBean;
import com.pager.lightsystem.man.LightHomeManBean;
import com.zhy.sample.folderlayout.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 照明管理中的  管理中心的 第一个界面
 */

public class LightManBeanManActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private RadioGroup mRg_group;
    private RadioButton mLight_management;
    private RadioButton mEnger_management;
    private ViewPager mViewPager;
    private ImageView mHome_bl;
    private BuildingFloorBean bean;
    private int right_childPosition;
    private int right_groupPosition;
    public LightSystemLineCharBean mLightSystemLineCharBean;

    @Override
    protected int initLayout() {
        return R.layout.light_system_man_layout;
    }

    @Override
    protected void initView() {
        // 照明管理按钮和  节能管理按钮的 父容器
        mRg_group = (RadioGroup) findViewById(R.id.system_rg_group);
        // 照明管理按钮
        mLight_management = (RadioButton) findViewById(R.id.system_light_management);
        //节能管理按钮
        mEnger_management = (RadioButton) findViewById(R.id.system_enger_management);
        //做为此界面的滑动 数据显示
        mViewPager = (ViewPager) findViewById(R.id.system_light_pager);
        //返回按钮
        mHome_bl = (ImageView) findViewById(R.id.home_bl);
    }

    @Override
    protected void initListener() {
        mRg_group.setOnCheckedChangeListener(this);
        mViewPager.addOnPageChangeListener(this);
        mHome_bl.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        ArrayList<BasePagerMan> list = new ArrayList<>();
        LightHomeManBean mLhmb = new LightHomeManBean(this); //照明管理界面
        LightEnergyManBean mLemb = new LightEnergyManBean(this); //节能管理界面
        Intent intent = getIntent();
        int mRadioButtonChild = intent.getIntExtra(FragmentTag.RADIOBUTTON1, -1);
        //获取到上个界面返回的右侧菜单的点击的groupPosition
        right_groupPosition = intent.getIntExtra(FragmentTag.GROUP_POSITION, -1);
        if (right_groupPosition != -1) {
            //获取到上个界面返回的右侧菜单的点击的整个右侧的responseBean
            bean = (BuildingFloorBean) intent.getSerializableExtra(FragmentTag.RIGHT_REQUEST);
            //返回分区的灯的 所有状态
            mLightSystemLineCharBean = (LightSystemLineCharBean) intent.getSerializableExtra(FragmentTag.CHILD_AREA);
            //获取到上个界面返回的右侧菜单的点击的整个右侧的分组内的childPosition
            right_childPosition = intent.getIntExtra(FragmentTag.CHILD_POSITION, -1);
            mLhmb.add(bean, right_groupPosition, right_childPosition);
            mLemb.add(bean, right_groupPosition, right_childPosition);

        }
        list.add(mLhmb);
        list.add(mLemb);
        LightingSystemAdapter_Man mLSA = new LightingSystemAdapter_Man(list);
        int count = mLSA.getCount();
        mViewPager.setAdapter(mLSA);
        if (mRadioButtonChild != -1) {
            mViewPager.setCurrentItem(mRadioButtonChild, false);
            if (mRadioButtonChild == 0) {
                mRg_group.check(R.id.system_light_management);
            } else {
                mRg_group.check(R.id.system_enger_management);
            }
        }

    }

    // 导航栏的监听事件
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.system_light_management:
                // 首页
                // mViewPager.setCurrentItem(0);
                mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                break;
            case R.id.system_enger_management:
                int currentItem = mViewPager.getCurrentItem();
                // 管理中心
                mViewPager.setCurrentItem(1, false);
                break;
            default:
                break;
        }
    }


    @Override
    public void onPageSelected(int position) {   //ViewPager 的 监听 滑动后  到哪个界面
        switch (position) {
            case 0:
                mRg_group.check(R.id.system_light_management);
                break;
            case 1:
                mRg_group.check(R.id.system_enger_management);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //返回按钮
    @Override
    public void onClick(View v) {
        finish();
    }
}
