package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.http.response.bean.SumLightListBean;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengx on 2017/10/19.
 */

public class SumListViewBeaseAdapter extends BaseAdapter {

    private final Context context;
    private final List<SumLightListBean.LisJavaBean> lists;

    public SumListViewBeaseAdapter(Context context, List<SumLightListBean.LisJavaBean> lists) {
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
            convertView = View.inflate(context, R.layout.sum_listview_item, null);
            mHodler.sum_id = (TextView) convertView.findViewById(R.id.sum_id);  //  设备编号
            mHodler.sum_device_id = (TextView) convertView.findViewById(R.id.sum_device_id); //设备ID
            mHodler.sum_temperatur = (TextView) convertView.findViewById(R.id.sum_temperatur); //温度
            mHodler.illuminance_id = (TextView) convertView.findViewById(R.id.illuminance_id); // 照度
            convertView.setTag(mHodler);
        } else {
            mHodler = (ViewHodler) convertView.getTag();
        }
        Log.e("position", "position*******************    " + position + "");
//        List<SumLightListBean.LisJavaBean> lisJavaBeen = lists.get(position);
//        lisJavaBeen.get(position);   getCount
        SumLightListBean.LisJavaBean bean = lists.get(position);
        mHodler.sum_id.setText(position + "");
        mHodler.sum_device_id.setText(bean.getFmn() + "");
        mHodler.sum_temperatur.setText(bean.getFtp() + "℃");
        mHodler.illuminance_id.setText(bean.getLst() + "lx");

        return convertView;
    }

    public void notifyData() {
        this.notifyDataSetChanged();
    }

    class ViewHodler {
        public TextView sum_id;
        public TextView sum_device_id;
        public TextView sum_temperatur;
        public TextView illuminance_id;
    }
}
