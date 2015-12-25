package com.stone.shop.shop.model;

import com.stone.shop.user.model.User;

import cn.bmob.v3.BmobObject;

/**
 * 店铺评论实体类
 *
 * @author Stone
 * @date 2014-5-3
 */
@SuppressWarnings("serial")
public class Comment extends BmobObject {

    public static final String COMMIT_STATE_ENABLE = "合法";
    public static final String COMMIT_STATE_CHECK = "审核中";
    public static final String COMMIT_STATE_DELETE_USER = "用户删除";
    public static final String COMMIT_STATE_DELETE_SHOP_OWNER = "店主强制删除";
    public static final String COMMIT_STATE_DELETE_FORCE = "管理员强制删除";

    public static final String COMMIT_TO_SHOP = "店铺评论";
    public static final String COMMIT_TO_GOOD = "商品评论";
    public static final String COMMIT_TO_USER = "用户评论";

    //匿名状态下默认用户名
    public static final String COMMENT_USERNAME_D = "匿名用户";

    // 评论类型
    private String type = COMMIT_TO_SHOP;

    // 状态
    private String state = COMMIT_STATE_ENABLE;

    // 是否匿名
    private boolean showname = true;

    // 内容
    private String content;

    // 评论用户
    private User user;

    // 评论对象为店铺
    private Shop shop;

    // 评论对象为商品
    private Good good;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowname() {
        return showname;
    }

    public void setShowname(boolean showname) {
        this.showname = showname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }
}
