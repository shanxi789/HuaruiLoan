package com.mzth.huaruiloan.ui.activity.sub.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.ui.activity.sub.WebOrderActivity;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/5/16.
 */

public class WingPayMentActivity extends BaseBussActivity {
    private TextView tv_big_title,tv_pay_name,tv_shen,
            tv_interest,tv_money,tv_arrival;
    private ImageView iv_back;
    private Button btn_unionPay;
    private LinearLayout ll_info;
    private EditText et_money;//借款金额
    private double interest;
    private String ky_price;
    private Spinner spinner;//借款期限
    private String money;
    private double a = 0.05/30;
    private DecimalFormat df = new DecimalFormat("######0");//四舍五入
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = WingPayMentActivity.this;
        setContentView(R.layout.activity_home_payment);
    }

    @Override
    protected void initView() {
        super.initView();
        //
        ll_info = (LinearLayout) findViewById(R.id.ll_info);
        //审核中
        tv_shen = (TextView) findViewById(R.id.tv_shen);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //翼支付button
        btn_unionPay = (Button) findViewById(R.id.btn_unionPay);
        //支付name
        tv_pay_name = (TextView) findViewById(R.id.tv_pay_name);
        //利息
        tv_interest = (TextView) findViewById(R.id.tv_interest);
        //可借额度
        tv_money = (TextView) findViewById(R.id.tv_money);
        //借用金额
        et_money = (EditText) findViewById(R.id.et_money);
        //实际到账
        tv_arrival = (TextView) findViewById(R.id.tv_arrival);
        //借款期限
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    @Override
    protected void initData() {
        super.initData();
        int status =Integer.parseInt(getIntent().getStringExtra("status"));
        //interest = getIntent().getDoubleExtra("interest",0);
        ky_price = getIntent().getStringExtra("ky_price");
        if(status==1){
            ll_info.setVisibility(View.VISIBLE);
            tv_shen.setVisibility(View.GONE);
        }else if(status==2){
            ll_info.setVisibility(View.GONE);
            tv_shen.setVisibility(View.VISIBLE);
            tv_shen.setText("审核中");
        }else if(status==3){
            ll_info.setVisibility(View.GONE);
            tv_shen.setVisibility(View.VISIBLE);
            tv_shen.setText("审核成功");
        }else if(status==4){
            ll_info.setVisibility(View.GONE);
            tv_shen.setVisibility(View.VISIBLE);
            tv_shen.setText("审核失败");
        }
        tv_money.setText(ky_price);//可用金额
        btn_unionPay.setClickable(false);
        btn_unionPay.setBackgroundResource(R.drawable.corners_bg_no);
        tv_big_title.setText("我要借款");
        tv_pay_name.setText("易联支付");

        //OrderRequest();
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        btn_unionPay.setOnClickListener(myonclick);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = (String) parent.getItemAtPosition(position);

                double data = Double.parseDouble(time);
                if(!StringUtil.isEmpty(money)){
                    double b = Double.parseDouble(tv_interest.getText().toString());
                    double c = Double.parseDouble(money);
                    double d = a*data*c;//利息
                    double e = c-d;//到账
                    tv_interest.setText(df.format(d)+"");//利息
                    tv_arrival.setText(df.format(e)+ "");//到账
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                money = et_money.getText().toString();
                if(!StringUtil.isEmpty(money)) {
                    double Dmoney = Double.parseDouble(money);
                    if(Dmoney>Double.parseDouble(ky_price)){
                        ToastUtil.showShort(_context,"您输入的金额有误," +
                                "输入的金额不可大于可用余额");
                        btn_unionPay.setClickable(false);
                        //btn_unionPay.setTextColor(getResources().getColor(R.color.black));
                        btn_unionPay.setBackgroundResource(R.drawable.corners_bg_no);
                        return;
                    }
                    btn_unionPay.setClickable(true);
                    //btn_unionPay.setTextColor(getResources().getColor(R.color.white));
                    btn_unionPay.setBackgroundResource(R.drawable.corners_bg);
                    double time = Double.parseDouble(spinner.getSelectedItem().toString());
                    double moneys = 50.00;
                    //double interestrate = moneys / 1000;
                    //利息
                    interest = a * Dmoney * time;
                    double arrival = Dmoney - interest;
                    tv_interest.setText(df.format(interest)+"");//利息
                    tv_arrival.setText(df.format(arrival)+"");
                }else{
                    tv_interest.setText("0.0");//利息
                    tv_arrival.setText("0");
                    btn_unionPay.setClickable(false);
                    //btn_unionPay.setTextColor(getResources().getColor(R.color.black));
                    btn_unionPay.setBackgroundResource(R.drawable.corners_bg_no);
                }
            }
        });
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.btn_unionPay://支付
                    //Buy();

                    startActivityForResult(WebOrderActivity.class,null,100);
                    break;
            }
        }
    };
    //
    private void OrderRequest(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("txnAmt",6);
        map.put("miscData","司昊煊|140581199804164818|6231970135113643873");
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.ORDER, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,json);
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
            }
        });
    }

    @Override
    protected void doActivityResult(int requestCode, Intent intent) {
        super.doActivityResult(requestCode, intent);
        switch (requestCode){
            case 100://支付回调
                break;
        }
    }
}
