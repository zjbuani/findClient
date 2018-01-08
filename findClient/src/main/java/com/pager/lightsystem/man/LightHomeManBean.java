package com.pager.lightsystem.man;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.all.activity.LightManBeanManActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.MyApplication;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.SetMode;
import com.http.response.bean.SetModeBean;
import com.http.response.bean.SignInBean;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.MyDialog;
import com.zhy.sample.folderlayout.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengx  照明系统中的 照明管理中的  照明数据  javaBean.
 */

public class LightHomeManBean extends BasePagerMan implements RadioGroup.OnCheckedChangeListener, RadioButton.OnCheckedChangeListener {

    private RadioGroup mManage;
    private RadioButton mManual_open;
    private RadioButton mManual_off;
    private RadioGroup mEnergy_pattern;
    private RadioButton mEnergy_pattern_button;
    private BuildingFloorBean bean;
    private int rightGroupPosition;
    private int right_chilidPosition;
    private TextView mTitle_name;

    private TextView mLight_mode;
    private TextView mDevice_num;
    private RadioGroup mLight_stall;  // green 档位RaidoGroup
    private RadioGroup mLight_stall_gone;  //gray 档位RaidoGroup
    private RadioGroup mLight_home_man_radioGroup;
    private RadioButton mStallNum1;
    private RadioButton mStallNum2;
    private RadioButton mStallNum3;
    private RadioButton mStallNum4;
    private RadioButton mStallNum5;
    private RadioButton mStallNum6;
    private RadioButton mStallNum7;
    private RadioButton mStallNum1_Gon;
    private RadioButton mStallNum2_Gon;
    private RadioButton mStallNum3_Gon;
    private RadioButton mStallNum4_Gon;
    private RadioButton mStallNum5_Gon;
    private RadioButton mStallNum6_Gon;
    private RadioButton mStallNum7_Gon;

    private View mGress_bar_green;
    private View mGress_bar_gray;
    private View mStall_gone_layout;
    private View mStall_green_layout;
    private ProgressBar mGreen_progress;
    private ProgressBar mGray_progress;
    private int mLva = 0; //档位控制
    private boolean isChecked = false; //刚进入的档位 radioButton 是可以点击的 设置成功后是不可点击
    private MyDialog myDialog = MyDialog.showDialog(mActivity);
    private RadioButton stallBt;
    private String lmd;
    private Gson json = new Gson();
    private int lvaint;

