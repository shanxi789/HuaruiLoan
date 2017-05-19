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
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/11.
 * 重置密码
 */

public class ReSetPwdActivity extends BaseBussActivity {
    private Button btn_next;
    private ImageView iv_back;
    private TextView tv_big_title;
    private EditText et_new_pwd,et_new_argin_pwd;
    private String code;//验证码
    private String phone;//手机号
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = ReSetPwdActivity.this;
        setContentView(R.layout.activity_find_pwd02);
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
        //新密码
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        //再次输入新密码
        et_new_argin_pwd = (EditText) findViewById(R.id.et_new_argin_pwd);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_big_title.setText("重置密码");
        phone = getIntent().getStringExtra("phone");//得到手机号
        code = getIntent().getStringExtra("code");//得到验证码

    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();
        iv_back.setOnClickListener(myonclick);
        btn_next.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back://返回键
                    onBackPressed();
                    break;
                case R.id.btn_next://下一步
                    String pwd = et_new_pwd.getText().toString();
                    String pwd2=et_new_argin_pwd.getText().toString();
                    if(StringUtil.isEmpty(pwd)){
                        ToastUtil.showShort(_context,"请输入密码");
                        return;
                    }
                    if(StringUtil.isEmpty(pwd2)){
                        ToastUtil.showShort(_context,"请再次输入密码");
                        return;
                    }
                    if(!pwd.equals(pwd2)){
                        ToastUtil.showShort(_context,"两次输入密码不一致");
                        return;
                    }
                    //startActivity(PwdReSetSureActivity.class,null);
                    //onBackPressed();
                    FindRequest();
                    break;
            }
        }
    };
    //找回密码第二步
    private void FindRequest(){
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(_context,"加载中...");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("phone",phone);
        map.put("token",code);
        map.put("password",et_new_pwd.getText().toString());
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.FIND_PWD, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                //ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                switch (Integer.parseInt(status)){
                    case 0:
                        String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1://正确
                        String result = GsonUtil.getJsonFromKey(json,"result");
                        ToastUtil.showShort(_context,result);
                        startActivity(PwdReSetSureActivity.class,null);
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
}
