package com.stone.shop.shop.model;

import com.stone.shop.user.model.User;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 结算订单
 * <p/>
 * Created by stone on 15/5/6.
 */
public class PayOrder extends BmobObject{

    public static final String PAY_METHOD_ALIPAY = "支付宝";
    public static final String PAY_METHOD_WEIXIN = "微信支付";
    public static final String PAY_METHOD_CASH = "货到付款｜现金";
    public static final String PAY_METHOD_CARD = "货到付款｜刷卡";

    // 订单状态
    private String state = Order.ORDER_STATE_NOT_PAY;

    // 订单状态码
    private int code = Order.ORDER_STATE_CODE_IN_PAY;

    // 支付方式
    private String payMethod = "PAY_METHOD_ALIPAY";

    private int count = 0;

    // 结算金额
    private double money = 0.0;

    // 取餐时间
    private String time;

    // 联系电话
    private String phone;

    // 附加信息
    private String tips;

    // 收获地址
    private String address;

    // 所有商品
    private BmobRelation orders;

    // 所属用户
    private User user;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobRelation getOrders() {
        return orders;
    }

    public void setOrders(BmobRelation orders) {
        this.orders = orders;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
