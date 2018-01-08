package com.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.pager.BasePager;

import java.util.ArrayList;

/**
 * Created by fengx on 2017/10/9.
 */

public class LightingSystemAdapter extends PagerAdapter {


    private ArrayList<BasePager> list;

    public LightingSystemAdapter(ArrayList<BasePager> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager = list.get(position);
        View view = pager.mRootView;
        pager.initData();
        container.addView(view);
        return view;
    }
}
