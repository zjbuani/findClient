package com.all.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adapter.LightingSystemAdapter;
import com.adapter.LightingSystemAdapter_Man;
import com.adapter.SumListViewBeaseAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.BaseActivity;
import com.fengxun.base.LightBaseActivity;
import com.fengxun.base.MyApplication;
import com.fragment.CarGuidanceRightFragment;
import com.fragment.CarListGuidanceRightFragment;
import com.fragment.FragmentTag;
import com.fragment.LightSystemRightFragment;
import com.google.gson.Gson;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.SignInBean;
import com.http.response.bean.SumLightListBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pager.lightsystem.man.BasePagerMan;
import com.pager.lightsystem.man.LightEnergyManBean;
import com.pager.lightsystem.man.LightHomeManBean;
import com.request.RequstUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.view.ZProgressHUD;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 车位引导  管理中的  管理中心的 第一个车位列表 界面
 */

public class CarPositionListManBeanManActivity extends LightBaseActivity implements OnLoadmoreListener, View.OnClickListener {
    protected int page = 1;
    protected int maxPage = 1;
//    private List<SumLightListBean> lists = new ArrayList<SumLightListBean>();
    private SumListViewBeaseAdapter adapter;
    private ListView listView;

    private String mBuild_id;
    private Gson json = new Gson();


    private ArrayList<List<SumLightListBean.LisJavaBean>> finallListData = new ArrayList<List<SumLightListBean.LisJavaBean>>();
    private ArrayList<SumLightListBean.LisJavaBean> list;
    private Map<Integer, Integer> allListSize = new HashMap<>();

    private Map<Integer, List<SumLightListBean.LisJavaBean>> allList = new HashMap<>();
    private String mSaveuser_id;
    private String TAG = "TAG";
    private String user_netlimit;
    private String build_peanut;

    private RefreshLayout refreshLayout;
    private ImageView mHome_bl;
    private ImageView mHome_set;
    private FragmentManager fm;
    private SlidingMenu menu;
    private ZProgressHUD progressHUD;
    private CarListGuidanceRightFragment rightFragment;
    private BuildingFloorBean bean;
    public int groupPosition;
    public int childPosition;

    @Override
    protected int initLayout() {
        return R.layout.car_position_listview_layout;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    handlerRefreshRequest(mBuild_id, maxPage);
                    mHandler.sendEmptyMessageDelayed(1, 5000);  //轮子启动  循环一直请求
                    break;
                case 2:
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void initView() {
        //总数列表
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        listView = (ListView) findViewById(R.id.listview);
        //black
        mHome_bl = (ImageView) findViewById(R.id.home_bl);
        //查看全车库 按钮
        mHome_set = (ImageView) findViewById(R.id.all_home_set);
        initSlidingMenuFragment();
        refreshLayout.setEnableRefresh(false); //禁止下拉刷新
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
        mHome_bl.setOnClickListener(this); //返回按钮
        mHome_set.setOnClickListener(this); //返回按钮
        refreshLayout.setOnLoadmoreListener(this);
    }

    //右侧菜单 返回会回调
    @Override
    public void rightGroupItem(int groupPosition, int childPosition) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        rightMenuRequest();
    }

