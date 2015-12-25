package com.stone.shop.base.interfaces;

/**
 * 网络请求回调接口
 *
 * Created by stonekity.shi on 2015/4/7.
 */
public interface BaseCallBack {
    public void onSuccess();
    public void onFails(int code, String msg);
}
