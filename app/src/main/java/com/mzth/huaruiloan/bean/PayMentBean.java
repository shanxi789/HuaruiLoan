package com.mzth.huaruiloan.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/15.
 * 支付宝对象
 */

public class PayMentBean implements Serializable {
    private String title;
    private String pay_type;
    private String notify_url;
    private String order_no;
    private int price;
    private AlipayParamsBean params;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public AlipayParamsBean getParams() {
        return params;
    }

    public void setParams(AlipayParamsBean params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return title+","+pay_type+","+notify_url+","+order_no+","+price;
    }
}
