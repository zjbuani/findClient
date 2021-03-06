package com.pager.pager.bean;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.all.activity.LightManBeanManActivity;
import com.all.activity.LightingSystemActivity_test;
import com.all.activity.SingleOperationActivity;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.SignInBean;
import com.pager.BasePager;
import com.zhy.sample.folderlayout.R;

import java.util.List;

/**
 * Created by fengx  管理中心界面
 */

public class LightManBean extends BasePager {

    private TextView mLight_project_name;
    private ImageView mLight_management;
    private ImageView mEnger_management;
    private ImageView mSingle_operation;
    private BuildingFloorBean bean;
    private int groupPosition;
    private int childPosition;

    public LightManBean(LightingSystemActivity_test activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.light_manage_ment, null);

        //项目名称
        mLight_project_name = (TextView) view.findViewById(R.id.light_project_name);

        //照明管理
        mLight_management = (ImageView) view.findViewById(R.id.light_management);
        //节能管理
        mEnger_management = (ImageView) view.findViewById(R.id.enger_management);
        //单机操作
        mSingle_operation = (ImageView) view.findViewById(R.id.single_operation);
        return view;
    }

    @Override
    public void initData() {
        bean =mActivity.bean;  //获取 到上个界面的 RightMenu
        String mBuild_id = mActivity.mBuild_id;
        if (mBuild_id == null) {
            mLight_project_name.setText(mActivity.mSignInBean.getBuild_list().get(0).getBuild_name());
        } else {
            List<SignInBean.BuildListJavaBean> build_list = mActivity.mSignInBean.getBuild_list();
            for (int i = 0; i < build_list.size(); i++) {
                String build_id = build_list.get(i).getBuild_id();
                if (mBuild_id.equals(build_id)) {     //通过左边项目列表GroupChildItem 来和登录返回的登录对象比较 然后找到点击项目的 项目名称
                    mLight_project_name.setText(build_list.get(i).getBuild_name());
                }
            }
        }
    }

    //右侧侧拉菜单的数据返回
    @Override
    public void add(BuildingFloorBean bean, int groupPosition, int childPosition, String mBuild_id) {
        this.bean = bean;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        mActivity.mBuild_id = mBuild_id;

    }

    @Override
    protected void initListener() {
        mLight_management.setOnClickListener(this);
        mEnger_management.setOnClickListener(this);
        mSingle_operation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String mBuild_id = mActivity.mBuild_id; //主页面 返回的 Build_id
        switch (v.getId()) {
            case R.id.light_management: //照明管理
                intent = new Intent(mActivity, LightManBeanManActivity.class);  //  打开照明管理界面
                intent.putExtra(FragmentTag.RADIOBUTTON1,0);
                if (mBuild_id != null) {
                    intent.putExtra(FragmentTag.SumListTag, mActivity.mBuild_id);
                }
                if (bean != null) {
                    intent.putExtra(FragmentTag.RIGHT_REQUEST, bean);
                    intent.putExtra(FragmentTag.GROUP_POSITION, groupPosition);
                    intent.putExtra(FragmentTag.CHILD_POSITION, childPosition);
                    intent.putExtra(FragmentTag.CHILD_AREA, mActivity.mLightSystemLineCharBean);
                }
                mActivity.startActivity(intent);
                break;
            case R.id.enger_management: //节能管理
                intent = new Intent(mActivity, LightManBeanManActivity.class);  //  打开照明管理界面
                if (bean != null) {
                    intent.putExtra(FragmentTag.RIGHT_REQUEST, bean);
                    intent.putExtra(FragmentTag.GROUP_POSITION, groupPosition);
                    intent.putExtra(FragmentTag.CHILD_POSITION, childPosition);
                    intent.putExtra(FragmentTag.CHILD_AREA, mActivity.mLightSystemLineCharBean);

                }
                intent.putExtra(FragmentTag.RADIOBUTTON1,1);
                mActivity.startActivity(intent);
                break;
            case R.id.single_operation:  // 单机操作
                intent = new Intent(mActivity, SingleOperationActivity.class);
                intent.putExtra(FragmentTag.M_BUILD_ID, mActivity.mBuild_id);
                mActivity.startActivity(intent); //开启单机操作界面
                mActivity.overridePendingTransition(R.anim.right, R.anim.left);  // 关闭时的  Activity的过场动画  右进左出
                break;
        }

    }
}
