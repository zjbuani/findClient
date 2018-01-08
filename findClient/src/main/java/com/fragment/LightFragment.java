package com.fragment;

import android.view.View;
import android.widget.ListView;

import com.fengxun.base.BaseFragment;
import com.http.response.bean.SignInBean;
import com.zhy.sample.folderlayout.R;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class LightFragment extends BaseFragment {

    private ListView mMenus;
    private SignInBean signInBean;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.light_fragment_layout, null);


        return view;
    }


}
