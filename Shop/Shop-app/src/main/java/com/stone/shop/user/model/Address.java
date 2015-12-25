package com.stone.shop.user.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 收货地址
 *
 * Created by stone on 15/5/6.
 */
public class Address extends BmobObject implements Serializable{

    private String address;

    private User user;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
