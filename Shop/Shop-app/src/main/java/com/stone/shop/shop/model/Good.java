package com.stone.shop.shop.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 商品实体类
 *
 * @date 2014-4-24
 * @author Stone
 */
public class Good extends BmobObject{

    private static final long serialVersionUID = -3248168273019127389L;

    public static final String GOOD_STATE_INIT = "未上架";
    public static final String GOOD_STATE_ON_SALE = "售卖中";
    public static final String GOOD_STATE_OFF_SALE_USER = "已下架";
    public static final String GOOD_STATE_OFF_SALE_FORCE = "强制下架";
    public static final String GOOD_STATE_ON_PROMOTE = "促销中";

    // 商品状态
    private String state = GOOD_STATE_INIT;

    // 名称
    private String name = "";

    // 原价
    private String price = "";

    // 优惠价
    private String dPrice = "";

    // 库存
    private Integer stock = 0;

    // 销量
    private Integer saleVolume = 0;

    // 分类
    private String category = "";

    private String description = "";

    // 商品主图
    private BmobFile picGood = null;

    // 缩略图
    private BmobRelation picGoods = null;

    //评论
    private BmobRelation comments = null;

    // 所属店铺
    private Shop shop;


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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getdPrice() {
        return dPrice;
    }

    public void setdPrice(String dPrice) {
        this.dPrice = dPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(Integer saleVolume) {
        this.saleVolume = saleVolume;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BmobFile getPicGood() {
        return picGood;
    }

    public void setPicGood(BmobFile picGood) {
        this.picGood = picGood;
    }

    public BmobRelation getPicGoods() {
        return picGoods;
    }

    public void setPicGoods(BmobRelation picGoods) {
        this.picGoods = picGoods;
    }

    public BmobRelation getComments() {
        return comments;
    }

    public void setComments(BmobRelation comments) {
        this.comments = comments;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
