package com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;


import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyRadioAdapter extends BaseAdapter {
    private final String mBigMac;
    private HashMap<String, Boolean> states = new HashMap<String, Boolean>();//记录所有radiobutton被点击的状态
    private final Context context;
    private final ArrayList<String> list;

    public MyRadioAdapter(Context context, ArrayList<String> list, String mBigMac) {
        this.mBigMac = mBigMac;
        this.context = context;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHodler mViewHodler;
        if (view == null) {
            mViewHodler = new ViewHodler();
            view = View.inflate(context, R.layout.my_lv_item_radio_layout, null);
            mViewHodler.diamond = (ImageView) view.findViewById(R.id.diamond);
            mViewHodler.button = (RadioButton) view.findViewById(R.id.button);
            view.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHodler) view.getTag();
        }
        final String item = list.get(position);
        mViewHodler.button.setOnClickListener(new   View.OnClickListener() {
            public void onClick(View v) {// 重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), mViewHodler.button.isChecked());
                notifyDataSetChanged();
                mListener.radioButtonItemData(item);
            }
        });
        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position),false);
        } else {
            res = true;
        }

        mViewHodler.button.setChecked(res);

        if (item.equals(mBigMac)){
            mViewHodler.diamond.setVisibility(View.VISIBLE);
        }else {
            mViewHodler.diamond.setVisibility(View.INVISIBLE);
        }
        mViewHodler.button.setText("MAC:" + item);
        return view;
    }
    public interface OnDeviceClickListener {
        void radioButtonItemData(String mac);
    }

    private OnDeviceClickListener mListener;

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }
    class ViewHodler {
        private RadioButton button;
        private ImageView diamond;
    }
}