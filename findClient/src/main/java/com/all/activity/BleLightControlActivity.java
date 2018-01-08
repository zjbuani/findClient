package com.all.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.fengxun.base.BaseBleManager;
import com.fengxun.base.BluetoothBaseActivity;
import com.http.response.bean.LightBluetoothBean;
import com.http.response.bean.LightBluetoothMode;
import com.http.response.bean.Time;
import com.imp.BleDeviceNotify;
import com.imp.BluetoothDeviceTag;
import com.utils.ToastShowUtil;
import com.view.MyDialog;
import com.zhy.sample.folderlayout.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.TooManyListenersException;

/**
 * Created by fengx on 2018/1/4.
 */

public class BleLightControlActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "writeCallBack";
    private TextView mBle_title_text;
    private String mReadDevice = "a2b2";
    private String mac;
    public BleDevice mBleDevice;
    private TextView mTemperature;
    private TextView mDeviceDate;
    private TextView mBluetoothLightMainVersion;
    private TextView mBluetoothLightMode1Version;
    private TextView mBluetoothLightMode2Version;
    private boolean isInitData;
    private View mStall_green_layout;
    private RadioGroup mLight_stall;
    private RadioButton mStallNum1;
    private RadioButton mStallNum2;
    private RadioButton mStallNum3;
    private RadioButton mStallNum4;
    private RadioButton mStallNum5;
    private RadioButton mStallNum6;
    private RadioButton mStallNum7;

    private View mStall_gone_layout;
    private RadioGroup mLight_stall_gone;
    private RadioButton mStallNum1_gon;
    private RadioButton mStallNum2_gon;
    private RadioButton mStallNum3_gon;
    private RadioButton mStallNum4_gon;
    private RadioButton mStallNum5_gon;
    private RadioButton mStallNum6_gon;
    private RadioButton mStallNum7_gon;
    private ProgressBar mGray_progress;
    private ProgressBar mGreen_progress;
    private View mGress_bar_green;
    private View mGress_bar_gray;
    private RadioButton mManual_open;
    private RadioButton mManual_off;
    private RadioGroup mLight_home_man_radioGroup;
    private int mLva = 0; //档位控制
    private String postLightData;
    //    private MyDialog myDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    writeData(postLightData);
                    if (!isInitData) {
                        mHandler.sendEmptyMessage(2);
                    }
                    break;
            }
        }
    };
    private TextView mLight_energy_text;
    private int mResultLightMode;
    private Button mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initData();
        initListener();
    }

    protected int initLayout() {
        return R.layout.light_home_man_stand_layout;
    }


    protected void initView() {
        initToolBar();
//             myDialog = MyDialog.showDialog(this);
        mBle_title_text = (TextView) findViewById(R.id.ble_title_text);
        // 灯的设置的 节能模式的显示说明
        mLight_energy_text = (TextView) findViewById(R.id.light_energy_text);
        // 灯的温度
        mTemperature = (TextView) findViewById(R.id.device_temperature);
        //灯的 运行时间
        mDeviceDate = (TextView) findViewById(R.id.device_date);
        //主板 版本号
        mBluetoothLightMainVersion = (TextView) findViewById(R.id.BluetoothLightMainVersion);
        //模板1 版本号
        mBluetoothLightMode1Version = (TextView) findViewById(R.id.BluetoothLightMode1Version);
        //模板2 版本号
        mBluetoothLightMode2Version = (TextView) findViewById(R.id.BluetoothLightMode2Version);

        //手动开启 和 手动关闭 的   按钮父容器
        mLight_home_man_radioGroup = (RadioGroup) findViewById(R.id.light_home_man_radioGroup);

        //进度条 green  的父布局
        mGress_bar_green = findViewById(R.id.gress_bar_green);
        //进度条 gray 的父布局
        mGress_bar_gray = findViewById(R.id.gress_bar_gray);
        //手动开启按钮
        mManual_open = (RadioButton) findViewById(R.id.manual_open);
        //手动关闭按钮
        mManual_off = (RadioButton) findViewById(R.id.manual_off);
        initLightGrayButton();
        initLightGreenButton();
    }

    private void initToolBar() {
        mNext = (Button) findViewById(R.id.next);

    }

    protected void initData() {
        mNext.setVisibility(View.VISIBLE); //设置下一步可见
        mNext.setText("设置");//设置按钮上 没有字体

        Intent intent = getIntent();
        mac = intent.getStringExtra(BluetoothDeviceTag.DEVICE_MAC);
        mBleDevice = intent.getParcelableExtra(BluetoothDeviceTag.KEY_DATA);
        isInitData = false;

    }

    protected void initListener() {
        //手动开启关闭 组合
        mLight_home_man_radioGroup.setOnCheckedChangeListener(this);
        // light greenRadioGroup  的 7 个档位
        mLight_stall.setOnCheckedChangeListener(this);
        // light gray RadioGroup  的 7 个档位
        mLight_stall_gone.setOnCheckedChangeListener(this);
        BaseBleManager instance = BaseBleManager.getInstance(getApplication());
        instance.notify_bleCallback(new MyBleNotifyCallback());
        postLightData = mReadDevice + mac;  //post给 light 的数据
        Log.e("initListener", "发送数据为:  " + postLightData);
        mHandler.sendEmptyMessage(2);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.manual_open:   //手动打开按钮 需要把green progressBar显示 还有 green RadioButton
                //选中 手动打开还是手动关闭 都需要把节能模式的  选中状态恢复的到默认状态
                setProgressBarViewVisibility(true, mLightGears);
//                myDialog.show();   显示 dialog
                mResultLightMode = 1;// 照明模式  手动开启
                writeBluetoothRequest(mLightGears + "", mResultLightMode + "");
                break;
            case R.id.manual_off:    //手动关闭按钮  需要把gray progressBar显示 还有 gray RadioButton
                ToastShowUtil.showToast(this, "manual_off  ");
                setProgressBarViewVisibility(false, mLightGears);
//                myDialog.show();   显示 dialog
                mResultLightMode = 0;// 照明模式  手动关闭
                writeBluetoothRequest(mLightGears + "", mResultLightMode + "");
                break;
            case R.id.num1:    //green radioButton 1
                mLva = 1;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num2:   //green radioButton 2
                mLva = 2;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num3:    //green radioButton 3
                mLva = 3;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num4:   //green radioButton 4
                mLva = 4;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num5:    //green radioButton 5
                mLva = 5;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num6:   //green radioButton 6
                mLva = 6;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num7:    //green radioButton 7
                mLva = 7;
                greenAndReayRequest(true, mLva);
                writeBluetoothRequest(mLva + "", mResultLightMode + "");
                break;
            case R.id.num1_gone:    //gray radioButton 1
                mLva = 1;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num2_gone:    //gray radioButton 2
                mLva = 2;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num3_gone:    //gray radioButton 3
                mLva = 3;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num4_gone:   //gray radioButton 4
                mLva = 4;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num5_gone:   //gray radioButton 5
                mLva = 5;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num6_gone:   //gray radioButton 6
                mLva = 6;
                greenAndReayRequest(false, mLva);
                break;
            case R.id.num7_gone:    //gray radioButton 7
                mLva = 7;
                greenAndReayRequest(false, mLva);
                break;
            default:
                break;
        }
    }

    /**
     * @param gears     档位
     * @param lightMode 灯的模式
     */
    public void writeBluetoothRequest(String gears, String lightMode) {
        String lightMac = numToHex16(Integer.parseInt(mMainMacNumber));
        String lightGears = numToHex8(Integer.parseInt(gears));
        String lightModes = numToHex8(Integer.parseInt(lightMode));
        postLightData = "a3b3" + lightMac + lightModes + lightGears;
//        sadfdsffas
        isInitData = false;

        mHandler.sendEmptyMessage(2);
    }

    private String mMainMacNumber;
    private int mLightGears;


    class MyBleNotifyCallback extends BleNotifyCallback {
        @Override
        public void onNotifySuccess() {
        }

        @Override
        public void onNotifyFailure(BleException exception) {
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            String resultData = bytesToHexString(data);
            if (resultData.contains("b2a2")) {
                LightBluetoothMode mLightParameter = getB2A2(resultData);
                if (mLightParameter != null) {
                    isInitData = true;
                    mBle_title_text.setText(mLightParameter.getAreaNumber() + "-" + Integer.parseInt(mac, 16));
                    mTemperature.setText(mLightParameter.getTemperature());
                    mDeviceDate.setText(mLightParameter.getActivateDate());
                    // 获取到灯 的 照明模式  0 手动关闭  1 手动开启  2 节能模式(就是手动开启和手动关闭都默认不选)
                    mResultLightMode = Integer.parseInt(mLightParameter.getLightMode());
                    //    灯的档位     作为全局变量(因为设置灯的档位时 需要发送给灯)
                    mLightGears = Integer.parseInt(mLightParameter.getLightGears());
                    // 获取到主 设备mac 地址  作为全局变量(因为设置灯的档位时 需要发送给灯)
                    mMainMacNumber = mLightParameter.getMacNumber();
                    //更新进度条 并且显示 档位(自动处理是否显示灰色档位 或者 绿色档位)
                    upDataLightMode(mResultLightMode, mLightGears);
                    mBluetoothLightMainVersion.setText("主板 " + mLightParameter.getMainVersionNumber());
                    mBluetoothLightMode1Version.setText("模式I " + mLightParameter.getMode1Version());
                    mBluetoothLightMode2Version.setText("模式II " + mLightParameter.getMode2Version());
                }
            } else {   //  if (resultData.contains("a3b3"))
                String mHead = resultData.substring(0, 4); // 校验开头
                if ("b3a3".equals(mHead)) {   //包头正确 正常解析
                    isInitData = true;
                    String mMac = resultData.substring(4, 8); // 主 mac 地址 0035
                    int macNumber = Integer.parseInt(mMac, 16);// 主 mac 地址
                    String result = resultData.substring(8, 10); // 分区号 02
                    int areaNumber = Integer.parseInt(result, 16);
                } else {
                    ToastShowUtil.showToast(BleLightControlActivity.this, "包头错误" + mHead);
                }

            }

        }
    }

    /**
     * @param mLightMode  根据 回调返回的 灯的照明模式来设置   是否 手动开启 或者 手动关闭
     * @param mLightGears 设置 档位
     */
    public void upDataLightMode(int mLightMode, int mLightGears) {
        boolean mLightSwitch;  // 用来记录是 显示 还是 关闭 状态
        if (0 == mLightMode) {
            mManual_off.setChecked(true);  // 选中关闭
            // 手动关闭 显示灰色的progressBar  并显示所有的 灰色档位按钮 隐藏所有的绿色档位按钮
//            setProgressBarViewVisibility(false, mLightGears);
            mLightSwitch = false;
            mLight_energy_text.setVisibility(View.INVISIBLE);  // 如果是0  就 隐藏
        } else if (1 == mLightMode) { //
            mManual_open.setChecked(true);  // 选中打开  手动打开 显示绿色的progressBar  并显示所有的 灰色档位按钮 隐藏所有的绿色档位按钮
//            setProgressBarViewVisibility(true, mLightGears);
            mLightSwitch = true;
            mLight_energy_text.setVisibility(View.INVISIBLE);  // 如果是0  就 隐藏
        } else {  //mLightMode  == 2  两个都不选中
            mLightSwitch = true;
            mLight_energy_text.setVisibility(View.VISIBLE);  // 如果是2  就 显示
        }
        // 手动关闭或者开启  progressBar  并显示所有的  档位按钮
        setProgressBarViewVisibility(mLightSwitch, mLightGears);
    }

    //绿色档位控件
    private void initLightGreenButton() {
        //绿色档位group的布局
        mStall_green_layout = findViewById(R.id.light_stall_layout);
        //green light的 照度 档位
        mLight_stall = (RadioGroup) findViewById(R.id.light_stall);
        //green 照明档位1
        mStallNum1 = (RadioButton) findViewById(R.id.num1);
        //green 照明档位2
        mStallNum2 = (RadioButton) findViewById(R.id.num2);
        //green 照明档位3
        mStallNum3 = (RadioButton) findViewById(R.id.num3);
        //green 照明档位4
        mStallNum4 = (RadioButton) findViewById(R.id.num4);
        //green 照明档位5
        mStallNum5 = (RadioButton) findViewById(R.id.num5);
        //green 照明档位6
        mStallNum6 = (RadioButton) findViewById(R.id.num6);
        //green 照明档位7
        mStallNum7 = (RadioButton) findViewById(R.id.num7);
        //green 进度条
        mGreen_progress = (ProgressBar) findViewById(R.id.light_green_progress);
    }

    //灰色档位控件
    private void initLightGrayButton() {
        //灰色档位group的布局
        mStall_gone_layout = findViewById(R.id.light_stall_gone_layout);
        //gray light的 照度 档位
        mLight_stall_gone = (RadioGroup) findViewById(R.id.light_stall_gone);
        //gray 照明档位1
        mStallNum1_gon = (RadioButton) findViewById(R.id.num1_gone);
        //gray 照明档位2
        mStallNum2_gon = (RadioButton) findViewById(R.id.num2_gone);
        //gray 照明档位3
        mStallNum3_gon = (RadioButton) findViewById(R.id.num3_gone);
        //gray 照明档位4
        mStallNum4_gon = (RadioButton) findViewById(R.id.num4_gone);
        //gray 照明档位5
        mStallNum5_gon = (RadioButton) findViewById(R.id.num5_gone);
        //gray 照明档位6
        mStallNum6_gon = (RadioButton) findViewById(R.id.num6_gone);
        //gray 照明档位7
        mStallNum7_gon = (RadioButton) findViewById(R.id.num7_gone);
        //gray 进度条
        mGray_progress = (ProgressBar) findViewById(R.id.light_gray_progress);


    }

    /**
     * 用来 记录是 绿色档位还是 灰色档位  绿色是false  true 是灰色
     */
    private boolean isChecked;

    /**
     * 此函数用来 更新UI (设置所有档位的 显示于隐藏  并且设置progressBar 的 进度)
     *
     * @param greenProgressBarBarVisibility 设置false则 显示所有的 灰色档位 隐藏所有的绿色档位
     * @param lvaint                        更新进度条的进度(也是档位数据)
     */
    public void setProgressBarViewVisibility(boolean greenProgressBarBarVisibility, int lvaint) {
        ProgressBar mFinalProgressBar; // 用来记录最后留下了progressBar 然后用于更新进度条
        RadioGroup mLight_Group;
        if (greenProgressBarBarVisibility) {
            mLight_stall_gone.setVisibility(View.INVISIBLE); //隐藏灰色档位 RadioButton
            mLight_stall.setVisibility(View.VISIBLE);   //显示绿色档位 的RadioButton
            mGress_bar_gray.setVisibility(View.GONE); //隐藏灰色绿色 progressBar
            mGress_bar_green.setVisibility(View.VISIBLE); //显示绿色 progressBar
            mFinalProgressBar = mGreen_progress;
            isChecked = false;  //让所有的档位 都不能被点击
            mLight_Group = mLight_stall; // 用来记录 是哪个 radioGroup
        } else {   // 修改此处
            mLight_stall.setVisibility(View.INVISIBLE);     //隐藏绿色档位 的RadioButton
            mLight_stall_gone.setVisibility(View.VISIBLE); //显示灰色档位 RadioButton*/
            mGress_bar_green.setVisibility(View.GONE);   //隐藏绿色 progressBar
            mGress_bar_gray.setVisibility(View.VISIBLE); //显示灰色 progressBar
            mStall_gone_layout.setVisibility(View.VISIBLE);
            mFinalProgressBar = mGray_progress;
            mLight_Group = mLight_stall_gone; // 用来记录 是哪个 radioGroup
            isChecked = true;//让所有的档位   能被点击
        }
        updateGreenProgressBar(mFinalProgressBar, lvaint);
        upDataGears(mLight_Group, lvaint);

    }


    //更新 照明的档位
    private void upDataGears(RadioGroup group, int lva) {
        String mSrcLva = "1234567";
        if (lva == 0) {
            return;
        }
        if (!mSrcLva.contains(lva + "")) {
            ToastShowUtil.showToast(this, "档位数据错误");
            return;
        }
        updateGreenRadioButtonChecked(group, lva); //更新绿色档位
    }

    //用来循环改变 当前以外的所有的radioButton的 点击状态

    /**
     * @param mLight_stall 档位的外层的group
     * @param lva          返回的档位
     */
    public void updateGreenRadioButtonChecked(RadioGroup mLight_stall, int lva) {
        lva--;
        for (int i = 0; i < mLight_stall.getChildCount(); i++) {
            RadioButton stallBt = (RadioButton) mLight_stall.getChildAt(i);
            if (!isChecked) {
                if (i == lva) { //把不是 这个档位ID 的都给设置成不可点击并且换掉background
                    stallBt.setChecked(true); //都设置成 选中状态
                }
            } else {  // 这里说明  是 灰色  那所有档位都不能被点击
                if (i == lva) { //把不是 这个档位ID 的都给设置成不可点击并且换掉background
                    stallBt.setChecked(true); //都设置成 选中状态
                } else {
                    stallBt.setEnabled(false); //都设置成不能点击状态
                }
            }
        }
    }


    //更新进度条
    private void updateGreenProgressBar(ProgressBar mFinalProgressBar, int lvaint) {
        mFinalProgressBar.setProgress(lvaint);
    }


    //发送数据前 更新进度条
    private void greenAndReayRequest(boolean green, int lva) {
        if (green) {
            if (!isChecked) {
                mGreen_progress.setProgress(lva);
            }
        } else {
            if (!isChecked) {
                mGray_progress.setProgress(lva);
            }
        }
    }


    public void click(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                Intent intent = new Intent(this, BluetoothLightSettingActivity.class);
                intent.putExtra(BluetoothDeviceTag.KEY_DATA, mac);
                startActivity(intent);
                ToastShowUtil.showToast(this, "等下设置");
                break;
        }
    }

    public void writeData(String data) {
        Log.e("initListener", " 写入数据为: " + data);
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
                        Log.e(TAG, "onWriteFailure  发送数据到设备失败 " + exception.getDescription() + "   " + exception.getCode());
                    }
                });
    }

    /**
     * @param resultData 返回16进制的 转换之后的 字符串 进行此界面解析
     */
    public LightBluetoothMode getB2A2(String resultData) {
        /**
         * MAC（2） 分区号（1） 激活时间（3） 频段（1） 温度（1） 灯模式（1） 灯手动档位（1） 主板版本号（2） 模块1版本号（2）
         * 模块2版本号（2）
         */
//          resultData = "b2a2003502000000051d0207030001010300";
        LightBluetoothMode mode = new LightBluetoothMode();
        String substring = resultData.substring(0, 4); // 校验开头是不是b0a0 b0a000350105000f
        if (!"b2a2".equals(substring)) {
            ToastShowUtil.showToast(this, getString(R.string.BluetoothServerDataErr));
            return null;
        }
        substring = resultData.substring(4, 8); // 主 mac 地址 0035
        int macNumber = Integer.parseInt(substring, 16);// 主 mac 地址

        mode.setMacNumber(macNumber + "");
        substring = resultData.substring(8, 10); // 分区号 02
        int areaNumber = Integer.parseInt(substring, 16);

        mode.setAreaNumber(areaNumber + "");

        substring = resultData.substring(10, 12); // 激活时间
        int datel = Integer.parseInt(substring, 16);  // 年


        substring = resultData.substring(12, 14); // 激活时间
        int date2 = Integer.parseInt(substring, 16);// 年

        substring = resultData.substring(14, 16); // 激活时间
        int date3 = Integer.parseInt(substring, 16); // 日 16

        if (datel != 0) {
            String string = (datel + 2000) + "-" + date2 + "-" + date3 + " 00:00:00";
            Time date = new Time(); // 1515138537521
            // String ss = date.CalculateTime("2018-01-03 00:00:00");
            if (datel < 2018) {
                mode.setActivateDate("0h");
            } else {
                String ss = date.CalculateTime(string);
                mode.setActivateDate(ss);
            }

        } else {
            mode.setActivateDate("0h");
        }
        substring = resultData.substring(16, 18);// 频段 1 位
        int frequency = Integer.parseInt(substring, 16);
        mode.setFrequency(frequency + "");

        substring = resultData.substring(18, 20);// 温度 1 位
        int temperature = Integer.parseInt(substring, 16);
        mode.setTemperature(temperature + "");

        substring = resultData.substring(20, 22);// 灯模式 1 位
        int lightMode = Integer.parseInt(substring, 16);
        mode.setLightMode(lightMode + "");

        substring = resultData.substring(22, 24);// 灯手动档位 1 位
        int gears = Integer.parseInt(substring, 16);
        mode.setLightGears(gears + "");

        substring = resultData.substring(24, 26);// 主板版本号 2 位  拆分1
        int mainVersion1 = Integer.parseInt(substring, 16);
        substring = resultData.substring(26, 28);// 主板版本号 2 位  拆分2
        int mainVersion2 = Integer.parseInt(substring, 16);
        mode.setMainVersionNumber(mainVersion1 + "." + mainVersion2);// 合并主板版本号

        substring = resultData.substring(28, 30);// 模块1版本号 2 位   拆分1
        int mode1_0 = Integer.parseInt(substring, 16);
        substring = resultData.substring(30, 32);// 模块1版本号 2 位   拆分2
        int mode1_1 = Integer.parseInt(substring, 16);
        mode.setMode1Version(mode1_0 + "." + mode1_1); //合并后模块1版本号

        substring = resultData.substring(32, 34);// 模块2版本号 2 位  拆分1
        int mode2_0 = Integer.parseInt(substring, 16);
        substring = resultData.substring(34, 36);// 模块2版本号 2 位  拆分2
        int mode2_1 = Integer.parseInt(substring, 16);
        mode.setMode2Version(mode2_0 + "." + mode2_1);  //合并后模块2版本号

        return mode;
    }

    // 需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

    // 需要使用1字节表示b
    public static String numToHex8(int b) {
        return String.format("%02x", b);
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
