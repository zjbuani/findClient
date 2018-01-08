package com.zhy.sample.folderlayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 主页面fragment
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class RightMenuFragment extends Fragment {

	private ListView mMenus;
	private String[] mMenuItemStr = {"RightBear", "RightBird", "RightCat", "RightTigers", "RightPanda"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.right_fragment_layout, container,
				false);
		mMenus = (ListView) view.findViewById(R.id.right_list_layout);
		mMenus.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.item_left_menu, mMenuItemStr));

		return view;
	}

}
