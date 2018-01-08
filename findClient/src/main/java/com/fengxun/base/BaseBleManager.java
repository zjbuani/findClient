package com.fengxun.base;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.http.response.bean.LightBluetoothBean;
import com.imp.BleConnectDevice;
import com.imp.BleDeviceNotify;
import com.imp.BleScanDevice;
import com.imp.BluetoothDeviceTag;
import com.utils.BluetRssiUtils;
import com.utils.ToastShowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengx on 2018/1/2.
 */

public class BaseBleManager {


    private ArrayList<Integer> ctrlMacArray = new ArrayList<>();
    private static final String TAG = "mGattCallback";
    private List<BleDevice> mBBList = new ArrayList<BleDevice>();
    private List<BluetBean> mBluetBean = new ArrayList<BluetBean>();
    private static BleManager instance;

    private BaseBleManager() {
    }

    public BleManager getBleManager() {
        return instance;
    }

    private static BaseBleManager mBaseBleManager = null;

    public static BaseBleManager getInstance(Application app) {
        if (mBaseBleManager == null) {
            mBaseBleManager = new BaseBleManager();
            instance = BleManager.getInstance();
            instance.init(app);
            config(true, 2000);
        }
        return mBaseBleManager;
    }

  /*  public BaseBleManager(Application app) {
        if (instance == null) {
            instance = BleManager.getInstance();
            instance.init(app);
            config(true, 2000);
        }
    }*/


    /**
     * @param enable         是否输出蓝牙log
     * @param operateTimeout 扫描时间
     */
    public static void config(boolean enable, int operateTimeout) {
        if (instance != null) {

            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setScanTimeOut(operateTimeout)             // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                    .build();
            instance.initScanRule(scanRuleConfig);
           /* instance.enableLog(true)
                    .setMaxConnectCount(maxCount)
                    .setOperateTimeout(operateTimeout).build;*/
        }
    }

    /**
     * 设备扫描
     */
    public void Scan(BleScanDevice mBleScanDevice) {
        if (instance != null) {
            this.mBleScanDevice = mBleScanDevice;
            instance.scan(new mBleScanCallBack());
        }
    }

    /**
     * 设备连接回调
     */
    public void connectCallBack(BleConnectDevice mBleConnectDevice) {
        if (instance != null) {
            this.mBleConnectDevice = mBleConnectDevice;

        }
    }

    /**
     * 设备连接回调
     */
    public void NotifyCallBack(BleDeviceNotify mBleDeviceNotify) {
        if (instance != null) {
            this.mBleDeviceNotify = mBleDeviceNotify;

        }
    }

    public void stopScan() {
        if (instance != null) {
            instance.cancelScan();
        }
    }

    public void disableBluetooth() {
        if (instance != null) {
            instance.disableBluetooth();
        }
    }

    private static BleScanDevice mBleScanDevice;        // 扫描的回调
    private static BleConnectDevice mBleConnectDevice;  // 链接的回调
    private static BleDeviceNotify mBleDeviceNotify;    //通知的回调
    private BleDevice mBleDevice;

    /**
     * 扫描蓝牙
     */
    private class mBleScanCallBack extends BleScanCallback {
        @Override
        public void onScanStarted(boolean success) {
            Log.e(TAG, (success == true ? "开始扫描" : "扫描失败"));
            mBleScanDevice.ScanCallBack(success, null);

        }

        @Override
        public void onScanning(BleDevice result) {

        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            for (int i = 0; i < scanResultList.size(); i++) {
                BleDevice bleDevice = scanResultList.get(i);
                String mData = bytesToHexString(bleDevice.getScanRecord());
                if (mData.contains("42463230")) {  // 这样是 我们的设备
                    BluetBean bluetBean = new BluetBean(bleDevice, bleDevice.getRssi(), bleDevice.getScanRecord());
                    mBluetBean.add(bluetBean);
                }
            }
            List<BluetBean> AfterOrderingList = BluetRssiUtils.sort(mBluetBean);
            if (AfterOrderingList.size() == 0) {
                return;
            }
            BluetBean bluetBean = AfterOrderingList.get(0);
            Log.e(TAG, "scanResultList  " + bluetBean.getDevice().getMac() + " Rssi : " + bluetBean.getRssi());
            mBleScanDevice.ScanCallBack(true, AfterOrderingList.get(0).getDevice());
            // 筛选出 信号强度最好的
            BleDevice device = AfterOrderingList.get(0).getDevice();
            instance.connect(device, new MyBleCattCallBack());

        }
    }

