package com.stone.shop.hbut.model;

import com.stone.shop.user.model.User;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class Lover extends BmobObject {

	private String boy;
	private String girl;
	private String words;
	private String countZYQ;
	private String countWQJ;
	private String countDBC;

	// 关联数据
	private User boyUser;
	private User girlUser;

	
	
	public String getBoy() {
		return boy;
	}

	public void setBoy(String boy) {
		this.boy = boy;
	}

	public String getGirl() {
		return girl;
	}

	public void setGirl(String girl) {
		this.girl = girl;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getCountZYQ() {
		return countZYQ;
	}

	public void setCountZYQ(String countZYQ) {
		this.countZYQ = countZYQ;
	}

	public String getCountWQJ() {
		return countWQJ;
	}

	public void setCountWQJ(String countWQJ) {
		this.countWQJ = countWQJ;
	}

	public String getCountDBC() {
		return countDBC;
	}

	public void setCountDBC(String countDBC) {
		this.countDBC = countDBC;
	}

	public User getBoyUser() {
		return boyUser;
	}

	public void setBoyUser(User boyUser) {
		this.boyUser = boyUser;
	}

	public User getGirlUser() {
		return girlUser;
	}

	public void setGirlUser(User girlUser) {
		this.girlUser = girlUser;
	}

}
