package com.mzth.huaruiloan.ui.activity.sub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseBussActivity {

    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = MainActivity.this;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,""))){
                    startActivity(LoginActivity.class,null);
                }else{
                    startActivity(HomeActivity.class,null);
                }

               onBackPressed();
            }
        };
        timer.schedule(task,3000);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
    }
}
