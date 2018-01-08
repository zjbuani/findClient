package com.all.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.fengxun.base.BaseActivity;
import com.fengxun.base.BaseBleManager;
import com.fengxun.base.BluetoothBaseActivity;
import com.http.response.bean.LightBluetoothBean;
import com.imp.BleConnectDevice;
import com.imp.BleDeviceNotify;
import com.imp.BleScanDevice;
import com.imp.BluetoothDeviceTag;
import com.skyfishjy.library.RippleBackground;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RunnableFuture;

/**
 * Created by fengx on 2017/10/21.
 */

public class SingleOperationActivity extends BluetoothBaseActivity implements View.OnClickListener, BleScanDevice,BleConnectDevice,BleDeviceNotify {

    private static final int REQUEST_ENABLE_BT = 3;
    private ImageView mBluetoothImage;
    private RippleBackground mRipple;
    private String[] permissions = {Manifest.permission.BLUETOOTH};
    private AlertDialog dialog;

    private boolean mResultCode;
    private ArrayAdapter mArrayAdapter;
    public static final int MSG_REGISTER = 1;
    public static final int MSG_UNREGISTER = 2;
    public static final int MSG_BLUETOOTH_OFF = 3; // 手机蓝牙关闭
    public static final int MSG_BLUETOOTH_ON = 4; // 手机蓝牙打开
    public static final int MSG_BLUETOOTH_SEARCH_DEVICE = 5; // 搜索到的手机设备
    public static final int MSG_BLUETOOTH_START_SEARCH = 6; // 开启搜索手机蓝牙搜索
    public static final int MSG_BLUETOOTH_SUBSCRIBE_SUCCESS = 7; // 和蓝牙设备订阅成功
    public static final int MSG_BLUETOOTH_SEARCH_FINISH = 8; // 手机蓝牙搜索完成
    public static final int CONNECT_BLE_DEVICE = 9; //连接蓝牙设备
    public static final int MSG_DEVICE_CONNECT_STATE = 10; //连接状态
    public static final int MSG_SET_TEMPERATURE = 11; //设置温度
    public static final int MSG_SEND_TEMPERATURE = 12; //返回蓝牙设备温度
    public static final int MSG_BLUETOOTH_START_SEARCH_CANCEL = 13; // 反注册蓝牙搜索广播

    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected int initLayout() {
        return R.layout.single_operation_layout;
    }

    @Override
    protected void initView() {
        //蓝牙按钮
        mBluetoothImage = (ImageView) findViewById(R.id.centerImage);
        //波纹效果
        mRipple = (RippleBackground) findViewById(R.id.content);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        if (mBluetoothAdapter == null) {
            ToastShowUtil.showToast(this, "您的设备不支持蓝牙");
            finish();
        }
        isPermissions();

    }

    /**
     * 判断 手机版本 是否 支持 蓝牙
     * 是:  查看手机的SDK版本 是否大于23 :
     * 是: 动态申请权限
     * 否:不用动态申请权限
     * 否: 老古董手机 直接关闭此界面
     */
    public void isPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    ToastShowUtil.showToast(this, "权限获取成功");
                }
            }
        }
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("蓝牙权限不可用")
                .setMessage("由于需要连接蓝牙，必须开启才能正常工作；\n否则，您将无法正常使用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {
        // 跳转到应用设置界面
        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }


    @Override
    protected void initListener() {
        mBluetoothImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.centerImage:
                //首先判断 是否开启蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                    //是  启动了  蓝牙
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //  ACTION_REQUEST_ENABLE 操作 Intent 调用 startActivityForResult()。这将通过系统设置发出启用蓝牙的请求
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    mResultCode = true;
                }
                if (mResultCode) {
                    mRipple.startRippleAnimation(); //开始动画
//                    mBleManager = new BaseBleManager(getApplication());
                    mBleManager = BaseBleManager.getInstance(getApplication());
                    Log.e("mBleManager", (mBleManager == null) + "");
                    mBleManager.Scan(this);
                    mBleManager.connectCallBack(this);
                    mBleManager.NotifyCallBack(this);
//
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void ScanCallBack(boolean success, BleDevice mBleDevice) {
        if (!success) {
            mBleManager.stopScan();
        }
    }

    @Override
    public void connectCallBack(boolean success, BleDevice mBleDevices, BleException exception) {
        if (!success) {
            ToastShowUtil.showToast(this, "蓝牙链接失败");
            mRipple.stopRippleAnimation(); // 停止动画
            return;
        }
        mBleDevice = mBleDevices;
        mHandler.sendEmptyMessage(1);

    }

    @Override
    public void notifySuccessCallBack(boolean success) {
        if (success) {
            writeData("a0b04659");
        } else {
            ToastShowUtil.showToast(this, "打开蓝牙服务失败");
        }
    }

    @Override
    public void notifyLightDataCallBack(LightBluetoothBean mLightBluetoothBean) {
        Log.e("mGattCallback", "notifyCallBack    " + mBleDevice.getMac() + "  打开 窗口" + mLightBluetoothBean);
        Intent intent = new Intent(this, BleListActivity.class);
        intent.putExtra(BluetoothDeviceTag.BROADCAST_DATA, mLightBluetoothBean);
        intent.putExtra(BluetoothDeviceTag.KEY_DATA, mBleDevice);
        startActivity(intent);
        mRipple.stopRippleAnimation(); // 停止动画
    }

    @Override
    public void notifyCameraDataCallBack(ArrayList<String> list) {
        Log.e("mGattCallback", "notifyCallBack    " + mBleDevice.getMac() + "  打开 窗口" + list);
        Intent intent = new Intent(this, BleListActivity.class);
        intent.putExtra(BluetoothDeviceTag.BROADCAST_DATA, list);
        intent.putExtra(BluetoothDeviceTag.KEY_DATA, mBleDevice);
        startActivity(intent);
    }


    /*   @Override
       public void finish() {
           super.finish();
           overridePendingTransition(R.anim.left,R.anim.right);  // 关闭时的  Activity的过场动画  左进右出
       }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", resultCode + " ");
        Log.e("onActivityResult", requestCode + " ");
        if (requestCode == REQUEST_ENABLE_BT) { // 说明开启了
            mResultCode = true;
        }
    }

}
