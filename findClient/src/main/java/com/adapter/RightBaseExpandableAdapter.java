package com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.http.response.bean.BuildingFloorBean;
import com.http.response.bean.SignInBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengx on 2017/10/10.
 */

public abstract class RightBaseExpandableAdapter extends BaseExpandableListAdapter {
    private String[] allFloorNames;
    private BuildingFloorBean signInBean;
    private Context context;

    public RightBaseExpandableAdapter(Context context, BuildingFloorBean signInBean, String[] allFloorNames) {
        this.context = context;
        this.signInBean = signInBean;
        this.allFloorNames = allFloorNames;
    }

    @Override
    public int getGroupCount() {
        return allFloorNames == null ? 0 : allFloorNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return 1;
        } else if (groupPosition == 1) {
            return signInBean.getFloor_list().size();
        } else {
            return signInBean.getArea_list().size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0) {
            return "全车库";
        } else if (groupPosition == 1) {
            return signInBean.getFloor_list();
        } else {
            return signInBean.getArea_list();
        }

    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 1) {
            return signInBean.getFloor_list().get(childPosition);
        } else if (groupPosition == 2) {
            return signInBean.getArea_list().get(childPosition);
        } else {
            return "全车库";
        }
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
        GroupHolder mGroupHolder;
        if (convertView == null) {
            mGroupHolder = new GroupHolder();
            convertView = View.inflate(parent.getContext(), groupLayout(), null);
            mGroupHolder.groupTv = (TextView) convertView.findViewById(groupLayoutTvId());
            convertView.setTag(mGroupHolder);
        } else {
            mGroupHolder = (GroupHolder) convertView.getTag();
        }
        mGroupHolder.groupTv.setText(allFloorNames[groupPosition]); //显示分组名称

        return convertView;
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
        if (groupPosition == 0) {
            mChildHolder.groupChildTv.setText("全车库");
        } else if (groupPosition == 1) {
            mChildHolder.groupChildTv.setText(signInBean.getFloor_list().get(childPosition).getFloor_name());
        } else {
            mChildHolder.groupChildTv.setText(signInBean.getArea_list().get(childPosition).getArea_name());
        }
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
