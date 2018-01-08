package com.pager.map.pager.light.pager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.all.activity.WMSBaseActivity;
import com.zhy.sample.folderlayout.R;

public class LightMapViewPagerInfoTwo extends LightMapBasePager  {
    public LightMapViewPagerInfoTwo(Context activity) {
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
        WMSBaseActivity context = (WMSBaseActivity) this.context;
        context.onClickImage(view);
    }


}