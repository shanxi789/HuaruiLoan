package com.mzth.huaruiloan.ui.activity.sub;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.bean.LoginBean;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.CountDownTimerUtils;
import com.mzth.huaruiloan.util.DialogThridUtils;
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/10.
 * 注册页面
 */

public class RegisterActivity extends BaseBussActivity{
    private TextView tv_big_title,tv_small_title,tv_send_code;
    private ImageView iv_back;
    private EditText et_phone,et_password,et_code;
    private CountDownTimerUtils timerUtils;
    private Dialog dialog;//加载动画
    private Button btn_register;
    private int flag;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = RegisterActivity.this;
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        super.initView();
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //标题
        tv_big_title = (TextView) findViewById(R.id.tv_big_title);
        //子标题
        tv_small_title = (TextView) findViewById(R.id.tv_small_title);
        //手机号
        et_phone = (EditText) findViewById(R.id.et_phone);
        //密码
        et_password = (EditText) findViewById(R.id.et_password);
        //验证码
        et_code = (EditText) findViewById(R.id.et_code);
        //发送验证码
        tv_send_code = (TextView) findViewById(R.id.tv_send_code);
        //注册
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("注册");
        tv_small_title.setText("登录");
        tv_small_title.setVisibility(View.VISIBLE);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        tv_small_title.setOnClickListener(myonclick);
        tv_send_code.setOnClickListener(myonclick);
        btn_register.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.tv_small_title://子标题  注册
                    startActivity(LoginActivity.class,null);
                    onBackPressed();
                    break;
                case R.id.tv_send_code://发送验证码
                    if(StringUtil.isEmpty(et_phone.getText().toString())){
                        ToastUtil.showShort(_context,"请输入手机号");
                        return;
                    }
                    SendCodeRequest();
                    break;
                case R.id.btn_register://下一步
                    if(StringUtil.isEmpty(et_phone.getText().toString())){
                        ToastUtil.showShort(_context,"请输入手机号");
                        return;
                    }
                    if(StringUtil.isEmpty(et_code.getText().toString())){
                        ToastUtil.showShort(_context,"请输入验证码");
                        return;
                    }
                    if(StringUtil.isEmpty(et_password.getText().toString())){
                        ToastUtil.showShort(_context,"请输入密码");
                        return;
                    }
                    if(et_password.getText().toString().length()<8||
                            et_password.getText().toString().length()>16){
                        ToastUtil.showShort(_context,"密码长度在8-16之间");
                        return;
                    }
                    RegisterRequest();
                    break;
            }
        }
    };
    //注册请求
    private void RegisterRequest(){
        dialog = WeiboDialogUtils.createLoadingDialog(_context,"正在注册");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("phone",et_phone.getText().toString());
        map.put("password",et_password.getText().toString());
        map.put("token",et_code.getText().toString().trim());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.REGISTER, map, new NetUtil.RequestCallBack() {
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
                        String user_id = GsonUtil.getJsonFromKey(result,"user_id");
                        String auth_key = GsonUtil.getJsonFromKey(result,"auth_key");
                        //保存用户ID
                        SharedPreferencesUtil.setParam(_context, LocalSaveKey.USER_ID,user_id);
                        //保存秘钥
                        SharedPreferencesUtil.setParam(_context, LocalSaveKey.AUTH_KEY,auth_key);
                        IsinfoRequest(user_id,auth_key);//查看是否完善资料
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
        map.put("phone",et_phone.getText().toString());
        map.put("type","register");
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.SEND_SMS, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                switch (Integer.parseInt(status)){
                    case 0:
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

    //是否填写信息
    private void IsinfoRequest(final String user_id, final String auth_key){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user_id",user_id);
        map.put("auth_key",auth_key);
        NetUtil.Request(NetUtil.RequestMethod.GET, Constans.CHECK_NAME, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String result = GsonUtil.getJsonFromKey(json,"result");
                String results = GsonUtil.getJsonFromKey(result,"result");
                flag = Integer.parseInt(results);
                if(flag==0){
                    startActivity(PerfectDataActivity.class,null);
                    onBackPressed();
                }else{
                    //ToastUtil.showShort(_context,"注册成功");
                    startActivity(LoginActivity.class,null);
                    onBackPressed();
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
}
