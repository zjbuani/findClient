package com.all.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.fragment.LeftMenuFragment;
import com.google.gson.Gson;
import com.http.response.bean.SignInBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.request.RequstUtils;
import com.utils.ToastShowUtil;
import com.weather.bean.WeatherBean;
import com.zhy.sample.folderlayout.R;
import com.zhy.sample.folderlayout.RightMenuFragment;
import com.zhy.ui.UiUtils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by fengx on 2017/9/14.
 */

public class HomeActivity extends SlidingFragmentActivity implements View.OnClickListener {
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_RIGHT_MENU = "TAG_RIGHT_MENU";
    //左上角的 查看按钮
    private ImageView mHome_bl;
    //右上角的 设置按钮
    private ImageView mHome_set;
    private SlidingMenu slidingMenu;
    private ImageView mLightbtn;
    private ImageView mLightbtn1;
    private ImageView mCarbtn;
    private ImageView mEnvbtn;
    private ImageView mAdsbtn;
    private ImageView mWorkbtn;
    private ImageView mCarsharebtn;
    private SignInBean signInBean;
    private TextView mHome_title;
    private TextView mTemperature_home_section;
    private ImageView mWeather_home;
    private boolean isleft;
    private TextView mProject_info;
    private String mGroupItembuild_id;
    public SignInBean.BuildListJavaBean bean;
    public String mCity_id = "";

    private   int[] drawable = {
            R.drawable.a0,
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10,
            R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20,
            R.drawable.a21, R.drawable.a22, R.drawable.a23,  R.drawable.a24, R.drawable.a25, R.drawable.a26, R.drawable.a27, R.drawable.a28, R.drawable.a29,R.drawable.a30,
            R.drawable.a31, R.drawable.a32, R.drawable.a33, R.drawable.a34, R.drawable.a35, R.drawable.a36, R.drawable.a37, R.drawable.a38, R.drawable.a99,
    };

    protected int initLayout() {
        return R.layout.bolang;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initData();
        initListener();
        slidingMenu = getSlidingMenu();
        //占位
        setBehindContentView(R.layout.home_left_ragement);
       /* //
        slidingMenu.setSecondaryMenu(R.layout.right_menu);*/
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
        // slidingMenu.setBehindOffset(200);// 屏幕预留200像素宽度
        // 200/320 * 屏幕宽度
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width * 200 / 320);
        initFragment();

    }

    //关闭左菜单
    public void toggle() {
        slidingMenu.toggle();
    }

    //左侧Fragment 内的 GroupChildItem 的 回掉  返回之后更新天气
    public void leftGroupItem(SignInBean.BuildListJavaBean bean) {
        if (bean != null) {
            mGroupItembuild_id = bean.getBuild_id(); //全局来传递 给滑动的 几个 Activity 传递
            mHome_title.setText(bean.getBuild_name());
        }
        this.bean = bean;

        this.mCity_id = bean.getCity_id();    // 返回右侧的城市ID
        isleft = true;
        updateTitleAndWeather(bean);  // 返回之后更新 天气



    }


    /**
     * 获取登录界面 发送过来的 内容  并且来更新当前的 天气状况
     */
    private void initData() {
        signInBean = (SignInBean) getIntent().getSerializableExtra("SignInBean");
        updateTitleAndWeather(signInBean.getBuild_list().get(0));
    }

    /**
     * @param signInBean 更新当前的主页的 天气信息 默认使用登录返回的第一个 城市
     */
    public void updateTitleAndWeather(SignInBean.BuildListJavaBean signInBean) {
        updateTitle(signInBean);  //更新title
        String mCity_name = signInBean.getCity_name();
        if (mCity_name != null) {
            RequstUtils.loadGetWeather(mCity_name, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Gson gson = new Gson();
                    WeatherBean mWeatherBean = gson.fromJson(s, WeatherBean.class);
                    WeatherBean.ResultsJavaBean results = mWeatherBean.getResults().get(0);
//                    mHome_title.setText(results.getLocation().getName()); // 设置 当前的城市名  "℃"
                    updateWeather(results); //更新当前城市的天气
                }
            });
        }
    }
    //更新title
    private void updateTitle(SignInBean.BuildListJavaBean signInBean){
        mHome_title.setText(signInBean.getBuild_name());
    }
    //更新天气
     private void updateWeather(WeatherBean.ResultsJavaBean results){
         List<WeatherBean.ResultsJavaBean.DailyJavaBean> daily = results.getDaily();
         String code_day = daily.get(0).getCode_day();
         ViewGroup.LayoutParams params = mWeather_home.getLayoutParams();
         params.height = (int) (60 * UiUtils.getScreenDensity(HomeActivity.this));
         params.width = (int) (60 * UiUtils.getScreenDensity(HomeActivity.this));
         mWeather_home.setLayoutParams(params);
         mWeather_home.setImageDrawable(getResources().getDrawable(drawable[Integer.parseInt(code_day)])); //设置天气图片
         int high =Integer.parseInt(daily.get(0).getHigh());
         int low = Integer.parseInt(daily.get(0).getLow());
         int Project_info =   low + ((high - low)/2);
         mProject_info.setTextSize(25* UiUtils.getScreenDensity(HomeActivity.this));
         mProject_info.setText(Project_info +""  );
         mTemperature_home_section.setText(low + "℃ ～ "+ high +"℃");
     }
    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();// 开始事务
        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
        if (signInBean != null) {  //绑定发送数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("SignInBean", signInBean);
            leftMenuFragment.setArguments(bundle);
        }
        transaction.replace(R.id.fl_left_menu, leftMenuFragment,
                TAG_LEFT_MENU);// 用fragment替换帧布局;参1:帧布局容器的id;参2:是要替换的fragment;参3:标记
