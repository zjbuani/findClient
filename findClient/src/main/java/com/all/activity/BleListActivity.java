package com.all.activity;

import android.app.ActionBar;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.adapter.MyRadioAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;

import com.fengxun.base.BluetoothBaseActivity;
import com.http.response.bean.LightBluetoothBean;
import com.imp.BluetoothDeviceTag;
import com.utils.StatusBarUtils;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;

/**
 * Created by fengx on 2017/12/27.
 */

public class BleListActivity extends BluetoothBaseActivity implements MyRadioAdapter.OnDeviceClickListener {
    private ListView mBle_listView;
    protected String mOnOff = "a1b1";
    private String mMac;
    private View mNextButton;


    @Override
    protected void initListener() {

    }
    @Override
    protected int initLayout() {
        return R.layout.ble_list_activity;
    }

    @Override
    protected void initView() {
        mBle_listView = (ListView) findViewById(R.id.ble_list_listView);
        mNextButton = findViewById(R.id.next);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        LightBluetoothBean mLightBluetoothBean = (LightBluetoothBean) intent.getSerializableExtra(BluetoothDeviceTag.BROADCAST_DATA);
        if (mBleDevice== null) {
            mBleDevice = intent.getParcelableExtra(BluetoothDeviceTag.KEY_DATA);
        }
        MyRadioAdapter baseBleListAdapter = new MyRadioAdapter(this, mLightBluetoothBean.getmLightMacList(), mLightBluetoothBean.getMacNumber());
        baseBleListAdapter.setOnDeviceClickListener(this);
        mBle_listView.setAdapter(baseBleListAdapter);
    }

    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }
    @Override
    public void radioButtonItemData(String mac) {
        mac = numToHex16(Integer.parseInt(mac));
        mMac = mac;
        Log.e("radioButtonItemData", " 点击 返回之后的mac   " +  mMac);
        mNextButton.setVisibility(View.VISIBLE);
//        writeData(mOnOff + mac);
        postLightData = mOnOff + mac; //需要发送的内容
        bleListItem = 0;         //因为发送 有时会失败有时 能成功 所以没包数据我 发 6 次 以确保数据发送成功
        mHandler.sendEmptyMessage(2);
    }

    public void click(View view){
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.next:
                Intent intent = new Intent(this, BleLightControlActivity.class);
                Log.e("radioButtonItemData",mMac   +  "   打开之前  mBleDevice " +  mBleDevice.getMac());
                intent.putExtra(BluetoothDeviceTag.DEVICE_MAC,mMac +"");
                intent.putExtra(BluetoothDeviceTag.KEY_DATA,mBleDevice);
                startActivity(intent);
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("mBleDevices",(mBleManager == null) + "   onDestroy");
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        if (mHandler!=null ){
            mHandler.removeMessages(1);
        }
    }


}
