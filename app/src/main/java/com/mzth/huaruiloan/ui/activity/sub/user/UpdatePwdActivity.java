package com.mzth.huaruiloan.ui.activity.sub.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.ui.activity.sub.PwdReSetSureActivity;

/**
 * Created by Administrator on 2017/5/11.
 * 修改密码
 */

public class UpdatePwdActivity extends BaseBussActivity {
    private Button btn_sure;
    private ImageView iv_back;
    private TextView tv_big_title;
    private EditText et_user_pwd01,et_user_pwd02,et_user_pwd03;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = UpdatePwdActivity.this;
        setContentView(R.layout.activity_reset_pwd);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //下一步
        btn_sure = (Button) findViewById(R.id.btn_sure);
        //原始密码
        et_user_pwd01 = (EditText) findViewById(R.id.et_user_pwd01);
        //新密码
        et_user_pwd02 = (EditText) findViewById(R.id.et_user_pwd02);
        et_user_pwd03 = (EditText) findViewById(R.id.et_user_pwd03);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("修改密码");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        btn_sure.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.btn_sure://下一步

                    break;
            }
        }
    };
}
