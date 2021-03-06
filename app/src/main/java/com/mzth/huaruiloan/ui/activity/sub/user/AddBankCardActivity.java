package com.mzth.huaruiloan.ui.activity.sub.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;

/**
 * Created by Administrator on 2017/5/11.
 * 添加银行卡
 */

public class AddBankCardActivity extends BaseBussActivity {
    private ImageView iv_back;
    private TextView tv_big_title;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = AddBankCardActivity.this;
        setContentView(R.layout.activity_add_card);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("添加银行卡");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
            }
        }
    };
}
