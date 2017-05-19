package com.mzth.huaruiloan.ui.fragment.sub;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.mzth.huaruiloan.R;
import com.mzth.huaruiloan.common.Constans;
import com.mzth.huaruiloan.common.LocalSaveKey;
import com.mzth.huaruiloan.ui.activity.sub.home.PayMentActivity;
import com.mzth.huaruiloan.ui.activity.sub.home.SingleActivity;
import com.mzth.huaruiloan.ui.activity.sub.home.WingPayMentActivity;
import com.mzth.huaruiloan.ui.fragment.base.BaseFragment;
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
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment {
    private RelativeLayout rl_loan_item;
    private TextView tv_user_quota;
    private String statuse;
    private MaterialRefreshLayout refresh;//刷新
    private double interest;//利息
    private String ky_price;
    public HomeFragment() {
        super();
    }

    public HomeFragment(Context context) {
        super(context);
    }

    public HomeFragment(Context context, int resId) {
        super(context, resId);
    }

    @Override
    protected void initView(View v, Bundle savedInstanceState) {
        //单期借款的item
        rl_loan_item = (RelativeLayout) v.findViewById(R.id.rl_loan_item);
        //可用额度
        tv_user_quota = (TextView) v.findViewById(R.id.tv_user_quota);
        //刷新
        refresh = (MaterialRefreshLayout) v.findViewById(R.id.refresh);
    }

    @Override
    protected void initData() {
        UserQuotaRequest();
    }

    @Override
    protected void BindComponentEvent() {
        rl_loan_item.setOnClickListener(myonclick);
        //设置刷新控件的刷新事件
        refresh.setMaterialRefreshListener(materialRefreshListener);
    }
    //结束刷新
    private void finishRefresh(){
        //结束下拉刷新
        refresh.finishRefresh();
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        //下拉刷新
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //刷新用户信息
            UserQuotaRequest();
        }
    };
    private View.OnClickListener myonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_loan_item://单期借款的item
                    //startActivity(PayMentActivity.class,null);
                    if(!StringUtil.isEmpty((String) SharedPreferencesUtil.getParam(_context,LocalSaveKey.USER_ID,""))){
                        Bundle bundle = new Bundle();
                        bundle.putString("status",statuse);
                        //bundle.putDouble("interest",interest);
                        bundle.putString("ky_price",ky_price);
                        startActivity(WingPayMentActivity.class,bundle);
                    }else{
                        ToastUtil.showShort(_context,"您还没有登录");
                    }
                    break;
            }
        }
    };
    //获取用户额度请求
    private void UserQuotaRequest(){
        final Dialog dialog = WeiboDialogUtils.createLoadingDialog(_context,"加载中...");
        Map<String,Object> map = new HashMap<String,Object>();
        String user_id = (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.USER_ID,"");
        String auth_key = (String) SharedPreferencesUtil.getParam(_context, LocalSaveKey.AUTH_KEY,"");
        map.put("user_id",user_id);
        map.put("auth_key",auth_key);
        NetUtil.Request(NetUtil.RequestMethod.POST, Constans.GET_PRICE, map, new NetUtil.RequestCallBack() {
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
                        ky_price = GsonUtil.getJsonFromKey(result,"ky_price");
                        String price = GsonUtil.getJsonFromKey(result,"price");
                        String yiy_price = GsonUtil.getJsonFromKey(result,"yiy_price");
                        statuse = GsonUtil.getJsonFromKey(result,"status");
                        tv_user_quota.setText("您的额度"+ky_price+"元");

                        break;
                }
               WeiboDialogUtils.closeDialog(dialog);//关闭动画
                finishRefresh();
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
                finishRefresh();
            }

            @Override
            public void onFailure(Exception e, String errorMsg) {
                ToastUtil.showShort(_context,errorMsg);
                WeiboDialogUtils.closeDialog(dialog);//关闭动画
                finishRefresh();
            }
        });
    }
    @Override
    protected void doActivityResult(int requestCode, Intent intent) {

    }
}
