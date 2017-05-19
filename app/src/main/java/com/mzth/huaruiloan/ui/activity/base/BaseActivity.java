package com.mzth.huaruiloan.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.MainApplication;
import com.mzth.huaruiloan.util.StatusBarCompat;


/**
 * Created by leeandy007 on 2017/3/11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Activity _context;
    protected MainApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //避免 输入法将页面顶出去
        super.onCreate(savedInstanceState);
        application = (MainApplication) this.getApplication();
        application.addActivity(this);
        //StatusBarCompat.compat(this, Color.RED);//沉浸式状态栏

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setCustomLayout(savedInstanceState);
        initView();
        BindComponentEvent();
        initData();


    }
    /**
     * 初始化布局
     * */
    protected abstract void setCustomLayout(Bundle savedInstanceState);

    /**
     * 初始化控件
     * */
    protected abstract void initView();

    /**
     * 绑定控件事件
     * */
    protected abstract void BindComponentEvent();

    /**
     * 初始化数据
     * */
    protected abstract void initData();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            doActivityResult(requestCode, intent);
        }
    }

    /**
     * 带返回值跳转的数据的处理方法
     * */
    protected abstract void doActivityResult(int requestCode, Intent intent);

}
