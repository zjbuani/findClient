package com.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.http.response.bean.CameraListBean;
import com.http.response.bean.MarkerErrListBean;
import com.http.response.bean.MarkerLayoutBean;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.List;

public class MarkerInfoWindow extends Dialog {

    private Context context;
    public TextView mArea_fmn;
    public TextView mMarker_ch_err;
    public TextView mMarker_err;
    public TextView mMarker_id;
    public TextView mMarker_ch;
    public TextView mMarker_tv_err;
    private float x = 0;
    private float y = 0;

    public MarkerInfoWindow(Context context) {
        this(context, R.style.MyDialog);
        this.context = context;

    }

    public MarkerInfoWindow(Context context, int themeResId) {
        super(context, themeResId);
        View view = getLayoutInflater().inflate(R.layout.marker_layout, null);
        mArea_fmn = (TextView) view.findViewById(R.id.area_fmn);
        mMarker_ch_err = (TextView) view.findViewById(R.id.marker_ch_err);
        mMarker_err = (TextView) view.findViewById(R.id.marker_err);
        mMarker_id = (TextView) view.findViewById(R.id.marker_id);
        mMarker_ch = (TextView) view.findViewById(R.id.marker_ch);
        mMarker_tv_err = (TextView) view.findViewById(R.id.marker_tv_err);

        setContentView(view);
    }


    //显示dialog的方法
    public void showDialog(ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers, ArrayList<Integer> mErrPositionIndexOf, MarkerErrListBean mMarkerErrList) {
        List<MarkerErrListBean.DeviceListJavaBean> mDevice_list = mMarkerErrList.getDevice_list();
        setInfoWindow(integers, mErrPositionIndexOf, mDevice_list, mArea_fmn, mMarker_ch_err, mMarker_err);
        super.setCanceledOnTouchOutside(true);   //点击外部 允许关闭dialog
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackground();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    private void initBackground() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (x != 0 && y != 0) {
            lp.x = (int) (x + 0.5f);
            lp.y = (int) (y + 0.5f);
        }
        //设置背景透明度 背景透明
        lp.alpha = 0.8f;//参数为0到1之间。0表示完全透明，1就是不透明。按需求调整参数
        window.setAttributes(lp);
    }

    public static void setInfoWindow(ArrayList<MarkerLayoutBean.DeviceListJavaBean> integers, ArrayList<Integer> mErrPositionIndexOf, List<MarkerErrListBean.DeviceListJavaBean> mDevice_list, TextView mArea_fmn, TextView mMarker_ch_err, TextView mMarker_err) {
        for (int i = 0; i < integers.size(); i++) {
            MarkerLayoutBean.DeviceListJavaBean mErrbean = integers.get(i);
            String i1 = mErrbean.getArea_id() + "--" + mErrbean.getFmn();
            String mErrCode = mErrbean.getErrcode() + "";
            String mChannel = mDevice_list.get(mErrPositionIndexOf.get(i)).getChannel();
            mArea_fmn.setText(i1);
            mMarker_ch_err.setText(mChannel);
            mMarker_err.setText(mErrCode);
        }
    }

    //显示dialog的方法
    public void showDialog(CameraListBean.CameraListJavaBean cameraListJavaBean) {
        setInfoWindow(cameraListJavaBean, mArea_fmn, mMarker_ch_err, mMarker_err);
        initBackground();
        super.setCanceledOnTouchOutside(true);   //点击外部 允许关闭dialog
    }

    public static void setInfoWindow(CameraListBean.CameraListJavaBean mCameraBean, TextView mArea_fmn, TextView mMarker_ch_err, TextView mMarker_err) {
        String fmn = mCameraBean.getCamera_area() + "-" + mCameraBean.getCamera_id();
        String mChannel = mCameraBean.getChannel();
        String mCamera_temp = mCameraBean.getCamera_temp() + "";
        mArea_fmn.setText(fmn);
        mMarker_ch_err.setText(mChannel);
        mMarker_err.setText(mCamera_temp + "℃");


    }


}