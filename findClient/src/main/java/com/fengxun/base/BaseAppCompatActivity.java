package com.fengxun.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by fengx on 2017/6/20.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancel();
        setContentView(initLayout());
        initView();
        initListener();
        initData();

    }

    protected   void cancel(){};

    protected   void cancelTitleBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消 titleBar
    };

    //添加布局
    protected abstract int initLayout();

    //初始化 控件
    protected void  initView(){};
    //更新数据
    protected  void initData(){};

    //添加监听事件
    protected   void initListener(){};
}
