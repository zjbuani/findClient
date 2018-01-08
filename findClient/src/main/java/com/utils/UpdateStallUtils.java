package com.utils;

import android.widget.RadioButton;

import com.zhy.sample.folderlayout.R;

/**
 * Created  对应档位更新
 */

public class UpdateStallUtils {
    //更新 照明的档位
    public static   void updateStall(RadioButton mStallNum, String lva , boolean isChecked) {
        if ("1".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio1_gray);
        } else if ("2".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio2_gray);
        } else if ("3".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio3_gray);
        } else if ("4".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio4_gray);
        } else if ("5".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio5_gray);
        } else if ("6".equals(lva)) {
            mStallNum.setChecked(isChecked);
            mStallNum.setBackgroundResource(R.drawable.radio6_gray);
        } else if ("7".equals(lva)) {
            mStallNum.setBackgroundResource(R.drawable.radio7_gray);
            mStallNum.setChecked(isChecked);
        } else {
//            mStallNum7.setBackground(mActivity.getResources().getDrawable(R.drawable.radio1_gray));

        }
    }

}
