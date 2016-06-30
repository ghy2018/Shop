package com.stone.shop.shop.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * 用户反馈信息
 *
 * @author Stone
 * @date 2014-5-27
 */
@SuppressWarnings("serial")
public class FeedBack extends BmobObject {

    public static final String FEEBBACK_STATE_REPLY = "已回复";
    public static final String FEEDBACK_STATE_NO_REPLY = "未回复";

    // 反馈状态
    private String state = FEEDBACK_STATE_NO_REPLY;

    // 内容
    private String content;

    // 用户信息
    private BmobUser user;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }
}
