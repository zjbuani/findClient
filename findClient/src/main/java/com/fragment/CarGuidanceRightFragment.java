package com.fragment;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.adapter.RightAdapter;
import com.fengxun.base.CarRightBaseFragment;
import com.fengxun.base.LightRightBaseFragment;
import com.http.response.bean.BuildingFloorBean;
import com.zhy.sample.folderlayout.R;

/**
 * @author 系统照明右侧菜单
 */
public class CarGuidanceRightFragment extends CarRightBaseFragment {

    private ListView mMenus;
    private BuildingFloorBean signInBean;
    private ExpandableListView mExplistview;
    private RightAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.light_right_layout, null);
        signInBean = (BuildingFloorBean) getArguments().getSerializable(FragmentTag.RIGHT);
        mExplistview = (ExpandableListView) view.findViewById(R.id.light_right_menu_lv);
        return view;
    }

    @Override
    public void initData() {
        String[] allFloorNames = new String[]{"全部", "楼层", "分区"};
        int result = signInBean.getResult();
        if (result == 0) {
            return;
        }
        adapter = new RightAdapter(mContext, signInBean, allFloorNames);
        mExplistview.setGroupIndicator(null); //去掉条目上的 小箭头
        mExplistview.setAdapter(adapter);
    }

    @Override
    protected void initListner() {
        mExplistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // LightingSystemActivity_test  的回调函数
                mContext.rightGroupItem(groupPosition, childPosition);
                mContext.toggle();
                parent.collapseGroup(groupPosition); // 指定位置 关闭group
//                parent.expandGroup(groupPosition) //  指定位置 打开group
                return true;
            }
        });
    }
}
