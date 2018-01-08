package com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.http.response.bean.SignInBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengx on 2017/10/10.
 */

public abstract class BaseExpandableAdapter extends BaseExpandableListAdapter {
    private  ArrayList<String> mGroup_List_key;
    private Context context;
    protected  HashMap<String, List<SignInBean.BuildListJavaBean>> groupList;


     public BaseExpandableAdapter(Context context,  HashMap<String, List<SignInBean.BuildListJavaBean>> groupList, ArrayList<String> mGroup_List_key) {
        this.context = context;
        this.groupList = groupList;
         this.mGroup_List_key= mGroup_List_key;
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        String key = mGroup_List_key.get(groupPosition);
        return groupList.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroup_List_key.get(groupPosition);
    }
    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = mGroup_List_key.get(groupPosition);

        return  groupList.get(key).get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder mGroupHolder;
        if (convertView == null) {
            mGroupHolder = new GroupHolder();
            view = View.inflate(parent.getContext(), groupLayout(), null);
            mGroupHolder.groupTv = (TextView) view.findViewById(groupLayoutTvId());
            view.setTag(mGroupHolder);
        } else {
            view = convertView;
            mGroupHolder = (GroupHolder) view.getTag();

        }
        String mCityName = mGroup_List_key.get(groupPosition);
        mGroupHolder.groupTv.setText(mCityName);
        return view;
    }

    protected abstract int groupLayoutTvId();

    //分组内的 TextView
    protected abstract int groupChildTvId();

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        GroupChildHolder mChildHolder;
        if (convertView == null) {
            mChildHolder = new GroupChildHolder();
            view = View.inflate(context, groupChildLayout(), null);
            mChildHolder.groupChildTv = (TextView) view.findViewById(groupChildTvId());
            view.setTag(mChildHolder);
        } else {
            view = convertView;
            mChildHolder = (GroupChildHolder) view.getTag();
        }
        String key = mGroup_List_key.get(groupPosition);
        List<SignInBean.BuildListJavaBean> strings = groupList.get(key);
        SignInBean.BuildListJavaBean buildListJavaBean = strings.get(childPosition);
        String mChild_ = buildListJavaBean.getBuild_name();
        mChildHolder.groupChildTv.setText(mChild_);
        return view;
    }


    protected abstract int groupChildLayout();

    //设置为 true 是让子item 可以点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {


        return true;
    }


    //group 布局
    protected abstract int groupLayout();

    class GroupHolder {
        private TextView groupTv;
    }
    class GroupChildHolder {
        private TextView groupChildTv;
    }
}
