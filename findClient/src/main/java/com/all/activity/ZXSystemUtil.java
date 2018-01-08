package com.all.activity;

import android.content.Context;

public class ZXSystemUtil {
    public static int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
}