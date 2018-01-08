package com.all.activity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengxun.base.BaseActivity;
import com.fengxun.base.MyApplication;
import com.http.response.bean.SignInBean;
import com.utils.PrefUtils;
import com.utils.ToastShowUtil;
import com.zhy.sample.folderlayout.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

/**
 * Created by fengx on 2017/9/22.
 */

public class HomeSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mtitle;
    private ImageView mString;
    private ImageView mBack;
    private Button mConnect;
    private TextView mConnect_tv;
    private String[] arr = {"内网", "外网"};
    private Button mLogout;
    private SignInBean.BuildListJavaBean isleft;
    private SignInBean mSignInBean;

    @Override
    protected int initLayout() {
        return R.layout.setting_layout;
    }

    @Override
    protected void initView() {
        mtitle = (TextView) findViewById(R.id.title_bar);
        mString = (ImageView) findViewById(R.id.all_home_set);
        mBack = (ImageView) findViewById(R.id.home_bl);//返回按钮
        mConnect = (Button) findViewById(R.id.connect); //选择是内网还是外网
        mConnect_tv = (TextView) findViewById(R.id.connect_tv); //返回显示的事内网还是外网
        mLogout = (Button) findViewById(R.id.logout);   //用户注销

    }

    @Override
    protected void initData() {
        //设置title
        mtitle.setText("设置");
        //隐藏设置按钮
        mString.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        isleft = (SignInBean.BuildListJavaBean) intent.getSerializableExtra("isleft");
        if (isleft == null) {
            mSignInBean = (SignInBean) intent.getSerializableExtra("SignInBean");// 是默认的
            updateUI(null, mSignInBean);
        } else {
            updateUI(isleft, null);
        }
    }

    private void updateUI(SignInBean.BuildListJavaBean isleft, SignInBean signin) {
        String user_netlimit;
        if (signin == null) {
            user_netlimit = isleft.getUser_netlimit();
        } else {
            user_netlimit = signin.getBuild_list().get(0).getUser_netlimit();
        }
        if (user_netlimit != null) {
            if ("1".equals(user_netlimit)) {
                mConnect_tv.setText("内网");
                mConnect.setEnabled(false);
            } else if ("3".equals(user_netlimit)) {
                mConnect_tv.setText("外网");
                mConnect.setEnabled(true);
            }
        }
    }

    /**
     * 注销用户登录
     */
    private void logOut() {
        PrefUtils.setString(this, "user", "");
        PrefUtils.setString(this, "mPostPwd", "");
        ToastShowUtil.showToast(this, "注销成功");
        mLogout.setEnabled(true);
    }

    @Override
    protected void initListener() {
        mBack.setOnClickListener(this);
        mConnect.setOnClickListener(this);
        mLogout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_bl:
                this.finish();
                break;
            case R.id.connect:
                showDialog();
                break;
            case R.id.logout:
                mLogout.setEnabled(false); //设置不可点击
                logOut();
                break;
        }

    }


    private void showDialog() {
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setSingleChoiceItems(arr, 1, new DialogInterface.OnClickListener() { // 参数2  设置默认选项
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mConnect_tv.setText(arr[which]);  //显示是内网 还是外网
                MyApplication application = (MyApplication) getApplication();
                if (which == 0) {
                    application.user_netlimit = "1";
                } else if (which == 1) {
                    application.build_peanut = isleft.getBuild_peanut();
                    application.user_netlimit = "2";
                }
                dialog.dismiss();
            }
        });
        mDialog.show();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left,R.anim.activity_right);  //activity的 关闭动画 右出左进
    }


}
