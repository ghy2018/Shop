package com.stone.shop.hbut.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class Cool extends BmobObject {

	
	private String username = ""; // 姓名
	private String sex = ""; // 性别
	private Integer votes = 0; 
	private BmobFile photo = null; // 照片

	public BmobFile getPhoto() {
		return photo;
	}

	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}


}
