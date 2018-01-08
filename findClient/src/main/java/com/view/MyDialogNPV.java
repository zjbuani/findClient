package com.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.all.activity.SetModeCustom;
import com.zhy.sample.folderlayout.R;

import org.w3c.dom.Text;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by carbs on 2016/7/11.
 */

public class MyDialogNPV extends Dialog implements View.OnClickListener,
        NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener {

    private static final String TAG = "picker";

    private Context mContext;
    private Button mButtonGetInfo;
    private NumberPickerView mNumberPickerViewStartTime, mNumberPickerViewHour, mNumberPickerViewMinute;
    private TextView mStartHour;
    private TextView mStartMinute;
    private TextView mEndHour;
    private TextView mEndMinute;
    private boolean istrue = false;

    public MyDialogNPV(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog_npv);
        mNumberPickerViewStartTime = (NumberPickerView) this.findViewById(R.id.my_picker_hour);
        mNumberPickerViewHour = (NumberPickerView) this.findViewById(R.id.my_picker_minute);
        mNumberPickerViewMinute = (NumberPickerView) this.findViewById(R.id.my_picker_half_day);
        mStartHour = (TextView) findViewById(R.id.start_hour);
        mStartMinute = (TextView) findViewById(R.id.start_minute);
        mEndHour = (TextView) findViewById(R.id.end_hour);
        mEndMinute = (TextView) findViewById(R.id.end_minute);
        mNumberPickerViewStartTime.setOnScrollListener(this);
        mNumberPickerViewHour.setOnScrollListener(this);
        mNumberPickerViewMinute.setOnScrollListener(this);
        mNumberPickerViewStartTime.setOnValueChangedListener(this);
        mNumberPickerViewHour.setOnValueChangedListener(this);
        mNumberPickerViewMinute.setOnValueChangedListener(this);

//        mNumberPickerView.refreshByNewDisplayedValues(mDisplayValues);

        mButtonGetInfo = (Button) this.findViewById(R.id.button_get_info);
        mButtonGetInfo.setOnClickListener(this);
    }

    // this method should be called after onCreate()
    public void initNPV() {
        String[] valuse1 = {"开始时间", "结束时间"};
        String[] valuse2 = new String[24];
        String[] valuse3 = new String[60];
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                valuse2[i] = "0" + i;
            } else {
                valuse2[i] = i + "";
            }
        }
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                valuse3[i] = "0" + i;
            } else {
                valuse3[i] = i + "";
            }

        }

//        mNumberPickerView.refreshByNewDisplayedValues(mDisplayValues);
        mNumberPickerViewStartTime.refreshByNewDisplayedValues(valuse1);
        mNumberPickerViewHour.refreshByNewDisplayedValues(valuse2);
        mNumberPickerViewMinute.refreshByNewDisplayedValues(valuse3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_info:
                String h = mNumberPickerViewStartTime.getContentByCurrValue();
                String m = mNumberPickerViewHour.getContentByCurrValue();
                String d = mNumberPickerViewMinute.getContentByCurrValue();

                SetModeCustom mContext = (SetModeCustom) this.mContext;
                String setTime = mStartHour.getText().toString().trim() + mStartMinute.getText().toString().trim() + mEndHour.getText().toString().trim() + mEndMinute.getText().toString().toString();
                mContext.dialogCallBack(setTime);
                this.dismiss();
                break;
        }
    }


    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {
        Log.d(TAG, "onScrollStateChange : " + scrollState);
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        String[] content = picker.getDisplayedValues();
        if (content != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
//                getString(R.string.current_thread_name);
                switch (picker.getId()) {
                    case R.id.my_picker_hour:
                  /*      Toast.makeText(mContext.getApplicationContext(), "开始时间 oldVal" + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();*/
                        break;
                    case R.id.my_picker_minute:
                      /*  Toast.makeText(mContext.getApplicationContext(), "时 oldVal" + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();*/
                        if (istrue) {
                            mStartHour.setText(mNumberPickerViewHour.getContentByCurrValue());
                        }
                        if (!istrue){
                            mEndHour.setText(mNumberPickerViewHour.getContentByCurrValue());
                        }
                        break;
                    case R.id.my_picker_half_day:

                        if (istrue) {
                            mStartMinute.setText(mNumberPickerViewMinute.getContentByCurrValue());
                        }
                        if (!istrue){

                            mEndMinute.setText(mNumberPickerViewMinute.getContentByCurrValue());
                        }
                        Toast.makeText(mContext.getApplicationContext(), "分 oldVal" + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();
                        break;
                }

            } else {
                Message message = Message.obtain();
                switch (picker.getId()) {
                    case R.id.my_picker_hour:
                      /*  Toast.makeText(mContext.getApplicationContext(), "else 开始时间oldVal " + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();*/
                        if (newVal == 0) {
                            istrue = true;
                        }
                        if (newVal == 1) {
                            istrue = false;
                        }
                        mNumberPickerViewHour.setValue(oldVal);
                        mNumberPickerViewMinute.setValue(oldVal);
                        break;

                }
            /*    Toast.makeText(mContext.getApplicationContext(), oldVal + " " + newVal, Toast.LENGTH_SHORT)
                        .show();*/
           /*     message.obj = mContext.getString(R.string.current_thread_name) + Thread.currentThread().getName()
                        + "\n" + mContext.getString(R.string.current_picked_value) + String.valueOf(newVal);
                mHandler.sendMessage(message);*/
            }


            Log.d(TAG, "onValueChange content : " + content[newVal - picker.getMinValue()]);
//            Toast.makeText(mContext.getApplicationContext(), "oldVal : " + oldVal + " newVal : " + newVal + "\n", Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    private void getCurrentContent() {
        String[] content = mNumberPickerViewStartTime.getDisplayedValues();
//        if (content != null)

          /*  Toast.makeText(mContext.getApplicationContext(), content[mNumberPickerViewStartTime.getValue() - mNumberPickerViewStartTime.getMinValue()],
                    Toast.LENGTH_SHORT)
                    .show();*/
    }
}