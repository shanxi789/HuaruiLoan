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
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.base.BaseBussActivity;
import com.mzth.huaruiloan.util.GsonUtil;
import com.mzth.huaruiloan.util.NetUtil;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.util.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 * 完善资料页面
 */

public class PerfectDataActivity extends BaseBussActivity {
    private Button btn_commit;//提交按钮
    private EditText et_info_other_phone,et_info_other_name,et_info_urgent_name,
            et_info_urgent_phone,et_id_address,et_id,et_card_hao,
            et_name;
    private TextView tv_title;
    private ImageView iv_back;
    @Override
    protected void setCustomLayout(Bundle savedInstanceState) {
        super.setCustomLayout(savedInstanceState);
        _context = PerfectDataActivity.this;
        setContentView(R.layout.activity_login_info);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        tv_title = (TextView) findViewById(R.id.tv_big_title);
        //返回键
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //提交按钮
        btn_commit = (Button) findViewById(R.id.btn_commit);
        //真实姓名
        et_name = (EditText) findViewById(R.id.et_name);
        //银行卡号
        et_card_hao = (EditText) findViewById(R.id.et_card_hao);
        //身份证
        et_id = (EditText) findViewById(R.id.et_id);
        //身份证所在地址
        et_id_address = (EditText) findViewById(R.id.et_id_address);
        //紧急联系人电话
        et_info_urgent_phone = (EditText) findViewById(R.id.et_info_urgent_phone);
        //紧急联系人姓名
        et_info_urgent_name = (EditText) findViewById(R.id.et_info_urgent_name);
        //其他联系人姓名
        et_info_other_name = (EditText) findViewById(R.id.et_info_other_name);
        //其他联系人电话
        et_info_other_phone = (EditText) findViewById(R.id.et_info_other_phone);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title.setText("完善资料");
        iv_back.setVisibility(View.GONE);
    }

    @Override
    protected void BindComponentEvent() {
        super.BindComponentEvent();

        //提交
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(et_name.getText().toString())){
                    ToastUtil.showShort(_context,"真实姓名不能为空");
                    return;
                }
                if(StringUtil.isEmpty(et_card_hao.getText().toString())){
                    ToastUtil.showShort(_context,"银行卡号不能为空");
                    return;
                }
                if(StringUtil.isEmpty(et_id.getText().toString())){
                    ToastUtil.showShort(_context,"身份证号码不能为空");
                    return;
                }
                InformationRequest();//提交
            }
        });
    }

    //填写信息请求
    private void InformationRequest(){
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(_context,"加载中...");
        Map<String,Object> map = new HashMap<String,Object>();
        String user_id = (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,"");
        String auth_key = (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.AUTH_KEY,"");
        map.put("user_id",user_id);//用户ID
        map.put("auth_key",auth_key);//用户秘钥
        map.put("name",et_name.getText().toString());//用户真实姓名
        String id = et_id.getText().toString();
        map.put("card",id);//用户身份证号
        map.put("num",et_card_hao.getText().toString());//用户银行卡号
        map.put("card_address",et_id_address.getText().toString());//用户身份证地址
        map.put("emer_name",et_info_urgent_name.getText().toString());//紧急联系人姓名
        map.put("emer_phone",et_info_urgent_phone.getText().toString());//紧急联系人电话
        map.put("else_name",et_info_other_name.getText().toString());//其他联系人姓名
        map.put("else_phone",et_info_other_phone.getText().toString());//其他联系人电话
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.CREDIT, map, new NetUtil.RequestCallBack() {
            @Override
            public void onSuccess(int statusCode, String json) {
                ToastUtil.showShort(_context,json);
                String status = GsonUtil.getJsonFromKey(json,"status");
                switch (Integer.parseInt(status)){
                    case 0:
                        String err_msg = GsonUtil.getJsonFromKey(json,"err_msg");
                        ToastUtil.showShort(_context,err_msg);
                        break;
                    case 1:
                        String result = GsonUtil.getJsonFromKey(json,"result");
                        ToastUtil.showShort(_context,result);
                        startActivity(HomeActivity.class,null);
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
