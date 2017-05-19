package com.mzth.huaruiloan.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/5/11.
 */

public class Constans {
    //接口action-------------------------------------------------

    public final static String APP_NAME ="HuaruiLoan";
    // sd卡的绝对路径
    public final static String SDPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    // 设置头像文件保存路径
    public final static String PATH_HEAD = SDPATH + File.separator + APP_NAME
            + File.separator + "head" + File.separator;
    //发送验证码
    public final static String SEND_SMS ="public/send_sms.html";
    //登录
    public final static String LOGIN ="passport/login.html";
    //注册
    public final static String REGISTER ="passport/register.html";
    //找回密码第一步
    public final static String CHECK_PWD ="passport/check_pwd_sms.html";
    //找回密码第二步
    public final static String FIND_PWD ="passport/find_pwd.html";
    //支付
    public final static String PAY_DATA ="index/pay_data.html";
    //支付商品
    public final static String ORDER ="http://1355.bibang110.com/demo/merOrderForm.php";
    //是否填写信息
    public final static String CHECK_NAME ="index/check_name.html";
    //填写信息
    public final static String CREDIT ="index/credit.html";
    //获取用户额度
    public final static String GET_PRICE ="index/get_price.html";

}
