package com.mzth.huaruiloan.ui.activity.sub.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/5/15.
 * 银联支付
 */

public class PayMentActivity extends BaseBussActivity {
    private TextView tv_big_title;
    private ImageView iv_back;
    private Button btn_unionPay;
    private final String mMode = "01";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";

    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tn = "";
            if (msg.obj == null || ((String) msg.obj).length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PayMentActivity.this);
                builder.setTitle("错误提示");
                builder.setMessage("网络连接失败,请重试!");
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            } else {
                tn = (String) msg.obj;
                /*************************************************
                 * 步骤2：通过银联工具类启动支付插件
                 ************************************************/
                doStartUnionPayPlugin(PayMentActivity.this, tn, mMode);
            }
        }
    };

    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = PayMentActivity.this;
        setContentView(R.layout.activity_home_payment);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //银联支付button
        btn_unionPay = (Button) findViewById(R.id.btn_unionPay);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("支付");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        btn_unionPay.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.btn_unionPay://支付
                    Buy();
                    break;
            }
        }
    };
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        // 判断当前手机是否安装了银联的 apk
        boolean bInstall = UPPayAssistEx.checkInstalled(activity);
        if (bInstall) {
            // mMode参数解释：
            // 0 - 启动银联正式环境
            // 1 - 连接银联测试环境
            int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
            if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                // 需要重新安装控件

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UPPayAssistEx.installUPPayPlugin(PayMentActivity.this);
                                dialog.dismiss();
                            }
                        });

                builder.setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

            }
        } else {
            UPPayAssistEx.startPay(activity, null, null, tn, mode);
        }
    }

    //银联支付请求
    public void Buy(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String tn = null;
                InputStream is;
                try {
                    String url = TN_URL_01;
                    URL myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    ucon.setConnectTimeout(120000);
                    is = ucon.getInputStream();
                    int i = -1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    tn = baos.toString();
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = mHandler.obtainMessage();
                msg.obj = tn;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void doActivityResult(int requestCode, Intent data) {
        super.doActivityResult(requestCode, data);
        if(data==null){
            return;
        }
        String msg = "";
    /*
     * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
     */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = null;
                    try {
                        sign = resultJson.getString("sign");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, mMode);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
            }
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }
}
