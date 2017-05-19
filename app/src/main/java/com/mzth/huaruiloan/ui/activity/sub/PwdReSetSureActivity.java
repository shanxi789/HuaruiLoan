package com.mzth.huaruiloan.ui.activity.sub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;

/**
 * Created by Administrator on 2017/5/11.
 * 重置密码成功
 */

public class PwdReSetSureActivity extends BaseBussActivity {
    private Button btn_next;
    private ImageView iv_back;
    private TextView tv_big_title;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = PwdReSetSureActivity.this;
        setContentView(R.layout.activity_find_pwd03);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //下一步
        btn_next = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("重置密码");
        iv_back.setVisibility(View.GONE);//隐藏返回键
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        btn_next.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_next://下一步
                    startActivity(LoginActivity.class,null);
                    onBackPressed();
                    break;
            }
        }
    };
}
