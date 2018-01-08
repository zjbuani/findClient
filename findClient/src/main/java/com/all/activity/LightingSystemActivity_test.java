package com.all.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adapter.LightingSystemAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.LightBaseActivity;
import com.fengxun.base.MyApplication;
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
 * 照明系统
 * Created by fengx on 2017/9/19.
 */

public class LightingSystemActivity_test extends LightBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener, DrawerLayout.DrawerListener {

    private CustomViewPager mLight_pager;
    private RadioGroup mRg_group;
    //    private ViewPager mLight_pager;
    private ArrayList<BasePager> list;
    public SignInBean mSignInBean;
    public String mBuild_id;
    private TextView mTitle;
    private ImageView mHome_bl;
    private ImageView mHome_set;
    private SlidingMenu menu;
    private LightSystemRightFragment rightFragment;
    private FragmentManager fm;
    private Gson json = new Gson();

    public BuildingFloorBean bean;
    private LightHomeBean mHomeBean;
    private LightManBean mLightManBean;
    public LightSystemLineCharBean mLightSystemLineCharBean;
    private int groupPosition;
    private int childPosition;
    private boolean isleft;
    private String user_netlimit;
    private String build_peanut;

    public MapFloorBean mapFloorBean;
    public boolean isMapInfo;
    private PromptDialog promptDialog;

    @Override
    protected int initLayout() {
        return R.layout.light_fragment_layout;

    }

