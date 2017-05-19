package com.mzth.huaruiloan.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/15.
 * 支付宝参数
 */

public class AlipayParamsBean implements Serializable {
    private String pid;
    private String key;
    private String email;
    private String alipay_public_key;
    private String self_private_key;
    private String self_private_key_pkcs8;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getSelf_private_key() {
        return self_private_key;
    }

    public void setSelf_private_key(String self_private_key) {
        this.self_private_key = self_private_key;
    }

    public String getSelf_private_key_pkcs8() {
        return self_private_key_pkcs8;
    }

    public void setSelf_private_key_pkcs8(String self_private_key_pkcs8) {
        this.self_private_key_pkcs8 = self_private_key_pkcs8;
    }
}
