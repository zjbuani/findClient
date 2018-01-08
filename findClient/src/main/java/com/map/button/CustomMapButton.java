package com.map.button;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.all.activity.SetModeCustom;
import com.zhy.sample.folderlayout.R;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by fengx on 2017/11/16.
 */

public final class CustomMapButton extends Dialog implements View.OnClickListener,
        NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener {

    private static final String TAG = "picker";

    private Context mContext;

    private NumberPickerView mNumberPickerViewStartTime;

    private boolean istrue = false;

    public CustomMapButton(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_button_layout);
        mNumberPickerViewStartTime = (NumberPickerView) this.findViewById(R.id.custom_picker_hour);
        mNumberPickerViewStartTime.setOnScrollListener(this);
        mNumberPickerViewStartTime.setOnValueChangedListener(this);
    }

    // this method should be called after onCreate()
    public void initNPV() {
        String[] valuse1 = {"开始时间", "结束时间"};

        for (int i = 0; i <10 ; i++) {
            valuse1[i] = "" + i;
        }
        mNumberPickerViewStartTime.refreshByNewDisplayedValues(valuse1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_info:
                String h = mNumberPickerViewStartTime.getContentByCurrValue();

                SetModeCustom mContext = (SetModeCustom) this.mContext;
                mContext.dialogCallBack(mContext.toString());
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
                        Toast.makeText(mContext.getApplicationContext(), "开始时间 oldVal" + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.my_picker_minute:
                        Toast.makeText(mContext.getApplicationContext(), "时 oldVal" + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();

                        break;

                }

            } else {
                Message message = Message.obtain();
                switch (picker.getId()) {
                    case R.id.my_picker_hour:
                        Toast.makeText(mContext.getApplicationContext(), "else 开始时间oldVal " + oldVal + " newVal " + newVal, Toast.LENGTH_SHORT)
                                .show();
                        if (newVal == 0) {
                            istrue = true;
                        }
                        if (newVal == 1) {
                            istrue = false;
                        }

                        break;

                }
            }


            Log.d(TAG, "onValueChange content : " + content[newVal - picker.getMinValue()]);

        }
    }

    private void getCurrentContent() {
        String[] content = mNumberPickerViewStartTime.getDisplayedValues();
        if (content != null)

            Toast.makeText(mContext.getApplicationContext(), content[mNumberPickerViewStartTime.getValue() - mNumberPickerViewStartTime.getMinValue()],
                    Toast.LENGTH_SHORT)
                    .show();
    }

}