    //上拉属性的监听事件
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshLayout.finishLoadmore();  //完成加载
        page++;
        refreshRequest(mBuild_id, page);
    }

    @Override
    protected void initData() { //数据更新
        TextView mTitle = (TextView) findViewById(R.id.title_bar);
        mTitle.setText("车位列表");
        openLoading(); //初始化dialog
        initSlidingMenuLayoutData();
        geiFirstDate();
    }

    //初始化好右侧布局之后给 ExpandableListView 添加数据
    private void initSlidingMenuLayoutData() {
        rightFragment = new CarListGuidanceRightFragment();
    }

    //网络请求右侧菜单的数据
    private void initRequest(BuildingFloorBean rightBean) {
        if (rightBean != null) {  //绑定发送数据
            Bundle bundle = new Bundle();
            bundle.putSerializable(FragmentTag.RIGHT, rightBean);
            rightFragment.setArguments(bundle);
        }
        fm.beginTransaction().replace(R.id.light_system_right_menu, rightFragment).commit();
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


            }
        });
    }


    //上拉加载获取数据
    private void refreshRequest(String mBuild_id, int page) {
        if (page < 0) {
            page = 1;
        }
        ArrayList<String> params = getArrayParams();
        String key = json.toJson(params);
        Map<String, String> maps = new HashMap<>();
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", page + "");
        maps.put("che", "1");
        maps.put("key", key);
        page++;
        RequstUtils.loadPostLightList(user_netlimit, build_peanut, maps, new RefrashResponse());

    }

    private void handlerRefreshRequest(String mBuild_id, int maxPage) {
        if (maxPage < 0) {
            maxPage = 0;
        }
        Log.e(TAG, "handlerRefresh " + maxPage);
        ArrayList<String> params = getArrayParams();
        String key = json.toJson(params);
        Map<String, String> maps = new HashMap<>();
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", maxPage + "");
        maps.put("che", "1");
        maps.put("key", key);
        RequstUtils.loadPostLightList(user_netlimit, build_peanut, maps, new HandlerRefrashResponse());
    }

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

    class FirstResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            SumLightListBean sumLightListBean = json.fromJson(o.toString(), SumLightListBean.class);
            if (sumLightListBean.getResult() != 1) {
                return;
            }
            Log.e("sumLightListBean", sumLightListBean.toString());

            list = new ArrayList<>();
            List<SumLightListBean.LisJavaBean> firstList = sumLightListBean.getLis();//数据返回的  列表参数
            allListSize.put(page, firstList.size());  //用来储存 第一次返回需要 刷新界面的所有数据的长度
            finallListData.add(firstList);
            for (int i = 0; i < finallListData.size(); i++) {
                list.addAll(finallListData.get(i));
            }
            Log.e(TAG, "firstList " + firstList.size());
            Log.e(TAG, "allListSize " + allListSize.toString());
            Log.e(TAG, "finallListData " + finallListData.size());
            adapter = new SumListViewBeaseAdapter(CarPositionListManBeanManActivity.this, list);
            listView.setAdapter(adapter);
            mHandler.sendEmptyMessageDelayed(1, 5000); //延迟发送 5 秒钟 发送
        }
    }

    class RefrashResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            SumLightListBean sumLightListBean = json.fromJson(o.toString(), SumLightListBean.class);
            if (sumLightListBean.getResult() != 1) {
                page--;
                return;
            }
            List<SumLightListBean.LisJavaBean> lis = sumLightListBean.getLis();//数据返回的  列表参数
            if (lis.size() == 0) { // 说明上拉请求返回的数据 是 0
                page--;   //减减之后说明是上一页  定时器再次请求就会有数据了
                maxPage = page;
            } else {
                maxPage = page;
                Log.e(TAG, "测试查看数据Pull " + maxPage + " " + page);
                Log.e(TAG, "测试查看数据Pull " + finallListData.size() + " ");
                Log.e(TAG, "测试查看数据Pull " + allListSize.toString() + " ");
                Integer integer = allListSize.get(page);
                Log.e(TAG, "测试查看数据PullInteger " + integer);
                if (integer == null) {
                    finallListData.add(lis);
                    list.clear();
                    Log.e("getCount", " finallListData.add(lis) " + lis.size());
                    for (int i = 0; i < finallListData.size(); i++) {
                        list.addAll(finallListData.get(i));
                    }
                    allListSize.put(maxPage, lis.size());
                } else {
                    Log.e(TAG, "测试查看数据Pull***" + page + ":::  " + maxPage + " finallListData " + finallListData.size());
                    finallListData.remove((maxPage - 1));
                    allListSize.put(maxPage, lis.size());

                }
                adapter.notifyDataSetChanged();
                int size = lis.size();
                Log.e(TAG, "*** 测试查看数据Pullsize*****" + size);

            }
        }
    }


    class HandlerRefrashResponse implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            maxPage--;
            Log.e(TAG, "maxPage--  " + maxPage);
            SumLightListBean sumLightListBean = json.fromJson(o.toString(), SumLightListBean.class);
            if (sumLightListBean.getResult() != 1) {
                maxPage++;
                page--;
                return;
            }
            if (0 == maxPage) { //说明是第一次添加数据
                maxPage = page;
            }
            Log.e(TAG, "HandlerRe maxPage page  " + maxPage + " " + page);
            Log.e(TAG, "HandlerRe finallListData " + finallListData.size() + " ");
            Log.e(TAG, "HandlerRe allListSize " + allListSize.toString() + " ");

            List<SumLightListBean.LisJavaBean> lis = sumLightListBean.getLis();  //数据返回的  列表参数
            Integer integer = allListSize.get(maxPage);
            Log.e(TAG, "HandlerRe  integer " + integer + " ");
            if (integer == null) {
                finallListData.add(lis);
                list.clear();
                for (int i = 0; i < finallListData.size(); i++) {
                    list.addAll(finallListData.get(i));
                }
                allListSize.put(maxPage, lis.size());
            } else {
                Log.e(TAG, "HandlerRe else " + page + ":::  " + maxPage);
                finallListData.remove((maxPage - 1));
                finallListData.add(lis);
                allListSize.put(maxPage, lis.size());
            }
            adapter.notifyDataSetChanged();
            int size = lis.size();
            Log.e(TAG, "***********************" + size + "   finallListData  " + finallListData.size());

        }
    }

    //进入界面第一次 数据添加内容
    private void requestLightList(String mBuild_id, int page) {
        ArrayList<String> params = getArrayParams();
        String key = json.toJson(params);
        final Map<String, String> maps = new HashMap<>();
        String mSaveuser_id = PrefUtils.getString(this, "user", "");
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", page + "");
        maps.put("che", "1");
        maps.put("key", key);
        MyApplication application = (MyApplication) getApplication();
        user_netlimit = application.user_netlimit;
        build_peanut = application.build_peanut;
        RequstUtils.loadPostLightList(user_netlimit, build_peanut, maps, new FirstResponse());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(1);
        mHandler = null;
    }

    //右侧菜单 楼层请求
    public void rightMenuFloorRequest(Map<String, String> maps, String mBuild_id) {
        ArrayList<String> pramas = getArrayParams();
        String key = json.toJson(pramas);
        String mSaveuser_id = PrefUtils.getString(this, "user", "");
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", page + "");
        maps.put("key", key);
        Log.e("SumActivity 数据发送之前", maps.toString());
        RequstUtils.loadPostLightHome(user_netlimit, build_peanut, maps, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.e("SumActivityFRequest ", o.toString());
                //图表数据和 设备总数
                SumLightListBean mSumRanBean = json.fromJson(o.toString(), SumLightListBean.class);
                Log.e("测试查看数据", mSumRanBean.toString());
                finallListData.clear();
                finallListData.add(mSumRanBean.getLis());
                adapter.notifyDataSetChanged();
            }
        });
    }

    //右侧菜单 分区请求
    public void rightMenuRanRequest(Map<String, String> maps, String mBuild_id) {
        ArrayList<String> pramas = getArrayParams();
//        List<SumLightListBean.LisJavaBean>
        String key = json.toJson(pramas);
        String mSaveuser_id = PrefUtils.getString(this, "user", "");
        maps.put("build_id", mBuild_id);
        maps.put("user_id", mSaveuser_id);
        maps.put("pag", page + "");
        maps.put("key", key);
        Log.e("SumActivity 数据发送之前", maps.toString());
        RequstUtils.loadPostLightHome(user_netlimit, build_peanut, maps, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.e("SumActivityRRequest ", o.toString());
                //图表数据和 设备总数
                SumLightListBean mSumRanBean = json.fromJson(o.toString(), SumLightListBean.class);
                Log.e("测试查看数据", mSumRanBean.toString());

                finallListData.clear();
                finallListData.add(mSumRanBean.getLis());
            }
        });
    }

    // 界面进入第一次的 数据
    private void geiFirstDate() {
        mSaveuser_id = PrefUtils.getString(this, "user", "");
        Intent intent = getIntent();
        //获取到此 开始时选择的 项目 ID
        mBuild_id = intent.getStringExtra(FragmentTag.SumListTag);
        int right_groupPosition = intent.getIntExtra(FragmentTag.GROUP_POSITION, -1);
        if (right_groupPosition != -1) {
            Map<String, String> maps = new HashMap<>();
            BuildingFloorBean bean = (BuildingFloorBean) intent.getSerializableExtra(FragmentTag.RIGHT_REQUEST);
            initRequest(bean);
            int childPosition = intent.getIntExtra(FragmentTag.CHILD_POSITION, -1);
            if (right_groupPosition == 1) {
                int floor_num = bean.getFloor_list().get(childPosition).getFloor_num();
                maps.put("floor", floor_num + "");
                rightMenuFloorRequest(maps, mBuild_id);
            } else if (right_groupPosition == 2) {
                LightSystemLineCharBean mLightSystemLineCharBean = (LightSystemLineCharBean) intent.getSerializableExtra(FragmentTag.CHILD_AREA);
                String area_name = bean.getArea_list().get(childPosition).getArea_name();
                maps.put("ran", area_name);
                ToastShowUtil.showToast(this, area_name + " 是不是一样 " + mLightSystemLineCharBean.getPar().getAll());
                rightMenuRanRequest(maps, mBuild_id);
            }
        } else {
            requestLightList(mBuild_id, page);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left, R.anim.activity_right);  //activity的 关闭动画 右出左进
    }

    public ArrayList<String> getArrayParams() {
        ArrayList<String> params = new ArrayList<String>(); //下拉列表的请求

        params.add("err");
        params.add("ftp");
        params.add("atp");
        params.add("lran");
        params.add("fmn");
        params.add("mac");
        params.add("lst");
        params.add("ert");
        params.add("lmd");
        params.add("ran");
        return params;
    }

    /**
     * 打开loading
     */
    public void openLoading() {
        progressHUD = ZProgressHUD.getInstance(this);
        progressHUD.setMessage("加载中");
        progressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        //progressHUD.dismiss();关闭loading
    }
}
