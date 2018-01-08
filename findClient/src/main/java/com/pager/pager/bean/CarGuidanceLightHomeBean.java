package com.pager.pager.bean;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.all.activity.AddWMSMapActivity;
import com.all.activity.AddWMSMapActivity_Test;
import com.all.activity.CarGuidanceSystemActivity;
import com.all.activity.DeviceMapActivity;
import com.all.activity.MapActivity;
import com.all.activity.SumActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.MapFloorBean;
import com.http.response.bean.SignInBean;
import com.pager.BasePager;
import com.request.RequstUtils;
import com.utils.LineChartShow;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengx   车位 应道 首页 系统中的 首页.
 */

public class CarGuidanceLightHomeBean extends CarBasePager {

    private TextView mNormal_sum;
    private TextView mBad_sum;
    private TextView mEnergy;
    private TextView mTemperatur;
    private TextView mGears;
    private LineChart mLineChart;
    private TextView mLight_project_name;
    private ImageView mLhomelightx_button;
    private ImageView mLhomerr_button;
    private Gson json = new Gson();
    private BuildingFloorBean bean;
    private int groupPosition;
    private int childPosition;
    private View fault;
    private TextView mId_tv;
    private MapFloorBean mapFloorBean;

    public CarGuidanceLightHomeBean(CarGuidanceSystemActivity activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.light_system_layout, null);
        //照明系统的 title 布局 上的 返回按钮
        //HomeActivity点击的项目 之后再 照明管理中需要更新的控件内容
        mLight_project_name = (TextView) view.findViewById(R.id.light_project_name);
        //总按钮 列表
        mLhomelightx_button = (ImageView) view.findViewById(R.id.lhomelightx_button);
        // 错误列表按钮
        mLhomerr_button = (ImageView) view.findViewById(R.id.lhomerr_button);
        // 正常的显示数量
        mNormal_sum = (TextView) view.findViewById(R.id.normal_sum);
        // 故障的显示数量
        mBad_sum = (TextView) view.findViewById(R.id.bad_sum);
        //  显示 功耗
        mEnergy = (TextView) view.findViewById(R.id.energy);
        //  显示温度
        mTemperatur = (TextView) view.findViewById(R.id.temperatur);
        //显示摄像头 布局
        fault = view.findViewById(R.id.fault);
        //  显示档位
        mGears = (TextView) view.findViewById(R.id.gears);
        mId_tv = (TextView) view.findViewById(R.id.id_tv);
        // 统计表控件
        mLineChart = (LineChart) view.findViewById(R.id.chart1);
        return view;
    }

    @Override
    public void initData() {
        String mBuild_id = mActivity.mBuild_id;
        Log.e("mBuild_ _id", mBuild_id);
        if (mBuild_id == null) {
            mLight_project_name.setText(mActivity.mSignInBean.getBuild_list().get(0).getBuild_name());
            mBuild_id = mActivity.mSignInBean.getBuild_list().get(0).getBuild_id();
            mActivity.mBuild_id = mBuild_id; //修改默认的 项目ID  如果上个界面的项目ID 是空那么 就给一个登陆时候返回的项目列表的 第一个项目ID 做为默认项目ID
        } else {
            List<SignInBean.BuildListJavaBean> build_list = mActivity.mSignInBean.getBuild_list();
//            build_list.get(0).getSystem_verson()
            for (int i = 0; i < build_list.size(); i++) {
                SignInBean.BuildListJavaBean javaBean = build_list.get(i);
                String build_id = javaBean.getBuild_id();
                if (mBuild_id.equals(build_id)) {     //通过左边项目列表GroupChildItem 来和登录返回的登录对象比较 然后找到点击项目的 项目名称
                    String system_verson = javaBean.getSystem_verson();
                    if (system_verson == null) {
                        return;
                    }
                    double mIntVersion = Double.parseDouble(system_verson);
                    if (mIntVersion < 3) {
                        fault.setVisibility(View.GONE);
                        mId_tv.setVisibility(View.VISIBLE);
                    } else {
                        mId_tv.setVisibility(View.GONE);
                        fault.setVisibility(View.VISIBLE);
                    }
                    mLight_project_name.setText(build_list.get(i).getBuild_name());
                }
            }
        }
        request(mBuild_id, mActivity.mSignInBean); //请求首页 加载需要的内容
    }

    //右侧菜单 在主页面的 点击回调方法 用来请求全车库或者单个分区  主页面的 回调
    @Override
    public void add(BuildingFloorBean bean, int groupPosition, int childPosition, String mBuild_id) {
        this.bean = bean;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        if (mBuild_id == null) {
            return;
        }
        Map<String, String> maps = new HashMap<>();
        if (groupPosition == 1) { //请求楼层
            int floor_num = bean.getFloor_list().get(childPosition).getFloor_num();
            maps.put("floor", floor_num + "");
        } else if (groupPosition == 2) { //请求分区
            String area_name = bean.getArea_list().get(childPosition).getArea_name();
            maps.put("ran", area_name + "");
        } else if (groupPosition == 0) {//请求全车库
            maps.put("che", 1 + "");
        }
        rightMenuRequest(maps, mBuild_id);
    }

    //右侧菜单 回到网络请求
    public void rightMenuRequest(Map<String, String> maps, String mBuild_id) {
        ArrayList<String> params = new ArrayList<String>();
        params.add("all");
        String key = json.toJson(params);
        maps.put("build_id", mBuild_id);
        maps.put("user_id", "beefind");
        maps.put("key", key);
        updateHomeView(mActivity.mLightSystemLineCharBean); // 更新主 界面的View
    }

    @Override   //主界面 第一次进入之后的 加载首页数据
    protected void request(String mBuild_id, SignInBean mSignInBean) {
        if (mBuild_id == null) {
            mBuild_id = mSignInBean.getBuild_list().get(0).getBuild_id();
        }
        super.mBuild_id = mBuild_id; //首次请求记录下次项目的Build_id
        ArrayList<String> params = new ArrayList<String>();
        params.add("all");
        String key = json.toJson(params);
        Map<String, String> maps = new HashMap<>();
        maps.put("build_id", mBuild_id);
        maps.put("user_id", "beefind");
        maps.put("che", "1");
        maps.put("key", key);
        Log.e("requestHome", maps.toString());
        updateHomeView(mActivity.mLightSystemLineCharBean); // 更新主 界面的View
    }
    private void updateHomeView(LightSystemLineCharBean mLightSystemLineCharBean) {
        if (mLightSystemLineCharBean == null) {
            return;
        }
        if (mLightSystemLineCharBean.getResult() != 1) {
            return;
        }
        LightSystemLineCharBean.ParJavaBean mPar = mLightSystemLineCharBean.getPar();
        MyApplication application = (MyApplication) mActivity.getApplication();
        if ("3".equals(application.user_netlimit)) {
            mNormal_sum.setText(mPar.getAll()); //更新设置总数
            mBad_sum.setText(mPar.getBad()); //更新设备故障数
            mEnergy.setText(mPar.getWat()); //更新功耗
            mTemperatur.setText(mPar.getAtp()); //更新温度
            mGears.setText(mPar.getAlm() + " W/h"); //更新档位
            //最后一个参数 是默认 点击的   radioButton 中的第一个( 功耗 温度 档位 )
            LineChartShow.showChart(mActivity, mLineChart, mLightSystemLineCharBean, 0);
        } else {
            mNormal_sum.setText(mPar.getAll()); //更新设置总数
            mBad_sum.setText(mPar.getBad()); //更新设备故障数
            mEnergy.setText(mPar.getWat()); //更新功耗
            mTemperatur.setText(mPar.getAtp()); //更新温度
            mGears.setText(mPar.getAlm() + " W/h"); //更新档位
            //最后一个参数 是默认 点击的   radioButton 中的第一个( 功耗 温度 档位 )
            LineChartShow.showChart(mActivity, mLineChart, mLightSystemLineCharBean, 0);
        }
        requestLightList(mBuild_id,null);
    }

    @Override
    protected void initListener() {
        mLhomelightx_button.setOnClickListener(this);
        mLhomerr_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.lhomelightx_button:
//                intent = new Intent(mActivity, SumActivity.class);  //所有灯的  列表查看界面
                intent = new Intent(mActivity, DeviceMapActivity.class);  //所有灯的 地图界面
                intent.putExtra(FragmentTag.MAP_URL, mActivity.mapFloorBean);
                String mBuild_id = mActivity.mBuild_id;
                if (mBuild_id != null) {
                    intent.putExtra(FragmentTag.SumListTag, mActivity.mBuild_id);
                }
                if (mActivity.bean != null) {
                    intent.putExtra(FragmentTag.RIGHT_REQUEST, mActivity.bean);
                }
                mActivity.startActivity(intent);
                break;
            case R.id.lhomerr_button:
//                intent = new Intent(mActivity, AddWMSMapActivity.class);  //查看故障列表 地图界面
                intent = new Intent(mActivity, AddWMSMapActivity_Test.class);  //查看故障列表 地图界面
                intent.putExtra(FragmentTag.MAP_URL, mActivity.mapFloorBean);
                String mBuild_id1 = mActivity.mBuild_id;
                if (mBuild_id1 != null) {
                    intent.putExtra(FragmentTag.SumListTag, mActivity.mBuild_id);
                }
                if (mActivity.bean != null) {
                    intent.putExtra(FragmentTag.RIGHT_REQUEST, mActivity.bean);
                    intent.putExtra(FragmentTag.GROUP_POSITION, groupPosition);
                    intent.putExtra(FragmentTag.CHILD_POSITION, childPosition);
                }
                Log.e("why", "开启服务");
                if (mActivity.isMapInfo){
                    mActivity.startActivity(intent);
                }else {
                    ToastShowUtil.showToast(mActivity.getApplication(),"地图数据错误" + mActivity.isMapInfo);
                }
                break;
            default:
                break;
        }

    }

    /**
     * response project map info url
     * @param mBuild_id request project Info
     * @param city
     */
    private void requestLightList(String mBuild_id, String city) {
        MyApplication application = (MyApplication) mActivity.getApplication();
        RequstUtils.loadPostMapFloor(mBuild_id, city, new MapFloorResponseListener(), new MapFloorErrListener());

    }

    class MapFloorResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            // 返回需要加载的 地图的URL 和  右边的滑轮 效果的数据
            mapFloorBean = json.fromJson(o.toString(), MapFloorBean.class);
        }
    }
    class MapFloorErrListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
        }
    }

    //设置radioButton 的背景
    private void setRadioButtonBackground(RadioGroup group, @IdRes int checkedId) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton view = (RadioButton) group.getChildAt(i);
            int id = view.getId();
            if (id != checkedId) { //不等于的都设置 换背景为 green
                view.setBackground(mActivity.getResources().getDrawable(R.drawable.light_sha_dow_button_pressed_false));
            } else {
                view.setBackground(mActivity.getResources().getDrawable(R.drawable.light_sha_dow_button_pressed_true));
            }
        }
    }


}
