package com.pager.map.pager;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by fengx on 2017/11/28.
 */

public abstract class MapBasePager implements View.OnClickListener  {
    public View mRootView;
    public Context context;
    public ArrayList  list = new ArrayList();
    public MapBasePager(Context activity) {
        context = activity;
        mRootView = initView();

        initListener();
    }

    protected abstract void initListener();


    public abstract View initView() ;

    public abstract void initData();
}
