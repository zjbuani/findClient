package com.imp;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.http.response.bean.LightBluetoothBean;

import java.util.ArrayList;

/**
 * BleManager 的 扫描 , 链接 , 通知 的 接口
 */

public interface BleDeviceNotify {
    /**
     * 监听服务 获取蓝牙通知 回调
     *
     * @param success 通知成功 或者失败
            获取到 服务返回的通知
     */
    void notifySuccessCallBack(boolean success);
    void notifyLightDataCallBack(LightBluetoothBean mLightBluetoothBean);
    void notifyCameraDataCallBack(ArrayList<String> list);

}
