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
import com.mzth.huaruiloan.ui.adapter.sub.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 * 历史记录
 */

public class HistoryActivity extends BaseBussActivity {
    private ImageView iv_back;
    private TextView tv_big_title;
    private RecyclerView rv_history;
    private List<HistoryBean> list;//测试数据
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = HistoryActivity.this;
        setContentView(R.layout.activity_user_history);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //历史纪录的RecyclerView
        rv_history = (RecyclerView) findViewById(R.id.rv_history);
        //设置布局管理器
        rv_history.setLayoutManager(new LinearLayoutManager(_context));
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("历史记录");
        //测试数据
        list = new ArrayList<HistoryBean>();
        HistoryBean bean = null;
        for (int i = 0 ; i<10 ; i++){
            bean = new HistoryBean();
            bean.setType("单期借款");
            bean.setMoney("6400元");
            bean.setTime("2017-03-13");
            list.add(bean);
        }
        //设置适配器
        rv_history.setAdapter(new HistoryAdapter(_context,list));
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
