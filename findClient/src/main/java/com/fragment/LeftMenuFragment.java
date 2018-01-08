package com.fragment;

import android.view.View;
import android.widget.ExpandableListView;

import com.adapter.Adapter;
import com.fengxun.base.BaseFragment;
import com.http.response.bean.SignInBean;
import com.zhy.sample.folderlayout.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class LeftMenuFragment extends BaseFragment {

    private ExpandableListView mExplistview;

    private SignInBean signInBean;
    private Adapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_left_menu, null);

        signInBean = (SignInBean) getArguments().getSerializable("SignInBean");

        mExplistview = (ExpandableListView) view.findViewById(R.id.id_left_menu_lv);

        return view;
    }

    @Override
    public void initData() {
        //设置悬浮头部VIEW
        List<SignInBean.CityListJavaBean> mCityList = signInBean.getCity_list();

        List<SignInBean.BuildListJavaBean> mBuild_list = signInBean.getBuild_list();
        HashMap<String, List<SignInBean.BuildListJavaBean>> map = new HashMap<>();
        ArrayList<String> mGroup_List_key = new ArrayList<>();
        for (int i = 0; i < mCityList.size(); i++) {
            ArrayList<SignInBean.BuildListJavaBean> mChild_name = new ArrayList<>();
            for (int y = 0; y < mBuild_list.size(); y++) {
                if (mCityList.get(i).getCity_id().equals(mBuild_list.get(y).getCity_id())) { //两个城市ID 一致说明次是 此城市下的项目
                    mChild_name.add(mBuild_list.get(y));
                }
            }
            mGroup_List_key.add(mCityList.get(i).getCity_name());
            map.put(mCityList.get(i).getCity_name(), mChild_name);
        }

        adapter = new Adapter(mContext, map, mGroup_List_key);

        mExplistview.setGroupIndicator(null); //去掉条目上的 小箭头
        mExplistview.setAdapter(adapter);

    }

    @Override
    protected void initListner() {
        mExplistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                SignInBean.BuildListJavaBean child = (SignInBean.BuildListJavaBean) adapter.getChild(groupPosition, childPosition);
                String build_name = child.getBuild_name();
//                ToastShowUtil.showToast(mContext, "第" + groupPosition + "组的第" + childPosition + "被点击了,内容是 : " +  build_name);
                mContext.leftGroupItem(child);
                mContext.toggle();
                parent.collapseGroup(groupPosition); // 指定位置 关闭group
//                parent.expandGroup(groupPosition) //  指定位置 打开group
                return true;
            }
        });
    }
}
