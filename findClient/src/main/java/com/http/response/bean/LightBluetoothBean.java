package com.http.response.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fengx on 2018/1/3.
 */

public class LightBluetoothBean implements Serializable {

    protected String macNumber;
    protected ArrayList<String> mLightMacList = new ArrayList<>();

    public String getMacNumber() {
        return macNumber;
    }

    public ArrayList<String> getmLightMacList() {
        return mLightMacList;
    }

    public void setMacNumber(String macNumber) {

        this.macNumber = macNumber;
    }

    public void setmLightMacList(ArrayList<String> mLightMacList) {
        this.mLightMacList = mLightMacList;
    }

    @Override
    public String toString() {
        return "LightBluetoothBean{" +
                "macNumber='" + macNumber + '\'' +
                ", mLightMacList=" + mLightMacList +
                '}';
    }
}
