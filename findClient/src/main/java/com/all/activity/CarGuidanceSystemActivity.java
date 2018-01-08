package com.all.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adapter.CarLightingSystemAdapter;
import com.adapter.LightingSystemAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.LightBaseActivity;
import com.fengxun.base.MyApplication;
import com.fragment.CarGuidanceRightFragment;
import com.fragment.FragmentTag;
import com.fragment.LightSystemRightFragment;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.MapFloorBean;
import com.http.response.bean.SignInBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pager.BasePager;
import com.pager.CustomViewPager;
import com.pager.pager.bean.CarBasePager;
import com.pager.pager.bean.CarGuidanceLightHomeBean;
import com.pager.pager.bean.CarGuidanceLightManBean;
import com.pager.pager.bean.LightHomeBean;
import com.pager.pager.bean.LightManBean;
import com.request.RequstUtils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.ZProgressHUD;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * 车位引导系统
 * Created by fengx on 2017/9/19.
 */

public class CarGuidanceSystemActivity extends LightBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener, DrawerLayout.DrawerListener {

    private TextView mTitle;
    private ImageView mHome_bl;
    private ImageView mAll_home_set;
    private boolean isleft;
    public String mBuild_id;
    public BuildingFloorBean bean;
    public SignInBean mSignInBean;
    private CarGuidanceRightFragment rightFragment;
    public String build_peanut;
    public String user_netlimit;
    public LightSystemLineCharBean mLightSystemLineCharBean;
    private FragmentManager fm;
    private SlidingMenu menu;
    private Gson json;

    private CustomViewPager mLight_pager;
    private ArrayList<CarBasePager> list;
    private RadioGroup mRg_group;
    public int groupPosition;
    public int childPosition;
    private CarGuidanceLightHomeBean mHomeBean;
    private CarGuidanceLightManBean mLightManBean;
    public String mCity;
    public MapFloorBean mapFloorBean;   // 返回需要加载的 地图的URL 和  右边的滑轮 效果的数据
    public boolean isMapInfo = true;    // 需要加载 地图的url
    private PromptDialog promptDialog;

    @Override
    protected int initLayout() {
        return R.layout.car_position_fragment_layout;
    }

    @Override
    protected void initView() {
        // 设置title
        mTitle = (TextView) findViewById(R.id.title_bar);
        //返回按钮
        mHome_bl = (ImageView) findViewById(R.id.home_bl);
        //右侧菜单
        mAll_home_set = (ImageView) findViewById(R.id.all_home_set);
        mLight_pager = (CustomViewPager) findViewById(R.id.light_pager);
        mRg_group = (RadioGroup) findViewById(R.id.rg_group);
        initSlidingMenuFragment();
    }

    //初始化
    private void initSlidingMenuFragment() {
        fm = getSupportFragmentManager();
        menu = getSlidingMenu();
        //设置菜单方向
        menu.setMode(SlidingMenu.RIGHT);
        //设置滑动的范围
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        WindowManager wm = getWindowManager();
        //设置滑动的宽度
        int width = wm.getDefaultDisplay().getWidth();
        menu.setBehindOffset(width * 200 / 320);
        //设置淡入淡出的效果
        menu.setFadeEnabled(true);
        menu.setFadeDegree(0.4f);
        //附加到activity
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置右边的菜单 布局
        setBehindContentView(R.layout.light_system_right);
    }

    @Override
    protected void initListener() {
        mHome_bl.setOnClickListener(this);
        mLight_pager.setScanScroll(false);  // 设置不可滑动
        mLight_pager.setOnPageChangeListener(this);
        mRg_group.setOnCheckedChangeListener(this);
        mAll_home_set.setOnClickListener(this);

    }

