package com.pager.map.pager.light.pager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.all.activity.WMSBaseActivity;
import com.zhy.sample.folderlayout.R;

public class LightMapViewPagerInfoOne extends LightMapBasePager {
    public LightMapViewPagerInfoOne(Context activity) {
        super(activity);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.one, null);
        ImageView mMapdevice03x = (ImageView) view.findViewById(R.id.mapdevice03x);
        ImageView mMapdevice53x = (ImageView) view.findViewById(R.id.mapdevice53x);
        ImageView mMapdevice13x = (ImageView) view.findViewById(R.id.mapdevice13x);
        ImageView mMapdevice23x = (ImageView) view.findViewById(R.id.mapdevice23x);
        ImageView mMapdevice43x = (ImageView) view.findViewById(R.id.mapdevice43x);
        ImageView mMapdevice33x = (ImageView) view.findViewById(R.id.mapdevice33x);
        mMapdevice03x.setOnClickListener(this);
        mMapdevice53x.setOnClickListener(this);
        mMapdevice13x.setOnClickListener(this);
        mMapdevice23x.setOnClickListener(this);
        mMapdevice43x.setOnClickListener(this);
        mMapdevice33x.setOnClickListener(this);
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