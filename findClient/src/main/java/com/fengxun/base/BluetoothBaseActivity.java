package com.fengxun.base;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.all.activity.BleListActivity;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.imp.BluetoothDeviceTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by fengx on 2017/12/26.
 */

public abstract class BluetoothBaseActivity extends BaseActivity {
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    protected UUID ReadUUID = UUID.fromString(BluetoothDeviceTag.ReadUUID);
    protected UUID WriteUUID = UUID.fromString(BluetoothDeviceTag.WriteUUID);
    protected UUID BigUUID = UUID.fromString(BluetoothDeviceTag.BigUUID);
    public BleDevice mBleDevice;
    protected BluetoothGatt gatt;
    public String LogWriteData = "writeData";
    public static final String TAG = "mGattCallback";
    public BaseBleManager mBleManager;
    protected String postLightData;
    private boolean isInitData;
    protected int bleListItem = 0;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mBleDevice != null) {
                        writeData("c1d1");
                    }
                    mHandler.sendEmptyMessageDelayed(1, 200);
                    break;
                case 2:
                    writeData(postLightData);
                    bleListItem++;
                    if (bleListItem < 6) {
                        mHandler.sendEmptyMessage(2);
                    }
                    break;
            }
        }
    };


  /*    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initAllView();
        initData();
        initListener();
    }
*/
    protected abstract void initListener();

    protected void initAllView() {
        initView();

    }



    public void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "不支持 ", Toast.LENGTH_LONG).show();
            return;
        }
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("请打开蓝牙功能 定位")
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton("前往设置",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, 300);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                }
                break;
        }
    }
    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 写入蓝牙服务的 方法
     * <p>
     * bleDevice 蓝牙扫描后 返回的数据
     *
     * @param data 需要写入蓝牙的数据
     */
    public void writeData(String data) {
        Log.e(LogWriteData, data);
        BleManager.getInstance().write(
                mBleDevice,
                BluetoothDeviceTag.BigUUID,
                BluetoothDeviceTag.WriteUUID,
                HexUtil.hexStringToBytes(data),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess() {
                        // 发送数据到设备成功（UI线程）
                        Log.e(TAG, "onWriteSuccess  写入成功 ");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        // 发送数据到设备失败（UI线程）
                        Log.e(TAG, "onWriteFailure  发送数据到设备失败 " +exception.getDescription()   +  "   " + exception.getCode()  );
                    }
                });
    }



    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();



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
