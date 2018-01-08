package com.fengxun.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.http.response.bean.SignInBean;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * Created by fengx on 2017/10/23.
 */

public abstract class LightBaseActivity extends SlidingFragmentActivity {
    protected static final String TAG_RIGHT_MENU = "TAG_RIGHT_MENU";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancel();

        setContentView(initLayout());
        initView();
        initListener();
        initData();
    }
    protected   void cancelTitleBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消 titleBar
    };
    protected   void cancel(){};
    //添加布局
    protected abstract int initLayout();

    //初始化 控件
    protected void  initView(){};
    //更新数据
    protected  void initData(){};

    //添加监听事件
    protected   void initListener(){};

    public abstract void rightGroupItem(int groupPosition, int childPosition);


}
