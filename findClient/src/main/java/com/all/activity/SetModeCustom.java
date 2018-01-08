package com.all.activity;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fengxun.base.BaseActivity;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.SetMode;
import com.http.response.bean.SetModeBean;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.MyDialogNPV;
import com.zhy.sample.folderlayout.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.zhy.sample.folderlayout.R.id.time;

/**
 * Created  灯管理中的自定义 编辑按钮
 */

public class SetModeCustom extends BaseActivity implements View.OnClickListener {

    private TextView mClickable_tv_item1;
    private TextView mClickable_tv_item2;
    private ImageView mEdit_back;
    private MyDialogNPV mMyDialogNPV;
    private LightSystemLineCharBean mLightSystemLineCharBean;
    private BuildingFloorBean bean;
    private int rightGroupPosition;
    private int right_chilidPosition;
    private TextView mMnl_tv1;
    private TextView mMxl_tv1;
    private TextView mMnl_tv2;
    private TextView mMxl_tv2;
    private Button mMnl_subtract1, mMnl_add1, mMxl_subtract1, mMxl_add1, mMnl_subtract2, mMnl_add2, mMxl_subtract2, mMxl_add2;
    private int[] arr = {0, 1, 2, 3, 4, 5, 6, 7};
    private int index_mnl1 = 0;
    private int index_mnl2 = 0;
    private int index_mx1 = 0;
    private int index_mx2 = 0;
    private Button mOk;
    private String time = "";
    private Gson gson;

    @Override
    protected int initLayout() {
        return R.layout.custom_layout;
    }

    @Override
    protected void initView() {

        mClickable_tv_item1 = (TextView) findViewById(R.id.custom_clickable_itme1);   //开始时间1
        mClickable_tv_item2 = (TextView) findViewById(R.id.custom_clickable_itme2); //结束时间2
        mOk = (Button) findViewById(R.id.ok); //完成按钮 点击后 发送设置
        //车道照度1的 减号
        mMnl_subtract1 = (Button) findViewById(R.id.mnl_subtract1);
        //车道照度1的 /加号
        mMnl_add1 = (Button) findViewById(R.id.mnl_add1);

        //车位1 照度减号
        mMxl_subtract1 = (Button) findViewById(R.id.mxl_subtract1);
        //车位1 照度加号
        mMxl_add1 = (Button) findViewById(R.id.mxl_add1);

        //车道照度1的 减号
        mMnl_subtract2 = (Button) findViewById(R.id.mnl_subtract2);
        //车道照度1的 /加号
        mMnl_add2 = (Button) findViewById(R.id.mnl_add2);

        //车位1 照度减号
        mMxl_subtract2 = (Button) findViewById(R.id.mxl_subtract2);
        //车位1 照度加号
        mMxl_add2 = (Button) findViewById(R.id.mxl_add2);

        //车道照度1的档位
        mMnl_tv1 = (TextView) findViewById(R.id.mnl_tv1);
        //车位照度1的档位
        mMxl_tv1 = (TextView) findViewById(R.id.mxl_tv1);
        //车位照度2的档位
        mMnl_tv2 = (TextView) findViewById(R.id.mnl_tv2);
        //车位照度2的档位
        mMxl_tv2 = (TextView) findViewById(R.id.mxl2_tv);

        mEdit_back = (ImageView) findViewById(R.id.edit_back);  //设置返回按钮
    }

    @Override
    protected void initListener() {
        mClickable_tv_item1.setOnClickListener(this);
        mClickable_tv_item2.setOnClickListener(this);
        mEdit_back.setOnClickListener(this);
        mOk.setOnClickListener(this);  //发送保存设置
        mMnl_subtract1.setOnClickListener(this);
        mMnl_add1.setOnClickListener(this);
        mMxl_subtract1.setOnClickListener(this);
        mMxl_add1.setOnClickListener(this);
        mMnl_subtract2.setOnClickListener(this);
        mMnl_add2.setOnClickListener(this);
        mMxl_subtract2.setOnClickListener(this);
        mMxl_add2.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        rightGroupPosition = intent.getIntExtra(FragmentTag.GROUP_POSITION, -1);
        if (rightGroupPosition != -1) {
            right_chilidPosition = intent.getIntExtra(FragmentTag.CHILD_POSITION, -1);
            bean = (BuildingFloorBean) intent.getSerializableExtra(FragmentTag.RIGHT_REQUEST);
            mLightSystemLineCharBean = (LightSystemLineCharBean) intent.getSerializableExtra(FragmentTag.CHILD_AREA);
            updateUI();
        }
    }

    public void updateUI() {
        if (rightGroupPosition == 2) {
            LightSystemLineCharBean.ParJavaBean par = mLightSystemLineCharBean.getPar();
            String ntm = par.getNtm();
            String mnl = par.getMnl();
            String mxl = par.getMxl();
            String mnl2 = par.getMnl2();
            String mxl2 = par.getMxl2();
            mMnl_tv1.setText((mnl == null ? "0" : mnl)); //车道照度 1
            mMxl_tv1.setText((mxl == null ? "0" : mxl));  //车位照度 1
            mMnl_tv2.setText((mnl2 == null ? "0" : mnl2)); //车道照度 2
            mMxl_tv2.setText((mxl2 == null ? "0" : mxl2)); // //车位照度 2
            index_mnl1 = Integer.parseInt((mnl == null ? "0" : mnl));
            index_mx1 = Integer.parseInt((mxl == null ? "0" : mxl));
            index_mnl2 = Integer.parseInt((mnl2 == null ? "0" : mnl2));
            index_mx2 = Integer.parseInt((mxl2 == null ? "0" : mxl2));
            String time1 = ntm.substring(0, 4);
            String time2 = ntm.substring(4);
            time1 = formatTime(time1);
            time2 = formatTime(time2);
            time = ntm;

            mClickable_tv_item1.setText(time1 + " - -  " + time2);
            mClickable_tv_item2.setText(time2 + " - -  " + time1);
        }

    }

