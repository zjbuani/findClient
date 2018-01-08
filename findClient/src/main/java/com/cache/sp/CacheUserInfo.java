package com.cache.sp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fengx on 2017/6/20.
 */

public class CacheUserInfo {

    private  Context context;

    public CacheUserInfo(Context context) {
        this.context = context;
    }

    /**
     * 将此次登陆的账户信息存储下来
     * */
    public boolean saveAccount(String phoneNum) {
        // 获取SharedPreference
        SharedPreferences preference =context.getSharedPreferences("login_info",MODE_PRIVATE);
        // 获取editor
        SharedPreferences.Editor editor = preference.edit();
        // 存入数据
        editor.putString("user", phoneNum);
        // 提交存入文件中
        boolean b = editor.commit();
        return b;
    }
    /**
     * 获取存入SharedPreference中的账户
     *
     * @return
     * */
    public String getAccount() {
        // 获取SharedPreference
        SharedPreferences preference = context.getSharedPreferences("login_info",
                MODE_PRIVATE);
        // 获取存在SharedPreference中的用户名
        String user = preference.getString("user", "");
        return user;
    }

}
