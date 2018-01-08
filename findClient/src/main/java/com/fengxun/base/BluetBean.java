package com.fengxun.base;

import android.util.Log;

import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;

/**
 * Created by fengx on 2017/12/21.
 */

public class BluetBean {
    private BleDevice device;
    private int rssi;
    private ArrayList<Integer> rssiList = new ArrayList();
    public int num;
    private byte[] scanRecord;

    public BluetBean(BleDevice device, int rssi, byte[] scanRecord) {
        rssiList.add(rssi);
        this.rssi = rssi;
        this.device = device;
        this.scanRecord = scanRecord;
    }

    public BleDevice getDevice() {
        return device;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
    public void addRssi(int rssi){
        rssiList.add(rssi);
        Log.e("mGattCallback","   ********  rssiList ************ size: " + rssiList.size() );
    }
    public void initRssi(){
        if (rssiList.size() > 1){
            rssi = rssiList.get(0);
        }
        if (rssiList.size() > 3) {
            rssiList.remove(0);
            rssiList.remove(rssiList.size() - 1);
            for (int i = 0; i < rssiList.size(); i++) {
                rssi += rssiList.get(i);
            }
        }
        rssi =  rssi / rssiList.size();
    }
    public int  getRssi() {
       return    rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "device=" + device +
                ", rssi=" + rssi +
                ", num=" + num +
                ", scanRecord=" + bytesToHexString(scanRecord) +
                '}';
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
