package com.mzth.huaruiloan.ui.activity.sub.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.bean.HistoryBean;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.ui.adapter.sub.BankAdapter;
import com.mzth.huaruiloan.ui.adapter.sub.HistoryAdapter;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.widget.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 * 银行卡页面
 */

public class BankCardActivity extends BaseBussActivity {
    private ImageView iv_back,iv_add_card;
    private TextView tv_big_title;
    private RecyclerView rv_card;
    private List<HistoryBean> list;//测试数据
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = BankCardActivity.this;
        setContentView(R.layout.activity_user_card);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //借款银行卡的RecyclerView
        rv_card = (RecyclerView) findViewById(R.id.rv_card);
        //添加银行卡
        iv_add_card = (ImageView) findViewById(R.id.iv_add_card);
        //设置listviewd的布局管理器
        rv_card.setLayoutManager(new LinearLayoutManager(_context));
    }

    @Override
    protected void initData() {
        super.initData();
        iv_add_card.setVisibility(View.VISIBLE);//添加银行卡显示
        tv_big_title.setText("我的账单");
        iv_add_card.setVisibility(View.GONE);
        //测试数据
        list = new ArrayList<HistoryBean>();
        HistoryBean bean = null;
        for (int i = 0 ; i<10 ; i++){
            bean = new HistoryBean();
            bean.setType("晋城银行");
            bean.setMoney("储蓄卡");
            bean.setTime("**** **** **** 5111");
            list.add(bean);
        }
        //设置适配器
        rv_card.setAdapter(new BankAdapter(_context, list, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {//为item添加监听
                //ToastUtil.showShort(_context,position+"");
                startActivity(BankCardDetailActivity.class,null);
            }
        }));
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        iv_add_card.setOnClickListener(myonclick);

    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.iv_add_card://添加银行卡
                    startActivity(AddBankCardActivity.class,null);
                    break;
            }
        }
    };
}
