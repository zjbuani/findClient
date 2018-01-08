package com.all.activity;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fengxun.base.BaseActivity;
import com.google.gson.Gson;
import com.http.response.bean.SignInBean;
import com.request.RequstUtils;
import com.utils.MD5Utils;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import java.util.HashMap;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by fengx on 2017/6/20.
 */

public class SignInActivity extends BaseActivity {
    private Context context = this;
    private EditText mUser_name;
    private EditText mPwd;
    private String mPostUser;
    private String mPostPwd;

    private Button mSign_in_b;
    private PromptDialog promptDialog;


    //设置布局
    @Override
    protected int initLayout() {
        return R.layout.sign_in;
    }

    @Override
    protected void initView() {

        mUser_name = (EditText) findViewById(R.id.user_name);
        mPwd = (EditText) findViewById(R.id.pwd);
        //登录按钮
        mSign_in_b = (Button) findViewById(R.id.sign_in_b);


    }

    @Override
    protected void initData() {
        String mSaveuser = PrefUtils.getString(context, "user", "");
        String mSavePwd = PrefUtils.getString(context, "mPostPwd", "");
        mUser_name.setText(mSaveuser);
        mPwd.setText(mSavePwd);

    }

    //点击登录 按钮
    public void click(View view) {
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(3000);
        promptDialog.showLoading("正在登录");
        //获取到用户账号
        mPostUser = mUser_name.getText().toString().trim();
        //获取到用户密码
        mPostPwd = mPwd.getText().toString().trim();
        String mMD5pwd = MD5Utils.encode(mPostPwd);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", mPostUser);
        map.put("password", mMD5pwd);
        Log.e("mMD5pwd", mMD5pwd);
        RequstUtils.loadPostSign(RequstUtils.SIGNIN, map, new SignInListener(), new SignInErrListener());
//        1 获取到 账户和密码 发送服务器  进行验证

//        2 如果可以登陆 则登陆  如果服务器返回数据为 0 则访问本地数据库

//        3 如果本地数据库查询失败则 不给予登陆 切不缓存密码
    }

    class SignInErrListener implements Response.ErrorListener { //请求失败
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("错误的请求", "onErrorResponseonErrorResponseonErrorResponse");
        }
    }

    class SignInListener implements Response.Listener {
        @Override
        public void onResponse(Object o) {
            Gson gson = new Gson();
            final SignInBean userBean = gson.fromJson(o.toString(), SignInBean.class);
            if (userBean.getResult() == null) {
                promptDialog.showError("登录失败");
            }
            if (!userBean.getResult().equals("1")){
                promptDialog.showError("登录失败");
                ToastShowUtil.showToast(SignInActivity.this, "登录失败用户名不存在");
            }else {
                promptDialog.showSuccess("登陆成功");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userBean == null) {
                        ToastShowUtil.showToast(SignInActivity.this, "服务器数据错误");
                        return;
                    }
                    int user_type = Integer.parseInt(userBean.getResult());

                    if (user_type == 0) {
                        ToastShowUtil.showToast(SignInActivity.this, "登录失败用户名不存在");
                    } else if (user_type == 2) {
                        ToastShowUtil.showToast(SignInActivity.this, "登录失败密码错误");
                    } else if (user_type == 1) {
                        PrefUtils.setString(context, "user", mPostUser);
                        PrefUtils.setString(context, "mPostPwd", mPostPwd);
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.putExtra("SignInBean", userBean);
                        context.startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }


}
