package com.mzth.huaruiloan.ui.fragment.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.sub.LoginActivity;
import com.mzth.huaruiloan.ui.activity.sub.RegisterActivity;
import com.mzth.huaruiloan.ui.activity.sub.user.BankCardActivity;
import com.mzth.huaruiloan.ui.activity.sub.user.HistoryActivity;
import com.mzth.huaruiloan.ui.activity.sub.user.UserInfoActivity;
import com.mzth.huaruiloan.ui.fragment.base.BaseFragment;
import com.mzth.huaruiloan.util.DialogUtil;
import com.mzth.huaruiloan.util.SharedPreferencesUtil;
import com.mzth.huaruiloan.util.StringUtil;
import com.mzth.huaruiloan.util.ToastUtil;
import com.mzth.huaruiloan.widget.CircleImageView;

/**
 * Created by Administrator on 2017/5/10.
 */
@SuppressLint("ValidFragment")
public class UserFragment extends BaseFragment {
    private CircleImageView iv_user_head;
    private TextView tv_user_login,tv_user_register,tv_bank_card,tv_user_info;
    private Button btn_exit;
    private RelativeLayout rl_user_info,rl_user_bank,rl_user_history;
    private String user_id;//用户ID
    public UserFragment() {
        super();
    }

    public UserFragment(Context context) {
        super(context);
    }

    public UserFragment(Context context, int resId) {
        super(context, resId);
    }

    @Override
    protected void initView(View v, Bundle savedInstanceState) {
        //头像
        iv_user_head = (CircleImageView) v.findViewById(R.id.iv_user_head);
        //登录
        tv_user_login = (TextView) v.findViewById(R.id.tv_user_login);
        //注册
        tv_user_register = (TextView) v.findViewById(R.id.tv_user_register);
        //我的资料
        rl_user_info = (RelativeLayout) v.findViewById(R.id.rl_user_info);
        //银行卡
        rl_user_bank = (RelativeLayout) v.findViewById(R.id.rl_user_bank);
        //银行卡张数
        tv_bank_card = (TextView) v.findViewById(R.id.tv_bank_card);
        //信息完善度
        tv_user_info = (TextView) v.findViewById(R.id.tv_user_info);
        //我要还款
        rl_user_history = (RelativeLayout) v.findViewById(R.id.rl_user_history);
        //退出登录
        btn_exit = (Button) v.findViewById(R.id.btn_exit);
    }

    @Override
    protected void initData() {
        user_id = (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,"");
        if(!StringUtil.isEmpty(user_id)){
            tv_user_login.setText("91钱");
            tv_user_login.setClickable(false);//不可以被点击
            tv_user_register.setVisibility(View.GONE);//隐藏注册
            tv_user_info.setVisibility(View.VISIBLE);
            //tv_bank_card.setVisibility(View.VISIBLE);
            btn_exit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void BindComponentEvent() {
        tv_user_login.setOnClickListener(myonclick);
        tv_user_register.setOnClickListener(myonclick);
        rl_user_info.setOnClickListener(myonclick);
        rl_user_bank.setOnClickListener(myonclick);
        rl_user_history.setOnClickListener(myonclick);
        btn_exit.setOnClickListener(myonclick);
    }
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userId = (String) SharedPreferencesUtil.getParam(_context,LocalSaveKey.USER_ID,"");
            switch (v.getId()){
                case R.id.tv_user_login://登录
                    startActivity(LoginActivity.class,null);
                    break;
                case R.id.tv_user_register://注册
                    startActivity(RegisterActivity.class,null);
                    break;
                case R.id.rl_user_info://我的资料
                        if(!StringUtil.isEmpty(userId)){
                            startActivityForResult(UserInfoActivity.class,null,100);
                        }else{
                            ToastUtil.showShort(_context,"您还没有登录");
                        }
                    break;
                case R.id.rl_user_bank://我的银行卡
                    if(!StringUtil.isEmpty(userId)) {
                        startActivity(BankCardActivity.class, null);
                    }else{
                        ToastUtil.showShort(_context,"您还没有登录");
                    }
                    break;
                case R.id.rl_user_history://我要还款
                    if(!StringUtil.isEmpty(userId)){
                        //startActivity(HistoryActivity.class,null);
                        DialogUtil.alertDialog(_context, "温馨提示", "如需还款请拨打联系客服,客服电话" +
                                        "0208-1774887",
                                "拨打", "Cancel", false, new DialogUtil.ReshActivity() {
                                    @Override
                                    public void reshActivity() {//确定按钮
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02081774887"));
                                        startActivity(intent);
                                    }
                                });
                    }else{
                        ToastUtil.showShort(_context,"您还没有登录");
                    }
                    break;
                case R.id.btn_exit://退出登录
                    //startActivity(HistoryActivity.class,null);
                    //退出登录的时候把user_id清空
                    AlertDialog.Builder builder=new AlertDialog.Builder(_context);
                    builder.setMessage("你确定要退出登录吗?");
                    builder.setTitle("提示");
                    //确认按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferencesUtil.setParam(_context,LocalSaveKey.USER_ID,"");
                            btn_exit.setVisibility(View.GONE);//隐藏退出登录的按钮
                            tv_user_login.setText("登录");
                            tv_user_login.setClickable(true);//可以被点击
                            tv_user_register.setVisibility(View.VISIBLE);//隐藏注册
                            tv_user_info.setVisibility(View.GONE);
                            //tv_bank_card.setVisibility(View.GONE);
                            dialogInterface.dismiss();
                        }
                    });
                    //取消按钮
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                    break;
            }
        }
    };
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {

        switch (requestCode){
            case 100://我的资料回调

                break;
        }
    }
}
