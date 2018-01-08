package com.utils;

import android.util.Log;
import android.widget.ImageView;

import com.weather.bean.Daily;
import com.zhy.sample.folderlayout.R;

import java.util.Calendar;

/**
 * Created by fengx on 2017/6/27.
 */

public class DateUtils {
  static   int[] weatherResource = {
            R.drawable.a0,
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,
            R.drawable.a9,
            R.drawable.a10,
            R.drawable.a11,
            R.drawable.a12,
            R.drawable.a13,
            R.drawable.a14,
            R.drawable.a15,
            R.drawable.a16,
            R.drawable.a17,
            R.drawable.a18,
            R.drawable.a19,
            R.drawable.a20,
            R.drawable.a21,
            R.drawable.a22,
            R.drawable.a23,
            R.drawable.a24,
            R.drawable.a25,
            R.drawable.a26,
            R.drawable.a27,
            R.drawable.a28,
            R.drawable.a29,
            R.drawable.a30,
            R.drawable.a31,
            R.drawable.a32,
            R.drawable.a33,
            R.drawable.a34,
            R.drawable.a35,
            R.drawable.a36,
            R.drawable.a37,
            R.drawable.a38
    };
    public static void setTime(Daily daily, ImageView imageView) {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        System.out.println("hour " + hour);
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        System.out.println("minute " + minute);
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        System.out.println("从0:00分开是到目前为止的分钟数 " + minuteOfDay);
        final int start = 0 * 60 + 20;// 起始时间 00:20的分钟数
        System.out.println("start " + start);
        final int end = 8 * 60;// 结束时间 8:00的分钟数
        System.out.println("end " + end);

        if (minuteOfDay >= 8 * 60 && minuteOfDay <= 11 * 60) {
            Log.e("Log", "上午");
            String code_day = daily.getCode_day();
            Log.e("Log", code_day);
            int mCode_day = Integer.parseInt(code_day);
            imageView.setImageResource(weatherResource[mCode_day]);

        } else if (minuteOfDay >= 11 * 60 && minuteOfDay <= 13 * 60) {
            Log.e("Log", "中午");
            String code_day = daily.getCode_day();
            Log.e("Log", code_day);
            int mCode_day = Integer.parseInt(code_day);
            imageView.setImageResource(weatherResource[mCode_day]);
        } else if (minuteOfDay >= 14 * 60 && minuteOfDay <= 17 * 60) {
            Log.e("Log", "下午");
            String code_day = daily.getCode_day();
            Log.e("Log", code_day);
            int mCode_day = Integer.parseInt(code_day);
            imageView.setImageResource(weatherResource[mCode_day]);
        } else if (minuteOfDay >= 17 * 60 && minuteOfDay <= 18 * 60) {
            Log.e("Log", "傍晚");
            String code_night = daily.getCode_night();
            Log.e("Log", code_night);
            int mCode_night = Integer.parseInt(code_night);
            imageView.setImageResource(weatherResource[mCode_night]);
        } else if (minuteOfDay >= 18 * 60 && minuteOfDay <= 22 * 60) {
            Log.e("Log", "晚上");
            String code_night = daily.getCode_night();
            Log.e("Log", code_night);
            int mCode_night = Integer.parseInt(code_night);
            imageView.setImageResource(weatherResource[mCode_night]);
        } else if (minuteOfDay >= 19 * 60 && minuteOfDay <= 5 * 60) {
            Log.e("Log", "夜间");
            String code_night = daily.getCode_night();
            Log.e("Log", code_night);
            int mCode_night = Integer.parseInt(code_night);
            imageView.setImageResource(weatherResource[mCode_night]);


        }
    }
}
