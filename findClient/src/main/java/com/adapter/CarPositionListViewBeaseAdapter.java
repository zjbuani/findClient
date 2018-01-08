package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.http.response.bean.CarListBean;
import com.http.response.bean.SumLightListBean;
import com.zhy.sample.folderlayout.R;

import java.util.List;

/**
 * Created by fengx on 2017/10/19.
 */

public class CarPositionListViewBeaseAdapter extends BaseAdapter {

    private final Context context;
    private final List<CarListBean.CarListJavaBean> lists;

    public CarPositionListViewBeaseAdapter(Context context,List<CarListBean.CarListJavaBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return (lists == null ? 0 : lists.size());
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler mHodler;
        if (convertView == null) {
            mHodler = new ViewHodler();
            convertView = View.inflate(context, R.layout.car_position_listview_item, null);
            mHodler.sum_id = (TextView) convertView.findViewById(R.id.sum_id);  //  设备编号
            mHodler.sum_areaName = (TextView) convertView.findViewById(R.id.sum_device_id); //设备ID
            mHodler.sum_state = (TextView) convertView.findViewById(R.id.sum_temperatur); //温度
            mHodler.sum_info = (ImageView) convertView.findViewById(R.id.illuminance_id); // 照度
            convertView.setTag(mHodler);
        } else {
            mHodler = (ViewHodler) convertView.getTag();
        }
        Log.e("position", "position*******************    " + position + "");
//        List<SumLightListBean.LisJavaBean> lisJavaBeen = lists.get(position);
//        lisJavaBeen.get(position);   getCount
        CarListBean.CarListJavaBean bean = lists.get(position);
        mHodler.sum_id.setText(bean.getCarpos()); //返回车位号
        mHodler.sum_areaName.setText(bean.getLran()); //返回分区名
        String ful = bean.getFul();
        String mFul;
        if ( ful == null){
            mFul="故障";
        }else if ("0".equals(ful)){
            mFul="空";
        }else if ("1".equals(ful)){
            mFul="满";
        }else{
            mFul="故障";
        }
        mHodler.sum_state.setText(mFul);
//        mHodler.sum_info.setText(bean.getLst() + "lx");  // 这里返回点击查看按钮 就是旁边的小眼睛 用来查看颜色什么的

        return convertView;
    }

    public void notifyData() {
        this.notifyDataSetChanged();
    }

    class ViewHodler {
        public TextView sum_id;
        public TextView sum_areaName;
        public TextView sum_state;
        public ImageView sum_info;
    }
}
