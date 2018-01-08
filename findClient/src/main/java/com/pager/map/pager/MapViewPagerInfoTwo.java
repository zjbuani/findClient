package com.pager.map.pager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.all.activity.AddWMSMapActivity;
import com.zhy.sample.folderlayout.R;

/**
 * Created by fengx on 2017/11/28.
 */

public class MapViewPagerInfoTwo extends MapBasePager  {
    public MapViewPagerInfoTwo(Context activity) {
        super(activity);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.two, null);
        ImageView mMapdevice63x = (ImageView) view.findViewById(R.id.mapdevice63x);
        mMapdevice63x.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        AddWMSMapActivity context = (AddWMSMapActivity) this.context;
        context.onClickImage(view);
    }


}
