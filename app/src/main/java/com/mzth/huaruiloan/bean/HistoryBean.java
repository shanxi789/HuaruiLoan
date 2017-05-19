package com.mzth.huaruiloan.bean;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 * 历史纪录的对象
 */

public class HistoryBean implements Serializable {
    private String type;
    private String money;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
