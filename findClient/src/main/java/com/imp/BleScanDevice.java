package com.imp;

import android.os.Handler;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.http.response.bean.LightBluetoothBean;

import java.util.ArrayList;
import java.util.List;

/**
 * BleManager 的 扫描 , 链接 , 通知 的 接口
 */

public interface BleScanDevice {
    /**
     * 蓝牙扫描回调
     *
     * @param success    扫描成功 或者失败
     * @param mBleDevice 扫描 结果
     */
    void ScanCallBack(boolean success, BleDevice mBleDevice);



}
