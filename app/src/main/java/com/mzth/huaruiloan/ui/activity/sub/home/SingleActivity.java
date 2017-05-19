package com.mzth.huaruiloan.ui.activity.sub.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.bean.AlipayParamsBean;
import com.mzth.huaruiloan.bean.PayMentBean;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.ui.activity.sub.FindPwdActivity;
import com.mzth.huaruiloan.ui.activity.sub.HomeActivity;
import com.mzth.huaruiloan.ui.activity.sub.RegisterActivity;
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.SignUtils;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.widget.PayResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/13.
 * 单期借款页面
 */

public class SingleActivity extends BaseBussActivity {
    private TextView tv_big_title;
    private ImageView iv_back;
    private RelativeLayout rl_card_back;
    private Button btn_bank_sure;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showShort(_context, "支付成功");

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showShort(_context,"支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showShort(_context,"取消支付");
                        }
                    }
                    break;
            }
        };
    };
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = SingleActivity.this;
        setContentView(R.layout.activity_home_loan);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //入账银行卡
        rl_card_back = (RelativeLayout) findViewById(R.id.rl_card_back);
        //确定按钮
        btn_bank_sure = (Button) findViewById(R.id.btn_bank_sure);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("我要借款");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        rl_card_back.setOnClickListener(myonclick);
        btn_bank_sure.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.rl_card_back://入账银行卡
                    break;
                case R.id.btn_bank_sure://确定按钮
                    PaymentRequest();
                    //PaymentCall();
                    break;
            }
        }
    };
    //测试支付请求
    private void  PaymentRequest(){
        Map<String,Object> map = new HashMap<String,Object>();
        String user_id = (String)SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,"");
        String key = (String)SharedPreferencesUtil.getParam(_context, LocalSaveKey.AUTH_KEY,"");
        map.put("user_id",user_id);
        map.put("auth_key",key);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.PAY_DATA, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                switch (Integer.parseInt(status)){
                    case 0:
                        String err_msg  = GsonUtil.getJsonFromKey(json,"err_msg");
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1:
                        String result = GsonUtil.getJsonFromKey(json,"result");
                        PayMentBean bean = GsonUtil.getBeanFromJson(result, PayMentBean.class);
                        AlipayParamsBean AlipayBean = bean.getParams();
                        PaymentCall(bean,AlipayBean);
                        break;
                }

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
    //调用支付宝
    private void PaymentCall(PayMentBean bean , AlipayParamsBean AlipayBean){
        String sign = SignUtils.sign(getOrderInfo(bean,AlipayBean),AlipayBean.getSelf_private_key_pkcs8());
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //final String payInfo = getOrderInfo(bean,AlipayBean,sign);
        final String payInfo = getOrderInfo(bean,AlipayBean) + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(_context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = 100;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(PayMentBean bean , AlipayParamsBean AlipayBean) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" +AlipayBean.getPid()+ "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + AlipayBean.getEmail() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + bean.getTitle() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + bean.getTitle() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + bean.getPrice() + "\"";

        // 服务器异步通知页面路径
        try {
            orderInfo += "&notify_url=" + "\"" + URLEncoder.encode(bean.getNotify_url(),"UTF-8") + "\"";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
