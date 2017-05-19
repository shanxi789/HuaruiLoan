package com.mzth.huaruiloan.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/13.
 * 登录对象
 */

public class LoginBean implements Serializable {
    private String user_id;//用户ID
    private String group_var;
    private String auth_key;//权密钥

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGroup_var() {
        return group_var;
    }

    public void setGroup_var(String group_var) {
        this.group_var = group_var;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }
}
