package com.imp;

/**
 * Created by fengx on 2017/12/25.
 */

public interface BluetoothDeviceTag {
    String DEVICE_MAME = "DEVICE_MAME";  //设备名称
    String DEVICE_MAC = "DEVICE_MAC";  //设备mac
    String BROADCAST_DATA = "broadcast";  //设备mac
    String KEY_DATA = "ble_key_data";  // 蓝牙 设备
    String DEVICE_RSSI = "RSSI";  //设备 信号强度
    String  WriteUUID = "0000fff1-0000-1000-8000-00805f9b34fb";
    String ReadUUID = "0000fff4-0000-1000-8000-00805f9b34fb";
    String BigUUID = "0000fff0-0000-1000-8000-00805f9b34fb";

}
