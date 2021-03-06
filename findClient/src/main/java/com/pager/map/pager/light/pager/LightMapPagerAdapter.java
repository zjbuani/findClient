package com.pager.map.pager.light.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.pager.map.pager.MapBasePager;

import java.util.ArrayList;

/**
 * Created by fengx on 2017/11/28.
 */

public class LightMapPagerAdapter extends PagerAdapter {
    private final ArrayList<LightMapBasePager> list;

    public LightMapPagerAdapter(ArrayList<LightMapBasePager> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LightMapBasePager pager = list.get(position);
        View view = pager.mRootView;
        pager.initData();
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
