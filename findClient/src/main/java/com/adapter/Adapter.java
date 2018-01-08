package com.adapter;

import android.content.Context;


import com.http.response.bean.SignInBean;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 左边菜单的 折叠 ListView 的 适配器
 */

public class Adapter extends BaseExpandableAdapter {

    public Adapter(Context context, HashMap<String, List<SignInBean.BuildListJavaBean>> groupList, ArrayList<String> mGroup_List_key) {
        super(context, groupList, mGroup_List_key);
    }
    @Override
    protected int groupLayout() {
        return R.layout.group;
    }
    @Override
    protected int groupLayoutTvId() {
        return R.id.groupto;
    }

    @Override
    protected int groupChildTvId() {
        return R.id.childto;
    }

    @Override
    protected int groupChildLayout() {
        return R.layout.child;
    }


}
