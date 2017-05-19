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
import com.mzth.huaruiloan.util.DialogThridUtils;
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/10.
 * 登录页面
 */

public class LoginActivity extends BaseBussActivity {
    private TextView tv_big_title,tv_small_title,tv_find_pwd;
    private ImageView iv_back;
    private Button btn_login;
    private EditText et_put_phone,et_put_pwd;
    private int flag;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = LoginActivity.this;
        setContentView(R.layout.activity_login);
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
        //登录button
        btn_login = (Button) findViewById(R.id.btn_login);
        //忘记密码
        tv_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        //手机号
        et_put_phone = (EditText) findViewById(R.id.et_put_phone);
        //密码
        et_put_pwd = (EditText) findViewById(R.id.et_put_pwd);
    }
    @Override
    protected void initData() {
        super.initData();

        if(!StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,""))){
            startActivity(HomeActivity.class,null);
            onBackPressed();
        }
        tv_big_title.setText("登录");
        tv_small_title.setText("注册");
        tv_small_title.setVisibility(View.VISIBLE);
//        et_put_phone.setText("18310968137");
//        et_put_pwd.setText("123456789");


    }
    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        tv_small_title.setOnClickListener(myonclick);
        btn_login.setOnClickListener(myonclick);
        tv_find_pwd.setOnClickListener(myonclick);
    }

    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.tv_small_title://子标题  注册
                    startActivity(RegisterActivity.class,null);
                    onBackPressed();
                    break;
                case R.id.btn_login://登录成功进入主页
                    if(StringUtil.isEmpty(et_put_phone.getText().toString())){
                        ToastUtil.showShort(_context,"手机号不能为空");
                        return;
                    }
                    if(StringUtil.isEmpty(et_put_pwd.getText().toString())){
                        ToastUtil.showShort(_context,"密码不能为空");
                        return;
                    }

                    LoginRequest();
                    break;
                case R.id.tv_find_pwd://忘记密码页面
                    startActivity(FindPwdActivity.class,null);
                    break;
            }
        }
    };
    //登录请求
    private void LoginRequest(){
        final Dialog dialog = DialogThridUtils.showWaitDialog(_context,"登录中···",false,false);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("phone",et_put_phone.getText().toString());
        map.put("password",et_put_pwd.getText().toString());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.LOGIN, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                switch (Integer.parseInt(status)){
                    case 0:
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1://返回正确
                        //ToastUtil.showShort(_context,"登录成功");
                        String result = GsonUtil.getJsonFromKey(json,"result");
                        LoginBean bean = GsonUtil.getBeanFromJson(result, LoginBean.class);
                        //将用户的基本信息保存在本地
                        SharedPreferencesUtil.setParam(_context, LocalSaveKey.USER_ID,bean.getUser_id());
                        SharedPreferencesUtil.setParam(_context, LocalSaveKey.GROUP_VAR,bean.getGroup_var());
                        SharedPreferencesUtil.setParam(_context, LocalSaveKey.AUTH_KEY,bean.getAuth_key());
                        IsinfoRequest(bean);//判读是否完善了资料
                        break;
                }
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                DialogThridUtils.closeDialog(dialog);//关闭动画
            }
        });
    }
    //是否填写信息
    private void IsinfoRequest(final LoginBean bean){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user_id",bean.getUser_id());
        map.put("auth_key",bean.getAuth_key());
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
                    startActivity(HomeActivity.class,null);
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
