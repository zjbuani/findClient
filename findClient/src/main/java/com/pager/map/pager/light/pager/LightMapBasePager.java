package com.pager.map.pager.light.pager;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

public abstract class LightMapBasePager implements View.OnClickListener  {
    public View mRootView;
    public Context context;
    public ArrayList list = new ArrayList();
    public LightMapBasePager(Context activity) {
        context = activity;
        mRootView = initView();

        initListener();
    }

    protected abstract void initListener();


    public abstract View initView() ;

    public abstract void initData();
}