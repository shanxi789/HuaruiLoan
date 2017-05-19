package com.mzth.huaruiloan.ui.activity.sub;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.CountDownTimerUtils;
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/11.
 * 忘记密码
 */

public class FindPwdActivity extends BaseBussActivity {
    private Button btn_next;
    private ImageView iv_back;
    private TextView tv_big_title,tv_send_code;
    private EditText et_find_phone,et_find_code;
    private CountDownTimerUtils timerUtils;
    private Dialog dialog;//加载动画
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = FindPwdActivity.this;
        setContentView(R.layout.activity_find_pwd01);
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
        //手机号
        et_find_phone = (EditText) findViewById(R.id.et_find_phone);
        //验证码
        et_find_code = (EditText) findViewById(R.id.et_find_code);
        //发送验证码
        tv_send_code = (TextView) findViewById(R.id.tv_send_code);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("忘记密码");
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        btn_next.setOnClickListener(myonclick);
        tv_send_code.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.tv_send_code://发送验证码
                    if(StringUtil.isEmpty(et_find_phone.getText().toString())){
                        ToastUtil.showShort(_context,"请输入手机号");
                        return;
                    }
                    SendCodeRequest();
                    break;
                case R.id.btn_next://下一步
                    if(StringUtil.isEmpty(et_find_phone.getText().toString())){
                        ToastUtil.showShort(_context,"请输入手机号");
                        return;
                    }
                    if(StringUtil.isEmpty(et_find_code.getText().toString())){
                        ToastUtil.showShort(_context,"请输入验证码");
                        return;
                    }
//                    startActivity(ReSetPwdActivity.class,null);
//                    onBackPressed();
                    Command();
                    break;
            }
        }
    };
    //提交手机号和验证码请求
    private void Command(){
        dialog = WeiboDialogUtils.createLoadingDialog(_context,"加载中...");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("phone",et_find_phone.getText().toString());
        String code = et_find_code.getText().toString();
        map.put("token",code);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.CHECK_PWD, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                switch (Integer.parseInt(status)){
                    case 0:
                        String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1:
                        String result  = GsonUtil.getJsonFromKey(json,"result");
                        ToastUtil.showShort(_context,result);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone",et_find_phone.getText().toString());
                        bundle.putString("code",et_find_code.getText().toString());
                        startActivity(ReSetPwdActivity.class,bundle);
                        onBackPressed();
                        break;
                }
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }
        });
    }

    //发送验证码请求
    private void SendCodeRequest(){
        dialog = WeiboDialogUtils.createLoadingDialog(_context,"正在发送验证码");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("phone",et_find_phone.getText().toString());
        map.put("type","find_pwd");
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.SEND_SMS, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                switch (Integer.parseInt(status)){
                    case 0:
                        String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1:
                        String result = GsonUtil.getJsonFromKey(json,"result");
                        String repeat_time = GsonUtil.getJsonFromKey(result,"repeat_time");
                        String expire_time = GsonUtil.getJsonFromKey(result,"expire_time");
                        int min = Integer.parseInt(expire_time)/60;
                        timerUtils = new CountDownTimerUtils(_context, tv_send_code, 1000*Integer.parseInt(repeat_time), 1000, "S后重新获取", "发送验证码", false);
                        timerUtils.start();
                        ToastUtil.showShort(_context,"验证码发送成功,有效时间为"+min+"分钟");
                        break;
                }
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画

            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
            }
        });
    }
}
