package com.imp;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.http.response.bean.LightBluetoothBean;

import java.util.ArrayList;

/**
 * BleManager 的 扫描 , 链接 , 通知 的 接口
 */

public interface BleConnectDevice {

    /**
     * 此函数 用来监听 连接蓝牙 的 回调
     *
     * @param success    链接成功 或者失败
     * @param mBleDevice 链接成功的设备
     * @param exception  链接失败 异常
     */
    void connectCallBack(boolean success, BleDevice mBleDevice, BleException exception);
}