    class MyBleCattCallBack extends BleGattCallback {


        @Override
        public void onStartConnect() {
            Log.e(TAG, "onStartConnect   开始连接（UI线程） ");

        }

        @Override
        public void onConnectFail(BleException exception) {
            // 链接 蓝牙 置位符
            Log.e(TAG, "onConnectFail   连接失败（UI线程）: " + exception.getCode());
            mBleConnectDevice.connectCallBack(false, null, exception);
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            mBleDevice = bleDevice;
            Log.e("bytesToHexString", "onConnectSuccess  链接成功  :BleDevice即为所连接的BLE设备（UI线程）   " + bleDevice.getMac() + " 链接状态码:  " + status);
            mBleConnectDevice.connectCallBack(true, bleDevice, null); // 回调 链接成功
            notify_ble(bleDevice);
            // 链接 蓝牙 置位符
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            if (instance != null) {
                instance.disconnect(device); //断开蓝牙
            }
            Log.e(TAG, "连接中断，isActiveDisConnected表示是否是主动调用了断开连接方法（UI线程）");
        }
    }

    /**
     * 设置 通知
     */
    private void notify_ble(BleDevice bleDevice) {
        BleManager.getInstance().notify(
                bleDevice,
                BluetoothDeviceTag.BigUUID,
                BluetoothDeviceTag.ReadUUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        // 打开通知操作成功（UI线程）
                        Log.e("bytesToHexString", "打开通知操作成功  ");
                        mBleDeviceNotify.notifySuccessCallBack(true);
                        Log.e("radioButtonItemData", true + " ");
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        // 打开通知操作失败（UI线程）
                        Log.e("bytesToHexString", "打开通知操作失败  ");
                        mBleDeviceNotify.notifySuccessCallBack(false); // 监听蓝牙的 服务回调 通知回调
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        ArrayList<String> list = new ArrayList<>();
                        LightBluetoothBean mLightBluetoothBean = new LightBluetoothBean();
                        String resultData = bytesToHexString(data);
                        Log.e("radioButtonItemData", resultData + "    resultData   ");
                        String substring = resultData.substring(0, 4);
                        if (!"b0a0".equals(substring)) {
                            mBleDeviceNotify.notifySuccessCallBack(false); // 监听蓝牙的 服务回调 通知回调
                            return;
                        }
//        list.add(substring); // 校验开头是不是b0a0
                        substring = resultData.substring(4, 8);  //主  mac 地址
                        int macNumber = Integer.parseInt(substring, 16);// 主  mac 地址
                        mLightBluetoothBean.setMacNumber(macNumber + "");
                        Log.e("substring", macNumber + " ");
                        substring = resultData.substring(8, 10); // 设备标识码
                        int deviceMark = Integer.parseInt(substring, 16);
                        substring = resultData.substring(10, 12); // 频段
                        int channel = Integer.parseInt(substring, 16);
                        substring = resultData.substring(12);// 设备使能
                        int ctrlNum = Integer.parseInt(substring, 16);
                        for (int i = 0; i < 9; i++) {
                            if ((ctrlNum & (1 << i)) == 0) {
                                continue;
                            }
                            Integer ctrlMac = macNumber / 10 * 10 + i + 1;
                            ctrlMacArray.add(ctrlMac);
                            list.add(ctrlMac + "");
                        }
                        mLightBluetoothBean.getmLightMacList().addAll(list);
                        mBleDeviceNotify.notifyLightDataCallBack(mLightBluetoothBean);
                        // 打开通知后，设备发过来的数据将在这里出现（UI线程）
                    }
                });
    }
    /**
     * 设置 通知
     */
    public void notify_bleCallback(BleNotifyCallback callback) {
        BleManager.getInstance().notify(
                mBleDevice,
                BluetoothDeviceTag.BigUUID,
                BluetoothDeviceTag.ReadUUID,
                callback);
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

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


}
