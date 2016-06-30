package com.stone.shop.shop.model;

import com.stone.shop.user.model.User;

import cn.bmob.v3.BmobObject;

/**
 * 订单实体类［购物车］
 *
 * @author Stone
 * @date 2014-4-24
 */
public class Order extends BmobObject {

    //购物车
    public static final int ORDER_STATE_CODE_IN_BAG = 0x001;
    public static final String ORDER_STATE_IN_BAG = "加入购物车";
    public static final String ORDER_STATE_OUT_BAG = "从购物车移除";

    //当前订单状态
    public static final int ORDER_STATE_CODE_IN_PAY = 0x002;
    public static final String ORDER_STATE_NOT_PAY = "待付款";
    public static final String ORDER_STATE_PAY_FAIL = "付款失败";
    public static final String ORDER_STATE_PAY_SUCCESS = "付款成功";
    public static final String ORDER_STATE_DILIVER = "配送中";
    public static final String ORDER_STATE_DILIVER_FAIL = "配送失败";
    public static final String ORDER_STATE_DILIVER_SUCCESS = "已签收";
    public static final String ORDER_STATE_BACK = "退款中";
    public static final String ORDER_STATE_BACK_FAIL = "退款失败";
    public static final String ORDER_STATE_BACK_SUCCESS = "退款成功";

    //历史订单状态
    public static final int ORDER_STATE_CODE_FINISHED = 0x003;
    public static final String ORDER_STATE_NOT_STAR = "未评价";
    public static final String ORDER_STATE_HAS_STAR = "已评价";
    public static final String ORDER_STATE_ERROR = "订单异常";

    // 订单状态
    private String state = ORDER_STATE_IN_BAG;

    // 订单状态码
    private int stateCode = ORDER_STATE_CODE_IN_BAG;

    // 数量
    private Integer count;

    // 订单总价
    private Double cost;

    // 商品
    private Good good;

    // 下单用户
    private User user;


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        setStateCodeHelper(state);
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // 自动补充stateCode信息
    public void setStateCodeHelper(String state) {

        if (state.equals(ORDER_STATE_IN_BAG)
                || state.equals(ORDER_STATE_OUT_BAG)
                || state.equals(ORDER_STATE_NOT_PAY)) {
            this.stateCode = ORDER_STATE_CODE_IN_BAG;
        }

        else if( state.equals(ORDER_STATE_NOT_PAY)
                || state.equals(ORDER_STATE_PAY_FAIL)
                || state.equals(ORDER_STATE_PAY_SUCCESS)
                || state.equals(ORDER_STATE_DILIVER)
                || state.equals(ORDER_STATE_DILIVER_FAIL)
                || state.equals(ORDER_STATE_DILIVER_SUCCESS)
                || state.equals(ORDER_STATE_BACK)
                || state.equals(ORDER_STATE_BACK_FAIL)
                || state.equals(ORDER_STATE_BACK_SUCCESS)) {
            this.stateCode = ORDER_STATE_CODE_IN_PAY;
        }

        else if(state.equals(ORDER_STATE_NOT_STAR)
                || state.equals(ORDER_STATE_HAS_STAR)
                || state.equals(ORDER_STATE_ERROR)){
            this.stateCode = ORDER_STATE_CODE_FINISHED;
        }

        else {
            this.stateCode = -1;
        }
    }


}
