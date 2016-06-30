package com.stone.shop.shop.model;


import com.stone.shop.user.model.User;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 店铺实体类， 实现序列化, Activity之间实现传递
 *
 * @author Stone
 * @date 2014-4-24
 */
public class Shop extends BmobObject implements Serializable {

    private static final long serialVersionUID = -8796635595320697255L;

    public static final String SHOP_TYPE_NORMAL = "普通店铺";
    public static final String SHOP_TYPE_VERTIFY = "会员店铺";
    public static final String SHOP_TYPE_OFFICIAL = "官方店铺";
    public static final String SHOP_TYPE_PROMOTED = "促销店铺";

    public static final String SHOP_STATE_INIT = "审核中";
    public static final String SHOP_STATE_ON_SALE = "营业中";
    public static final String SHOP_STATE_OFF_SALE = "休息中";
    public static final String SHOP_STATE_CLOSED = "已关闭";
    public static final String SHOP_STATE_BLACK = "黑名单";

    // 类型(店铺的分类)
    private String type = SHOP_TYPE_NORMAL;

    // 店铺状态
    private String state = SHOP_STATE_INIT;

    // 店名
    private String name;

    // 地理位置
    private String location;

    // 联系电话
    private String phone;

    // 简介
    private String info;

    // 评分
    private float rates = 0.0f;

    // 促销信息
    private String sale;

    // 经营范围
    private String scrope;

    // 是否是官方认证店铺
    private Boolean isOfficial = false;

    // 是否参与了推广计划
    private Boolean isPromoted = false;

    // 是否支持货到付款
    private Boolean canPayCash = false;

    // 是否支持送货上门
    private Boolean canDiliver = false;

    // 商店主图
    private BmobFile picShop;

    // 店铺评论
    private BmobRelation comments;

    // 店铺商品
    private BmobRelation goods;

    // 店铺持有者
    private User user;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getRates() {
        return rates;
    }

    public void setRates(float rates) {
        this.rates = rates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobRelation getGoods() {
        return goods;
    }

    public void setGoods(BmobRelation goods) {
        this.goods = goods;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public BmobFile getPicShop() {
        return picShop;
    }

    public void setPicShop(BmobFile picShop) {
        this.picShop = picShop;
    }

    public Boolean getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(Boolean isPromoted) {
        this.isPromoted = isPromoted;
    }

    public Boolean getCanPayCash() {
        return canPayCash;
    }

    public void setCanPayCash(Boolean canPayCash) {
        this.canPayCash = canPayCash;
    }

    public Boolean getCanDiliver() {
        return canDiliver;
    }

    public void setCanDiliver(Boolean canDiliver) {
        this.canDiliver = canDiliver;
    }

    public String getScrope() {
        return scrope;
    }

    public void setScrope(String scrope) {
        this.scrope = scrope;
    }

    public Boolean getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

}