    @Override
    public void rightGroupItem(int groupPosition, int childPosition) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        rightMenuRequest();
    }

    @Override
    protected void initData() {
        mTitle.setText("车位引导系统");
        openLoading(); //初始化dialog
        //给viewPager 添加内容
        list = new ArrayList<>();
        Intent intent = getIntent(); //获取到homeActivity intent
        //左侧菜单点击之后 选择的项目
        isleft = intent.getBooleanExtra("isleft", false);
        mSignInBean = (SignInBean) intent.getSerializableExtra(FragmentTag.SignInBean);

        mBuild_id = intent.getStringExtra(FragmentTag.LeftGroupChildItem);
        mCity = intent.getStringExtra(FragmentTag.CITY);  // 左侧菜单点击后 返回的 此城市ID
        // radiobutton 的首页 内容
        mHomeBean = new CarGuidanceLightHomeBean(this);
        //radiobutton的管理中心内容
        mLightManBean = new CarGuidanceLightManBean(this);
        list.add(mHomeBean);
        list.add(mLightManBean);
        initHomeBeanDate(); //首页的内容请求

        initSlidingMenuLayoutData(mSignInBean, mBuild_id);

    }

    //初始化好右侧布局之后给 ExpandableListView 添加数据
    private void initSlidingMenuLayoutData(SignInBean mSignInBean, String mBuild_id) {
        rightFragment = new CarGuidanceRightFragment();
    }

    // 本 页面的 首页第一次加载的首页数据
    private void initHomeBeanDate() {
        json = new Gson();
        Map<String, String> maps = new HashMap<>();
        ArrayList<String> params = new ArrayList<String>();
        params.add("all");
        params.add("ntm");
        String key = json.toJson(params);
        maps.put("che", 1 + "");
        maps.put("build_id", mBuild_id);
        maps.put("user_id", "beefind");
        maps.put("key", key);
        MyApplication application = (MyApplication) getApplication();
        Log.e("application::", ((application == null) ? false : application.user_netlimit) + "" + application.build_peanut);
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;



        RequstUtils.loadPostLightHome(user_netlimit, build_peanut, maps, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.e("TestinitCarGuidance", o.toString());
                //图表数据和 设备总数
                mLightSystemLineCharBean = json.fromJson(o.toString(), LightSystemLineCharBean.class);
                if (mLightSystemLineCharBean.getResult() != 1) {
                    ToastShowUtil.showToast(CarGuidanceSystemActivity.this, "数据错误");
                    return;
                }
                initRequest();      //给右侧菜单的布局 添加数据
                Log.e("TestinitCarGuidance", mLightSystemLineCharBean.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_bl:
                finish();
                break;
            case R.id.all_home_set:
                menu.toggle();
                break;
        }
    }

    //右侧菜单返回之后 更新首页的所有数据
    public void rightMenuRequest() {
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
        ArrayList<String> params = new ArrayList<String>();
        params.add("all");
        String key = json.toJson(params);
        maps.put("build_id", mBuild_id);
        maps.put("user_id", "beefind");
        maps.put("key", key);
        Log.e("Test右侧菜单数据发送之前", maps.toString());
        RequstUtils.loadPostLightHome(user_netlimit, build_peanut, maps, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                mLightSystemLineCharBean = json.fromJson(o.toString(), LightSystemLineCharBean.class);
                if (groupPosition == 1) {
                    mHomeBean.add(bean, groupPosition, childPosition, mBuild_id);
                    mLightManBean.add(bean, groupPosition, childPosition, mBuild_id);
                } else if (groupPosition == 2) {
                    mHomeBean.add(bean, groupPosition, childPosition, mBuild_id);
                    mLightManBean.add(bean, groupPosition, childPosition, mBuild_id);
                    Log.e("Test右侧分区请求数据", mLightSystemLineCharBean.toString());
                    Log.e("Testo", groupPosition + " " + childPosition + " \r\n " + bean.toString());
                } else if (groupPosition == 0) {
                    mHomeBean.add(bean, groupPosition, childPosition, mBuild_id);
                    mLightManBean.add(bean, groupPosition, childPosition, mBuild_id);
                }
                //图表数据和 设备总数
            }
        });
    }
    //网络请求右侧菜单的数据
    private void initRequest() {
        String mSaveuser = PrefUtils.getString(this, "user", "");
        Map<String, String> map = new HashMap<>();
        map.put("build_id", mBuild_id);
        map.put("user_id", mSaveuser);
        MyApplication application = (MyApplication) getApplication();
        RequstUtils.postBuildFloors(application.user_netlimit, application.build_peanut, map, new RightFragmentBody(), new  RightFramentErr());

    }

    // 右侧的fragment的 网络请求返回内容  请求分区和楼层信息
    class RightFragmentBody implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            bean = json.fromJson(o.toString(), BuildingFloorBean.class);
            Log.e("第一次加载数据", bean.toString());
            if (bean.getResult() != 1) {
                ToastShowUtil.showToast(CarGuidanceSystemActivity.this, "数据请求错误");
            }
            if (bean != null) {  //绑定发送数据
                Bundle bundle = new Bundle();
                bundle.putSerializable(FragmentTag.RIGHT, bean);
                rightFragment.setArguments(bundle);
            }
            mLight_pager.setAdapter(new CarLightingSystemAdapter(list));  //设置 ViewPager 的内容
            fm.beginTransaction().replace(R.id.light_system_right_menu, rightFragment).commit();
            initProjectInfoUrl();
        }


    }
    //请求需要加载 地图的 URL
    private void initProjectInfoUrl() {
        requestLightList(mBuild_id);
    }
    /**
     * response project map info url
     * @param mBuild_id request project Info
     */
    private void requestLightList(String mBuild_id) {
        RequstUtils.loadPostMapFloor(mBuild_id, null, new MapFloorResponseListener(), new MapFloorErrListener());
    }
    class MapFloorResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            promptDialog.showSuccess("加载成功");  //取消 加载动画
            // 返回需要加载的 地图的URL 和  右边的滑轮 效果的数据
            mapFloorBean = json.fromJson(o.toString(), MapFloorBean.class);
            String result = mapFloorBean.getResult();
            if (!"1".equals(result)){
                ToastShowUtil.showToast(CarGuidanceSystemActivity.this,mapFloorBean.getResult() + " 数据错误");
                isMapInfo = false;
            }
            Log.e("mapFloorBean",mapFloorBean.toString());
        }
    }
    class MapFloorErrListener implements Response.ErrorListener { @Override public void onErrorResponse(VolleyError volleyError) { } }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        CarBasePager pager = list.get(position);
        pager.initData();
        switch (position) {
            case 0:
                mRg_group.check(R.id.rb_home);
                break;
            case 1:
                mRg_group.check(R.id.rb_man);
                break;

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    //ViewGroup的 监听事件
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Log.e("positionmRg_group", "R.id.rb_home");
        switch (checkedId) {
            case R.id.rb_home:
                // 首页
                // mViewPager.setCurrentItem(0);
                mLight_pager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                mTitle.setText("引导系统");
                Log.e("positionmRg_group", "R.id.rb_home");
                break;
            case R.id.rb_man:
                // 管理中心
                mLight_pager.setCurrentItem(1, false);
                Log.e("positionmRg_group", "R.id.rb_man");
                mTitle.setText("管理中心");
                break;
            default:
                break;
        }

    }


    //右侧Fragment 数据 返回错误回调
    class RightFramentErr implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
        }
    }

    /**
     * 打开loading
     */
    public void openLoading() {
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(3000);
        promptDialog.showLoading("正在加载");

        //progressHUD.dismiss();关闭loading
    }

}
