package com.stone.shop.hbut.model;

/**
 * 学期信息
 *
 * Created by stonekity.shi on 2015/4/6.
 */
public class Semester {

    /**
     * JSON Back:  {"key":"20142","value":"2014学年 第二学期"}
     */

    private String key;
    private String value;

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value.replace(" ", "\r\n");
    }

    public void setKey(String paramString) {
        this.key = paramString;
    }

    public void setValue(String paramString) {
        this.value = paramString;
    }
}
