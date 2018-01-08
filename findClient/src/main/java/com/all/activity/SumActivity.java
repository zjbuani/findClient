package com.all.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.SumListViewBeaseAdapter;
import com.android.volley.Response;
import com.fengxun.base.BaseActivity;
import com.fengxun.base.MyApplication;
import com.fragment.FragmentTag;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.LightSystemLineCharBean;
import com.http.response.bean.SumFloorBean;
import com.http.response.bean.SumLightListBean;
import com.http.response.bean.SumRanBean;
import com.request.Http_URl;
import com.request.RequstUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created  查看设备总数 界面   此界面有  定时分页刷新功能
 */

public class SumActivity extends BaseActivity implements OnLoadmoreListener {


    protected int page = 1;
    protected int maxPage = 1;
    private List<SumLightListBean> lists = new ArrayList<SumLightListBean>();
    private SumListViewBeaseAdapter adapter;
    private ListView listView;

    private String mBuild_id;
    private Gson json = new Gson();


    private ArrayList<List<SumLightListBean.LisJavaBean>> finallListData = new ArrayList<List<SumLightListBean.LisJavaBean>>() ;
    private ArrayList<SumLightListBean.LisJavaBean> list;
    private Map<Integer, Integer> allListSize = new HashMap<>();

    private Map<Integer, List<SumLightListBean.LisJavaBean>> allList = new HashMap<>();
    private String mSaveuser_id;
    private String TAG = "TAG";
    private String user_netlimit;
    private String build_peanut;

    private RefreshLayout refreshLayout;

    @Override
    protected int initLayout() {
        return R.layout.sum_listview_layout;
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
        refreshLayout.setEnableRefresh(false); //禁止下拉刷新
        Log.e("初始化handler 对象 ", mHandler + "");
    }


    @Override
    protected void initListener() {
        refreshLayout.setOnLoadmoreListener(this);
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
        geiFirstDate();
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
            for (int i = 0; i <finallListData.size() ; i++) {
                list.addAll(finallListData.get(i));
            }
            Log.e(TAG, "firstList " + firstList.size());
            Log.e(TAG, "allListSize " + allListSize.toString());
            Log.e(TAG, "finallListData " + finallListData.size());
            adapter = new SumListViewBeaseAdapter(SumActivity.this, list);
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
                    for (int i = 0; i <finallListData.size() ; i++) {
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
            if (0  == maxPage) { //说明是第一次添加数据
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
                for (int i = 0; i <finallListData.size() ; i++) {
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


}
