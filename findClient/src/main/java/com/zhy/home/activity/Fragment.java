package com.zhy.home.activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhy.sample.folderlayout.R;




/**
 * Created by fengx on 2017/6/15.
 */

public class Fragment extends android.app.Fragment {
    int[]  icon = {R.drawable.lightbtn ,R.drawable.carbtn,R.drawable.envbtn2x,R.drawable.adsbtn,R.drawable.workbtn,R.drawable.carsharebtn};

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.materialrecents_activity_recents, container, false);
        //这个是一个 FrameLayout  它实现了 手势识别和  手势事件处理
        RecentsList recents = (RecentsList) view.findViewById(R.id.recents);

        recents.setAdapter(new RecentsAdapter() {
            @Override
            public View getView(int position) {
                ImageView iv = new ImageView(getActivity());
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                iv.setImageResource(icon[position]);

                iv.setBackgroundColor(0);  //给这个图片设置 背景
                return iv;
            }

            @Override
            public int getCount() {

                return 5;
            }
        });
        recents.setOnItemClickListener(new RecentsList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(view.getContext(), "Card " + i + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