    public LightHomeManBean(LightManBeanManActivity activity) {
        super(activity);
    }



    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.light_home_man_layout, null);
        //设置当前页面的 titleName
        mTitle_name = (TextView) view.findViewById(R.id.title_name);
        //照明模式
        mLight_mode = (TextView) view.findViewById(R.id.light_mode);
        //照明设备总数
        mDevice_num = (TextView) view.findViewById(R.id.device_num);
        //手动照明管理 的 按钮父容器
        mManage = (RadioGroup) view.findViewById(R.id.light_home_man_radioGroup);
        //手动开启groupButton
        mLight_home_man_radioGroup = (RadioGroup) view.findViewById(R.id.light_home_man_radioGroup);
        //进度条 green
        mGress_bar_green = view.findViewById(R.id.gress_bar_green);
        //进度条 gray
        mGress_bar_gray = view.findViewById(R.id.gress_bar_gray);
        initLightGreenButton(view);
        initLightGrayButton(view);
        //手动开启按钮
        mManual_open = (RadioButton) view.findViewById(R.id.manual_open);
        //手动关闭按钮
        mManual_off = (RadioButton) view.findViewById(R.id.manual_off);
        //节能模式按钮的 父容器
        mEnergy_pattern = (RadioGroup) view.findViewById(R.id.energy_pattern);
        //节能模式按钮
        mEnergy_pattern_button = (RadioButton) view.findViewById(R.id.energy_pattern_button);
        return view;
    }

    @Override
    public void initData() {
       disableRadioGroup(mLight_stall_gone);
    }

    //灰色档位控件
    private void initLightGrayButton(View view) {
        //灰色档位group的布局
        mStall_gone_layout = view.findViewById(R.id.light_stall_gone_layout);
        //gray light的 照度 档位
        mLight_stall_gone = (RadioGroup) view.findViewById(R.id.light_stall_gone);
        //gray 照明档位1
        mStallNum1_Gon = (RadioButton) view.findViewById(R.id.num1_gone);
        //gray 照明档位2
        mStallNum2_Gon = (RadioButton) view.findViewById(R.id.num2_gone);
        //gray 照明档位3
        mStallNum3_Gon = (RadioButton) view.findViewById(R.id.num3_gone);
        //gray 照明档位4
        mStallNum4_Gon = (RadioButton) view.findViewById(R.id.num4_gone);
        //gray 照明档位5
        mStallNum5_Gon = (RadioButton) view.findViewById(R.id.num5_gone);
        //gray 照明档位6
        mStallNum6_Gon = (RadioButton) view.findViewById(R.id.num6_gone);
        //gray 照明档位7
        mStallNum7_Gon = (RadioButton) view.findViewById(R.id.num7_gone);
        //gray 进度条
        mGray_progress = (ProgressBar) view.findViewById(R.id.light_gray_progress);


    }

    //绿色档位控件
    private void initLightGreenButton(View view) {
        //绿色档位group的布局
        mStall_green_layout = view.findViewById(R.id.light_stall_layout);
        //green light的 照度 档位
        mLight_stall = (RadioGroup) view.findViewById(R.id.light_stall);
        //green 照明档位1
        mStallNum1 = (RadioButton) view.findViewById(R.id.num1);
        //green 照明档位2
        mStallNum2 = (RadioButton) view.findViewById(R.id.num2);
        //green 照明档位3
        mStallNum3 = (RadioButton) view.findViewById(R.id.num3);
        //green 照明档位4
        mStallNum4 = (RadioButton) view.findViewById(R.id.num4);
        //green 照明档位5
        mStallNum5 = (RadioButton) view.findViewById(R.id.num5);
        //green 照明档位6
        mStallNum6 = (RadioButton) view.findViewById(R.id.num6);
        //green 照明档位7
        mStallNum7 = (RadioButton) view.findViewById(R.id.num7);
        //green 进度条
        mGreen_progress = (ProgressBar) view.findViewById(R.id.light_green_progress);
    }


    //右侧菜单回调数据 接口
    @Override
    public void add(BuildingFloorBean bean, int right_groupPosition, int right_childPosition) {
        this.bean = bean;
        this.rightGroupPosition = right_groupPosition;
        this.right_chilidPosition = right_childPosition;
        Log.e("BuildingFloorBean", bean.toString());
        updateUI(rightGroupPosition, right_chilidPosition);
    }

    //右侧菜单回调  更新主页面
    @Override
    public void updateUI(int rightGroupPosition, int right_chilidPosition) {
        //此界面更新  全车库默认不可点击  楼层默认不可点击
        //且所有的档位是灰色且不可点击 进度条且为灰色 手动开启所有才可点击
        //节能模式档位和进度条不可点击和进度条
        LightSystemLineCharBean.ParJavaBean par = mActivity.mLightSystemLineCharBean.getPar();
        Log.e("updateUIpar",par.toString());
        //返回的照明模式
        lmd = par.getLmd();
        // 返回照明档位
        String lva = par.getLva();
        if (this.rightGroupPosition == 1) {  //按楼层
            mTitle_name.setText(bean.getFloor_list().get(this.right_chilidPosition).getFloor_name());
            updateGrayStall(lva, rightGroupPosition, right_chilidPosition);
        } else if (this.rightGroupPosition == 2) { // 照明模式是按分区来配置的
            String area_name = bean.getArea_list().get(this.right_chilidPosition).getArea_name();
            mTitle_name.setText(area_name);
            if ("1".equals(lmd)) {
                isChecked = true;
                mLight_mode.setText("手动开启"); //照明模式
                mManual_open.setChecked(true);
            } else if ("2".equals(lmd)) {
                mEnergy_pattern_button.setChecked(true);
                mLight_mode.setText("节能模式"); //照明模式
            } else {
                mManual_off.setChecked(true);
                mLight_mode.setText("手动关闭"); //照明模式
            }
            Log.e("updateUI", area_name + " " + par.getAll());
            updateGreenStall(lva, rightGroupPosition, right_chilidPosition);
        } else { //这里是全车库   全车库 默认是不可点击的 档位是不点击切档位更新 进度条为灰色 进度条的两端图标为灰色
            mTitle_name.setText("全车库");
            mLight_stall.setVisibility(View.GONE); //如果是全车库 就隐藏绿色档位和 绿色的进度条
            updateGrayStall(lva, rightGroupPosition, right_chilidPosition);
        }
        mDevice_num.setText(par.getAll() + "");
    }

    //更新  照明的档位 gon掉绿色
    private void updateGrayStall(String lva, int rightGroupPosition, int right_chilidPosition) {
        mLight_stall.setVisibility(View.GONE); //不管是全车库和楼层都给Gon掉绿色
        mGress_bar_green.setVisibility(View.GONE); // gone掉绿色
        for (int i = 0; i < mLight_stall_gone.getChildCount(); i++) {
            mLight_stall_gone.getChildAt(i).setEnabled(false);
        }
    }

    //更新 照明的档位  隐藏gray
    private void updateGreenStall(String lva, int rightGroupPosition, int right_chilidPosition) {
        String mSrcLva = "1234567";
        if (lva == null) {
            return;
        }
        if (!mSrcLva.contains(lva)) {
            ToastShowUtil.showToast(mActivity, "档位数据错误");
            return;
        }
        mStall_gone_layout.setVisibility(View.GONE);       //显示绿色档位 mStall_layout是RadioGroup gray 最外层的LinerLayout
        mLight_stall.setVisibility(View.VISIBLE);   //显示绿色档位 mStall_layout是RadioGroup green最外层的LinerLayout
        lvaint = Integer.parseInt(lva);     // 灯的 档位
        updateGreenRadioButtonChecked(mLight_stall, lvaint); //更新绿色档位
        mGress_bar_green.setVisibility(View.VISIBLE); //显示绿色进度条
        mGress_bar_gray.setVisibility(View.GONE);  //隐藏灰色进度条
        updateGreenProgressBar(mGreen_progress, lvaint);
    }

    //更新进度条
    private void updateGreenProgressBar(ProgressBar mGreen_progress, int lvaint) {
        mGreen_progress.setProgress(lvaint);
    }

    //用来循环改变 当前以外的所有的radioButton的 点击状态

    /**
     * @param mLight_stall 档位的外层的group
     * @param lva          返回的档位
     */
    public void updateGreenRadioButtonChecked(RadioGroup mLight_stall, int lva) {
        lva--;
        for (int i = 0; i < mLight_stall.getChildCount(); i++) {
            stallBt = (RadioButton) mLight_stall.getChildAt(i);
            if (i == lva) { //把不是 这个档位ID 的都给设置成不可点击并且换掉background
//                stallBt.setEnabled(false); //都设置成不能点击状态
                stallBt.setChecked(true); //都设置成 选中状态

            }
        }
        isChecked = false;
    }

    @Override
    protected void initListener() {
        mManage.setOnCheckedChangeListener(this);
        //手动开启关闭 组合
        mLight_home_man_radioGroup.setOnCheckedChangeListener(this);
        // light greenRadioGroup  的 7 个档位
        mLight_stall.setOnCheckedChangeListener(this);
        // light gray RadioGroup  的 7 个档位
        mLight_stall_gone.setOnCheckedChangeListener(this);
        mEnergy_pattern_button.setOnCheckedChangeListener(this);
    }



    //设置 手动开关的 监听事件
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        boolean checked = mEnergy_pattern_button.isChecked(); //每次进入首先查看 节能模式 的状态
        switch (checkedId) {
            case R.id.manual_open:   //手动打开按钮 需要把green progressBar显示 还有 green RadioButton
                //选中 手动打开还是手动关闭 都需要把节能模式的  选中状态恢复的到默认状态
                if (checked) {
                    mEnergy_pattern.clearCheck();
                }
                if (!isChecked) {
                    setProgressBarVisibility(true);
                    myDialog.show();
                    lmd = "1";
                    request();
                }
                break;
            case R.id.manual_off:    //手动关闭按钮  需要把gray progressBar显示 还有 gray RadioButton
                //选中 手动打开还是手动关闭 都需要把节能模式的 选中状态恢复的到默认状态
                if (checked) {
                    mEnergy_pattern.clearCheck();
                }
                if (!isChecked) {
                    setProgressBarVisibility(false);
                    myDialog.show();
                    lmd = "0";
                    request();
                }
                break;
            case R.id.num1:    //green radioButton 1
                mLva = 1;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num2:   //green radioButton 2
                mLva = 2;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num3:    //green radioButton 3
                mLva = 3;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num4:   //green radioButton 4
                mLva = 4;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num5:    //green radioButton 5
                mLva = 5;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num6:   //green radioButton 6
                mLva = 6;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num7:    //green radioButton 7
                mLva = 7;
                greenAndReayRequest(true, mLva);
                break;
            case R.id.num1_gone:    //gray radioButton 1
                mLva = 1;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num2_gone:    //gray radioButton 2
                mLva = 2;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num3_gone:    //gray radioButton 3
                mLva = 3;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num4_gone:   //gray radioButton 4
                mLva = 4;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num5_gone:   //gray radioButton 5
                mLva = 5;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num6_gone:   //gray radioButton 6
                mLva = 6;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num7_gone:    //gray radioButton 7
                mLva = 7;
                greenAndReayRequest(false, mLva);
                break;
            default:
                break;
        }
    }


    private void setProgressBarVisibility(boolean greenProgressBarBarVisibility) {
        if (greenProgressBarBarVisibility) {
            mLight_stall_gone.setVisibility(View.INVISIBLE); //隐藏灰色档位 RadioButton
            mLight_stall.setVisibility(View.VISIBLE);   //显示绿色档位 的RadioButton
            mGress_bar_gray.setVisibility(View.GONE); //隐藏灰色绿色 progressBar
            mGress_bar_green.setVisibility(View.VISIBLE); //显示绿色 progressBar
            mLight_mode.setText("手动开启"); //照明模式
        } else {   // 修改此处

            mLight_stall.setVisibility(View.INVISIBLE);     //隐藏绿色档位 的RadioButton
            mLight_stall_gone.setVisibility(View.VISIBLE); //显示灰色档位 RadioButton*/
            mGress_bar_green.setVisibility(View.GONE);   //隐藏绿色 progressBar
            mGress_bar_gray.setVisibility(View.VISIBLE); //显示灰色 progressBar
            mLight_mode.setText("手动关闭"); //照明模式
            mStall_gone_layout.setVisibility(View.VISIBLE);
            updateGreenProgressBar(mGray_progress,lvaint);
        }

    }

    //发送数据前 更新进度条
    private void greenAndReayRequest(boolean green, int lva) {
        if (green) {
            if (!isChecked) {
                mGreen_progress.setProgress(lva);
                myDialog.show();
                request();
            }
        } else {
            if (!isChecked) {
                mGray_progress.setProgress(lva);
                myDialog.show();
                request();
            }
        }
    }

    protected void request() {
        Map<String, String> map = new HashMap<>();
        String build_id = mActivity.mLightSystemLineCharBean.getBuild_id();
        String mSaveuser = PrefUtils.getString(mActivity, "user", "");
        map.put("user_id", mSaveuser);
        map.put("build_id", build_id);
        SetMode setMode = new SetMode();
        setMode.setLmd(lmd);
        if (0 == this.rightGroupPosition) { // 设置全车库
            map.put("che", "1");
        } else if (1 == this.rightGroupPosition) { //设置全楼层
            map.put("floor", bean.getFloor_list().get(this.right_chilidPosition).getFloor_num() + "");
        } else {  //设置分区
            setMode.setLva(mLva + "");
            map.put("ran", bean.getArea_list().get(right_chilidPosition).getArea_name());
        }
        map.put("set", json.toJson(setMode));
        MyApplication application = (MyApplication) mActivity.getApplication();

        RequstUtils.loadPostLightSetMode(application.user_netlimit,application.build_peanut, map, new SetModeResponse(), new SetModeErrResponse());

    }

    class SetModeResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            Log.e("LightHomeManBean", o.toString());
            SetModeBean setModeBean = json.fromJson(o.toString(), SetModeBean.class);
            if (setModeBean.getResult() == 1) {
                if (setModeBean.getSuccess()!=1){
                    ToastShowUtil.showToast(mActivity, "设置失败");
                }else {
                    updateStall();
                    ToastShowUtil.showToast(mActivity, "设置成功");
                }
            } else {
                updateStall();
                ToastShowUtil.showToast(mActivity, "设置失败");
            }
            myDialog.cancel();
        }
    }
   private void updateStall(){
       if ("0".equals(lmd)){
           mLight_mode.setText("手动关闭"); //照明模式
       }else if ("1".equals(lmd)){
           mLight_mode.setText("手动开启"); //照明模式
       }else {
           mLight_mode.setText("节能模式"); //照明模式
       }
   }

    class SetModeErrResponse implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            ToastShowUtil.showToast(mActivity, "数据加载失败");
        }
    }

    //节能模式  radioButton 的监听事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mManage.clearCheck();
        }
        if (!isChecked) {
            myDialog.show();
            lmd = "2";
            request();
        }
    }
    //使所有radioGroup 内的所有radioButton 不能点击
    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }
    //使所有radioGroup 内的所有radioButton 可以点击
    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }
}
