package com.all.activity;

import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.fengxun.base.BaseActivity;
import com.zhy.sample.folderlayout.R;

/**
 * 管理界面
 */

public class ManageMent extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RadioButton mRb_man;
    private RadioButton mRb_home;

    @Override
    protected int initLayout() {
        return R.layout.light_manage_ment;
    }

    @Override
    protected void initView() {
        mRb_man = (RadioButton) findViewById(R.id.rb_man);
        mRb_home = (RadioButton) findViewById(R.id.rb_home);
    }

    @Override
    protected void initData() {
        mRb_home.setChecked(true);

        mRb_man.setChecked(false);
    }

    @Override
    protected void initListener() {
        mRb_home.setOnCheckedChangeListener(this);
        mRb_man.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent mIntent = null;
        switch (((RadioButton) buttonView).getId()) {
            case R.id.rb_man:
                if (mIntent == null) {
                    mIntent = new Intent(this, LightingSystemActivity.class);
                    startActivity(mIntent);
                    finish();
                }
                break;
            case R.id.rb_home:
                mIntent = new Intent(this, ManageMent.class);
                startActivity(mIntent);
                break;
        }
    }
}