    @Override
    protected void initView() {
        openLoading(); //初始化dialog
        //用来预览就加载
        mLight_pager = (CustomViewPager) findViewById(R.id.light_pager);
//        mLight_pager = (ViewPager) findViewById(R.id.light_pager);
        mRg_group = (RadioGroup) findViewById(R.id.rg_group);
        //设置title
        mTitle = (TextView) findViewById(R.id.title_bar);
        //返回按钮
        mHome_bl = (ImageView) findViewById(R.id.home_bl);
        //查看全车库 按钮
        mHome_set = (ImageView) findViewById(R.id.all_home_set);
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
    protected void initData() {

        //给viewPager 添加内容
        list = new ArrayList<>();
        Intent intent = getIntent(); //获取到homeActivity intent
        isleft = intent.getBooleanExtra("isleft", false); //左侧菜单点击之后 选择的项目
        mSignInBean = (SignInBean) intent.getSerializableExtra(FragmentTag.SignInBean);
        mBuild_id = intent.getStringExtra(FragmentTag.LeftGroupChildItem);
        // radiobutton 的首页 内容
        mHomeBean = new LightHomeBean(this);
        //radiobutton的管理中心内容
        mLightManBean = new LightManBean(this);
        list.add(mHomeBean);
        list.add(mLightManBean);
        initHomeBeanDate(); //首页的内容请求
        initSlidingMenuLayoutData(mSignInBean, mBuild_id);
    }

    @Override
    protected void initListener() {
        mHome_set.setOnClickListener(this);
        mLight_pager.setScanScroll(false);  // 设置不可滑动
        mLight_pager.setOnPageChangeListener(this);
        mRg_group.setOnCheckedChangeListener(this);
        mHome_bl.setOnClickListener(this);

    }

    // 本 页面的 首页第一次加载的首页数据
    private void initHomeBeanDate() {
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
                Log.e("Testinit首次返回数据", o.toString());
                //图表数据和 设备总数
                mLightSystemLineCharBean = json.fromJson(o.toString(), LightSystemLineCharBean.class);
                if (mLightSystemLineCharBean.getResult() != 1) {
                    ToastShowUtil.showToast(LightingSystemActivity_test.this, "数据错误");
                    return;
                }
                initRequest();    //给右侧菜单的布局 添加数据

                Log.e("Testinit首次返回数据", mLightSystemLineCharBean.toString());
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

        RequstUtils.postBuildFloors(application.user_netlimit, application.build_peanut, map, new RightFragmentBody(), new RightFramentErr());

    }

    //初始化好右侧布局之后给 ExpandableListView 添加数据
    private void initSlidingMenuLayoutData(SignInBean mSignInBean, String mBuild_id) {
        rightFragment = new LightSystemRightFragment();
        requestLightList(mBuild_id, null); //请求  地图 需要加载的 url
    }


    /**
     * response project map info url
     *
     * @param mBuild_id request project Info
     * @param city
     */
    private void requestLightList(String mBuild_id, String city) {
        MyApplication application = (MyApplication) getApplication();
        RequstUtils.loadPostMapFloor(mBuild_id, city, new MapFloorResponseListener(), new MapFloorErrListener());
    }

    class MapFloorResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String o) {
            // 返回需要加载的 地图的URL 和  右边的滑轮 效果的数据
            mapFloorBean = json.fromJson(o.toString(), MapFloorBean.class);
            if (!mapFloorBean.getResult().equals("1")) {
                isMapInfo = false;
            } else {
                isMapInfo = true;
            }
        }
    }

    class MapFloorErrListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            isMapInfo = false;
        }
    }


    //右侧点击之后返回的groupPosition 和 childPosition
    @Override
    public void rightGroupItem(int groupPosition, int childPosition) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        rightMenuRequest();
    }

    /**
     * 打开loading
     */
    public void openLoading() {
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(3000);
        promptDialog.showLoading("正在加载");


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("onKey****Down", KeyEvent.KEYCODE_BACK + " ");
        promptDialog.dismissImmediately();
        finish();

//        return super.onKeyDown(keyCode, event);
        return true;
    }

    //查看全车库的一个按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_home_set: // 查看全车库按钮
                Log.e("是否执行此监听事件", "*****************************");
                menu.toggle();
                break;
            case R.id.home_bl:  // 返回按钮

                finish();
                break;
        }
    }

    //ViewPager的监听事件
    @Override
    public void onPageSelected(int position) {
        BasePager pager = list.get(position);
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

    // 右侧的fragment的 网络请求返回内容  请求分区和楼层信息
    class RightFragmentBody implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            promptDialog.showSuccess("加载成功");
            bean = json.fromJson(o.toString(), BuildingFloorBean.class);
            Log.e("第一次加载数据", bean.toString());
            if (bean.getResult() != 1) {
                ToastShowUtil.showToast(LightingSystemActivity_test.this, "数据请求错误");
            }
            if (bean != null) {  //绑定发送数据
                Bundle bundle = new Bundle();
                bundle.putSerializable(FragmentTag.RIGHT, bean);
                rightFragment.setArguments(bundle);
            }

            mLight_pager.setAdapter(new LightingSystemAdapter(list));  //设置 ViewPager 的内容
            fm.beginTransaction().replace(R.id.light_system_right_menu, rightFragment).commit();
        }
    }

    //右侧Fragment 数据 返回错误回调
    class RightFramentErr implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
        }
    }


    //ViewGroup的 监听事件
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                // 首页
                // mViewPager.setCurrentItem(0);
                mLight_pager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                mTitle.setText("照明系统");
                break;
            case R.id.rb_man:
                int currentItem = mLight_pager.getCurrentItem();

                // 管理中心
                mLight_pager.setCurrentItem(1, false);
                mTitle.setText("管理中心");
                break;
            default:
                break;
        }
    }

    //ViewPager的监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //ViewPager的监听事件
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void cancel() {   //取消titleBar
        cancelTitleBar();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left, R.anim.activity_right);  //activity的 关闭动画 右出左进
    }

    /*
         一下是 myDrawerLayout 监听事件
     */

    /**
     * 当抽屉滑动状态改变的时候被调用
     * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
     * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
     */
    @Override
    public void onDrawerStateChanged(int newState) {
        // TODO Auto-generated method stub
        Log.i("drawer1", "drawer的状态：" + newState);
    }

    /**
     * 当抽屉被滑动的时候调用此方法
     * arg1 表示 滑动的幅度（0-1）
     */
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // TODO Auto-generated method stub
        Log.i("drawer2", slideOffset + "");
    }

    /**
     * 当一个抽屉被完全打开的时候被调用
     */
    @Override
    public void onDrawerOpened(View drawerView) {
        // TODO Auto-generated method stub
        Log.i("drawer3", "抽屉被完全打开了！");
    }

    /**
     * 当一个抽屉完全关闭的时候调用此方法
     */
    @Override
    public void onDrawerClosed(View drawerView) {
        // TODO Auto-generated method stub
        Log.i("drawer4", "抽屉被完全关闭了！");
    }

}