//        transaction.replace(R.id.fl_right_menu, new RightMenuFragment(), TAG_RIGHT_MENU);
        transaction.commit();// 提交事务
    }

    protected void initView() {
        mHome_bl = (ImageView) findViewById(R.id.home_bl);
        mHome_title = (TextView) findViewById(R.id.home_title);
        mHome_set = (ImageView) findViewById(R.id.home_set);
        mWeather_home = (ImageView) findViewById(R.id.weather_home); //天气显示
        mTemperature_home_section = (TextView) findViewById(R.id.temperature_home_section); //天气温度区间显示
        mProject_info = (TextView) findViewById(R.id.project_info);
        mLightbtn1 = (ImageView) findViewById(R.id.lightbtn);
        mCarbtn = (ImageView) findViewById(R.id.carbtn);
        mEnvbtn = (ImageView) findViewById(R.id.envbtn);
        mAdsbtn = (ImageView) findViewById(R.id.adsbtn);
        mWorkbtn = (ImageView) findViewById(R.id.workbtn);
        mCarsharebtn = (ImageView) findViewById(R.id.carsharebtn);

    }

    private void initListener() {
        mHome_bl.setOnClickListener(this);
        mHome_set.setOnClickListener(this);
        mLightbtn1.setOnClickListener(this);
        mCarbtn.setOnClickListener(this);
        mEnvbtn.setOnClickListener(this);
        mAdsbtn.setOnClickListener(this);
        mWorkbtn.setOnClickListener(this);
        mCarsharebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        MyApplication application = (MyApplication) getApplication();
        switch (view.getId()) {
            case R.id.home_bl:
                toggle();
                break;
            case R.id.home_set:
                intent = new Intent(this, HomeSettingActivity.class);

                if (isleft){  //说明是左边点击之后返回的对象
                    intent.putExtra("isleft",bean);
                    application.build_peanut = bean.getBuild_peanut();
                }
                intent.putExtra("SignInBean",signInBean);
                startActivity(intent);
                break;
            case R.id.lightbtn:
                //打开照明管理 界面
                intent = new Intent(HomeActivity.this, LightingSystemActivity_test.class);
                if (isleft){  //说明是左边点击之后返回的对象
                    intent.putExtra("isleft",true);
                }

                intent.putExtra(FragmentTag.SignInBean,signInBean);
                intent.putExtra(FragmentTag.LeftGroupChildItem,mGroupItembuild_id);
                startActivity(intent);
                break;
            case R.id.carbtn:
                intent = new Intent(HomeActivity.this, CarGuidanceSystemActivity.class); //引导系统模块
                if (isleft){  //说明是左边点击之后返回的对象
                    intent.putExtra("isleft",true);
                }
                intent.putExtra(FragmentTag.SignInBean,signInBean);
                intent.putExtra(FragmentTag.CITY,mCity_id);
                intent.putExtra(FragmentTag.LeftGroupChildItem,mGroupItembuild_id);
                startActivity(intent);
                break;
            case R.id.envbtn:
                intent = new Intent(HomeActivity.this, EnvironmentSystemActivity.class);
                startActivity(intent);

                break;
            case R.id.adsbtn:
                intent = new Intent(HomeActivity.this, AdvertisementSystemActivity.class);
                startActivity(intent);
                break;
            case R.id.workbtn:
                intent = new Intent(HomeActivity.this, StationSystemActivity.class);
                startActivity(intent);
                break;
            case R.id.carsharebtn:
                intent = new Intent(HomeActivity.this, CarPointSystemActivity.class);
                startActivity(intent);
                break;
        }
        overridePendingTransition(R.anim.right, R.anim.left);
    }

    /**
     * 按钮被按下
     */
    private final static float[] BUTTON_PRESSED = new float[]{
            2.0f, 0, 0, 0, -50,
            0, 2.0f, 0, 0, -50,
            0, 0, 2.0f, 0, -50,
            0, 0, 0, 5, 0};

    /**
     * 按钮恢复原状
     */
    private final static float[] BUTTON_RELEASED = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};

    private static final View.OnTouchListener touchListener = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_PRESSED));
                v.setBackgroundDrawable(v.getBackground());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_RELEASED));
                v.setBackgroundDrawable(v.getBackground());
            }
            return false;
        }
    };

    public static void setButtonStateChangeListener(ImageView v) {
        v.setOnTouchListener(touchListener);
    }


}

