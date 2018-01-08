package com.adapter;

import android.content.Context;

import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.SignInBean;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 左边菜单的 折叠 ListView 的 适配器
 */

public class RightAdapter extends RightBaseExpandableAdapter {

    public RightAdapter(Context context, BuildingFloorBean signInBean, String[] allFloorNames) {
        super(context,signInBean,allFloorNames);
    }
    @Override
    protected int groupLayout() {
        return R.layout.right_group;
    }
    @Override
    protected int groupLayoutTvId() {
        return R.id.right_groupto;
    }

    @Override
    protected int groupChildTvId() {
        return R.id.right_childto;
    }

    @Override
    protected int groupChildLayout() {
        return R.layout.right_child;
    }


}
