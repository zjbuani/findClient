package com.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhy.sample.folderlayout.R;

public class MyDialog extends Dialog {

    private Context context;
    private static MyDialog dialog;
    private ImageView ivProgress;


    public MyDialog(Context context) {
        super(context);
        this.context = context;
        initBackground();
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initBackground();

    }

    private void initBackground() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置背景透明度 背景透明
        lp.alpha = 0.8f;//参数为0到1之间。0表示完全透明，1就是不透明。按需求调整参数
        window.setAttributes(lp);
    }

    //显示dialog的方法
    public static MyDialog showDialog(Context context){
        dialog = new MyDialog(context, R.style.MyDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_layout);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
           ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
            ivProgress.startAnimation(animation);
        }
    }
}