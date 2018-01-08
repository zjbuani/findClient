package com.pager.lightsystem.man;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.all.activity.LightManBeanManActivity;
import com.all.activity.SetModeCustom;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.SetMode;
import com.http.response.bean.SetModeBean;
import com.request.RequstUtils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.MyDialog;
import com.view.ZProgressHUD;
import com.zhy.sample.folderlayout.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengx  管理中心中的  节能页面的中 javaBean
 */

public class LightEnergyManBean extends BasePagerMan implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView mCar_title;
    private RadioGroup mLight_grade;
    private RadioButton mLight_grade_child1;
    private RadioButton mLight_grade_child2;
    private RadioButton mLight_grade_child3;
    private RadioButton mLight_grade_child4;
    private Button mCustom;
    private BuildingFloorBean bean;
    private int rightGroupPosition;
    private int right_chilidPosition;
    private Gson json;
    private ZProgressHUD progressHUD;
    private MyDialog myDialog = MyDialog.showDialog(mActivity);

    public LightEnergyManBean(LightManBeanManActivity activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.light_man_energy_layout, null);
        json = new Gson();
        mCar_title = (TextView) view.findViewById(R.id.car_title);
        mLight_grade = (RadioGroup) view.findViewById(R.id.light_grade);
        mLight_grade_child1 = (RadioButton) view.findViewById(R.id.light_grade_child1);
        mLight_grade_child2 = (RadioButton) view.findViewById(R.id.light_grade_child2);
        mLight_grade_child3 = (RadioButton) view.findViewById(R.id.light_grade_child3);
        mLight_grade_child4 = (RadioButton) view.findViewById(R.id.light_grade_child4_custom_mode);
        mCustom = (Button) view.findViewById(R.id.light_grade_child_custom);

        return view;
    }

    @Override
    public void initData() {
        openLoading();
    }

    @Override
    protected void initListener() {
        mLight_grade.setOnCheckedChangeListener(this);
        mCustom.setOnClickListener(this);
    }

    @Override
    public void add(BuildingFloorBean bean, int right_groupPosition, int right_childPosition) {
        this.bean = bean;
        this.rightGroupPosition = right_groupPosition;
        this.right_chilidPosition = right_childPosition;
        Log.e("BuildingFloorBean", bean.toString());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        SetMode setMode = new SetMode();
        switch (checkedId) {
            case R.id.light_grade_child1:
                setMode.setEng("1");
                request(setMode);
                break;
            case R.id.light_grade_child2:
                setMode.setEng("2");
                request(setMode);
                break;
            case R.id.light_grade_child3:
                setMode.setEng("3");
                request(setMode);
                break;
            case R.id.light_grade_child4_custom_mode:
                setMode.setEng("4");
                request(setMode);
                break;
        }
    }

    protected void request(SetMode setMode) {
        HashMap<String, String> map = new HashMap<>();
        String build_id = mActivity.mLightSystemLineCharBean.getBuild_id();
        String mSaveuser = PrefUtils.getString(mActivity, "user", "");
        map.put("user_id", mSaveuser);
        map.put("build_id", build_id);
        if (0 == this.rightGroupPosition) { // 设置全车库
            map.put("che", "1");
        } else if (1 == this.rightGroupPosition) { //设置全楼层
            map.put("floor", bean.getFloor_list().get(this.right_chilidPosition).getFloor_num() + "");
        } else {  //设置分区
            map.put("ran", bean.getArea_list().get(right_chilidPosition).getArea_name());
        }
        map.put("set", json.toJson(setMode));
        MyApplication application = (MyApplication) mActivity.getApplication();
//        progressHUD.show(); //正在加载
        myDialog.show();
        RequstUtils.loadPostLightSetMode(application.user_netlimit, application.build_peanut, map, new LightEnergyManResponse(), new LightEnergyManErr());
    }

    class LightEnergyManResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            myDialog.dismiss();
            SetModeBean setModeBean = json.fromJson(o.toString(), SetModeBean.class);
            if (setModeBean.getResult()!=1){
                ToastShowUtil.showToast(mActivity,"设置失败");
            }else {
                ToastShowUtil.showToast(mActivity,"设置成功");
            }
        }
    }

    class LightEnergyManErr implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    //自定义编辑按钮
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, SetModeCustom.class);
        if (bean != null) {
            intent.putExtra(FragmentTag.RIGHT_REQUEST, bean);
            intent.putExtra(FragmentTag.GROUP_POSITION, rightGroupPosition);
            intent.putExtra(FragmentTag.CHILD_POSITION, right_chilidPosition);
            intent.putExtra(FragmentTag.CHILD_AREA, mActivity.mLightSystemLineCharBean);
        }
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right, R.anim.left);  // 关闭时的  Activity的过场动画  右进左出
    }

    public void openLoading() {
        progressHUD = ZProgressHUD.getInstance(mActivity);
        progressHUD.setMessage("加载中");
        progressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        //progressHUD.dismiss();关闭loading
    }
}
