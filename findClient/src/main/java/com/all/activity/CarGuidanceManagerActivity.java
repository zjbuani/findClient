package com.all.activity;

import com.fengxun.base.BaseActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

/**
 * Created  车位引导 管理 界面
 */

public class CarGuidanceManagerActivity extends BaseActivity implements OnLoadmoreListener {
    @Override
    protected int initLayout() {
        return 0;
    }

    //上拉加载的 回调
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }
}