    private String formatTime(String time) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < time.length(); i++) {
            char charAt = time.charAt(i);
            if (i == 2) {
                sb.append(" : ");
                sb.append(charAt);
                continue;
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_clickable_itme1:  //设置开始时间
                showMyNPVDialog();
                break;
            case R.id.mnl_subtract1:  //车道照度1的 减号
                index_mnl1--;
                if (index_mnl1 <= 0) {
                    index_mnl1 = 0;
                }
                mMnl_tv1.setText(index_mnl1 + "");
                break;
            case R.id.mnl_add1:  //车道照度1的 加号
                index_mnl1++;
                if (index_mnl1 >= 8) {
                    index_mnl1 = 7;
                }
                mMnl_tv1.setText(index_mnl1 + "");
                break;
            case R.id.mxl_subtract1: //车位1 照度减号
                index_mx1--;
                if (index_mx1 <= 0) {
                    index_mx1 = 0;
                }
                mMxl_tv1.setText(index_mx1 + "");
                break;
            case R.id.mxl_add1: //车位1 照度减号
                index_mx1++;
                if (index_mx1 >= 8) {
                    index_mx1 = 7;
                }
                mMxl_tv1.setText(index_mx1 + "");

                break;
            case R.id.mnl_subtract2: //车道照度2的 减号
                index_mnl2--;
                if (index_mnl2 <= 0) {
                    index_mnl2 = 0;
                }
                mMnl_tv2.setText(index_mnl2 + "");
                break;
            case R.id.mnl_add2://车道照度2的 加号
                index_mnl2++;
                if (index_mnl2 >= 8) {
                    index_mnl2 = 7;
                }
                mMnl_tv2.setText(index_mnl2 + "");
                break;
            case R.id.mxl_subtract2: //车位2 照度减号
                index_mx2--;
                if (index_mx2 <= 0) {
                    index_mx2 = 0;
                }
                mMxl_tv2.setText(index_mx2 + "");
                break;
            case R.id.mxl_add2: //车位2 照度加号
                index_mx2++;
                if (index_mx2 >= 8) {
                    index_mx2 = 7;
                }
                mMxl_tv2.setText(index_mx2 + "");
                break;
            case R.id.ok: //提交请求
                Log.e("time",time);
                if ("--".equals(mClickable_tv_item1.getText().toString())){
                    ToastShowUtil.showToast(this,"请设置时间段");
                    return;
                }
                request();
                break;
            case R.id.edit_back: // 设置结束时间
                ToastShowUtil.showToast(this, "edit_back");
                finish();
                break;
        }
    }
    //保存发送
    private void request() {

      /*  ntm	否	设置灯的节能时段 开始 时 分；结束 时 分
        mnl	否	设置车道照度1的档位 （0-7档）
        mxl	否	设置车位照度1的档位 （0-7档）
        mnl2	否	设置车道照度2的档位 （0-7档）
        mxl2*/
        gson = new Gson();
        MyApplication application = (MyApplication) getApplication();
        SetMode setMode = new SetMode();
        setMode.setMnl(index_mnl1 + "");
        setMode.setMnl2(index_mnl2 + "");
        setMode.setMxl(index_mx1+ "");
        setMode.setMxl2(index_mx2 + "");
        setMode.setNtm(time);
        String key = gson.toJson(setMode);
        Map<String, String> map = new HashMap<>();
        String build_id = mLightSystemLineCharBean.getBuild_id();
        String mSaveuser = PrefUtils.getString(this, "user", "");
        map.put("user_id", mSaveuser);
        map.put("build_id", build_id);
        if (0 == this.rightGroupPosition) { // 设置全车库
            map.put("che", "1");
        } else if (1 == this.rightGroupPosition) { //设置全楼层
            map.put("floor", bean.getFloor_list().get(this.right_chilidPosition).getFloor_num() + "");
        } else {  //设置分区
            map.put("ran", bean.getArea_list().get(right_chilidPosition).getArea_name());
        }
        map.put("set", key);
        RequstUtils.loadPostLightSetMode(application.user_netlimit, application.build_peanut, map, new SetModeResponse(), null);
    }

    class SetModeResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            SetModeBean setModeBean = gson.fromJson(o.toString(), SetModeBean.class);
            if (setModeBean.getResult()!=1){
                ToastShowUtil.showToast(SetModeCustom.this,"设置失败");
            }else {
                ToastShowUtil.showToast(SetModeCustom.this,"设置成功");
            }
        }
    }

    private void showMyNPVDialog() {
        if (mMyDialogNPV == null) {
            mMyDialogNPV = new MyDialogNPV(this);
        }
        if (mMyDialogNPV.isShowing()) {
            mMyDialogNPV.dismiss();
        } else {
            mMyDialogNPV.setCancelable(true);
            mMyDialogNPV.setCanceledOnTouchOutside(true);
            mMyDialogNPV.show();
            // recommend initializing data (or setting certain data) of NumberPickView
            // every time setting up NumberPickerView,
            // and setting attr app:npv_RespondChangeOnDetached="false" to avoid NumberPickView
            // of responding onValueChanged callback if NumberPickerView detach from window
            // when it is scrolling
            mMyDialogNPV.initNPV();
        }
    }

    public void dialogCallBack(String time) {
        this.time = time;

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left, R.anim.activity_right);  //activity的 关闭动画 右出左进
    }
}
