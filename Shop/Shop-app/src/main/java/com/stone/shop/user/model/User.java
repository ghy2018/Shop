package com.stone.shop.user.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 用户实体类
 *
 * @author Stone
 * @date 2014-4-24
 */
@SuppressWarnings("serial")
public class User extends BmobUser implements Serializable{

    public static final String SEX_MALE = "男";
    public static final String SEX_FEMALE = "女";

    public static final String USER_TYPE_NORMAL = "普通用户";
    public static final String USER_TYPE_PAY = "付费用户";
    public static final String USER_TYPE_VIP = "认证用户";
    public static final String USER_TYPE_WIN = "中奖用户";
    public static final String USER_TYPE_BLACK = "黑名单";

    public static final String USER_ROLE_CUSTOME = "学生";
    public static final String USER_ROLE_OWNNER = "商户";
    public static final String USER_ROLE_ADMIN = "管理员";
    public static final String USER_ROLE_ROOT = "病毒";

    public static final String USER_STATE_LOGIN_BY_XIAOCAI = "登录[小菜]";
    public static final String USER_STATE_LOGIN_BY_SCHOOL = "登录[学号]";
    public static final String USER_STATE_LOGOUT = "离线";

    public static final String HBUT_USERNAME_FORMAT = "HBUT_%1s";
    public static final String HBUT_PASSWORD_FORMAT = "HBUT_%1s_*";

    // 学号
    private String stuID = "";

    // 教务系统登陆密码
    private String stuPsd = "";

    // 昵称
    private String nickname = "";

    // 性别
    private String sex = "";

    // 电话
    private String phone = "";

    // QQ
    private String qq = "";

    // 学校
    private String school = "";

    // 学院
    private String cademy = "";

    // 班级
    private String className = "";

    // 校区
    private String dorPart = "";

    // 寝室号
    private String dorNum = "";

    // 收货地址
    private ArrayList<String> address;

    // 用户登录状态
    private String state = USER_STATE_LOGOUT;

    // 用户类型
    private String type = USER_TYPE_NORMAL;

    // 用户角色
    private String role = USER_ROLE_CUSTOME;

    // 用户头像
    private BmobFile picUser;


    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getStuPsd() {
        return stuPsd;
    }

    public void setStuPsd(String stuPsd) {
        this.stuPsd = stuPsd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQQ() {
        return qq;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCademy() {
        return cademy;
    }

    public void setCademy(String cademy) {
        this.cademy = cademy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDorPart() {
        return dorPart;
    }

    public void setDorPart(String dorPart) {
        this.dorPart = dorPart;
    }

    public String getDorNum() {
        return dorNum;
    }

    public void setDorNum(String dorNum) {
        this.dorNum = dorNum;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BmobFile getPicUser() {
        return picUser;
    }

    public void setPicUser(BmobFile picUser) {
        this.picUser = picUser;
    }


}
