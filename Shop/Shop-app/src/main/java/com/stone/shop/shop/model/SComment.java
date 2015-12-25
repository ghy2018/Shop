package com.stone.shop.shop.model;

import cn.bmob.v3.BmobObject;

/**
 * 店铺评论实体类
 * 
 * @date 2014-5-3
 * @author Stone
 */
@SuppressWarnings("serial")
public class SComment extends BmobObject {

	private String username;
	private String content;
	private Shop shop;

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
