package com.utils;

import android.util.Log;

import com.fengxun.base.BluetBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengx on 2017/12/21.
 */

public class BluetRssiUtils {
    public static List<BluetBean> sort(List<BluetBean> mBBList) {
        ArrayList<BluetBean> bluetBeen = new ArrayList<BluetBean>();
        HashMap<String, BluetBean> map = new HashMap<String, BluetBean>(); //用来存储mac  用于判断是否 存入了 这个mac  用于记录mac出现的次数使用
        for (int i = 0; i < mBBList.size(); i++) {
            BluetBean bluetBean1 = mBBList.get(i);
            String mMac = bluetBean1.getDevice().getMac();
            if (map.get(mMac) != null) {  //说明 map中 已经有此对象了
                Log.e("mGattCallback", "get(mMac)  " + bluetBean1.getDevice().getMac() +  "   " + bluetBean1.getDevice().getRssi());
                map.get(mMac).addRssi(bluetBean1.getDevice().getRssi());

            }else {    //  说明map中还么有 存入此对象
                Log.e("mGattCallback", "else  get(mMac)  " + bluetBean1.getDevice().getMac() +  "   " + bluetBean1.getRssi());
                map.put(mMac, bluetBean1);
            }

        }
        mBBList.clear();

        Log.e("mGattCallback",  map.size()  +  "   map size" );
        for (String strings : map.keySet()) {
            BluetBean mBluetBean = map.get(strings);
            mBluetBean.initRssi();
            Log.e("mGattCallback", "MAP :::  " + mBluetBean.getDevice().getMac() +  "   " + mBluetBean.getRssi());
            mBBList.add(mBluetBean);
        }
        sortOld(mBBList);
        for (int i= 0;i <mBBList.size();  i ++){
            Log.e("mGattCallback", "mBBList :::  " + mBBList.get(i).getDevice().getMac() + " getRssi " +  mBBList.get(i).getRssi());
        }
        return mBBList;
    }

    private static void sortOld(List<BluetBean> bluetBeen) {
        Collections.sort(bluetBeen, new Comparator<BluetBean>() {
            @Override
            public int compare(BluetBean bluetBean, BluetBean t1) {
                return t1.getRssi() - bluetBean.getRssi();
            }
        });

    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
