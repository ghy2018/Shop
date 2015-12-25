package com.stone.shop.shop.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.user.ui.activity.AddressActivity;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ui.adapter.CartListAdapter;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.main.ui.fragment.NavigationDrawerFragment;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.shop.model.Order;
import com.stone.shop.shop.model.PayOrder;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.LocalBroadcasts;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 结算页面
 * <p/>
 * Created by stone on 15/5/6.
 */
public class PayOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PayOrderActivity";

    // 生成结算订单结果广播
    public static final String RECEIVER_BATCH_UPDATE_ORDERS_RESULT = "com.stone.shop.receive.batch.update.orders.result";

    // 结算订单结果广播
    public static final String RECEIVER_PAY_ORDER_RESULT = "com.stone.shop.RECEIVER_PAY_ORDER_RESULT";

    // 收货地址
    public static final int REQUEST_CODE_SET_ADDRESS = 100;

    // 支付
    public static final int REQUEST_CODE_PAY_ORDER = 200;

    private ListView lvShopCart;
    private CartListAdapter mOrderListAdapter;
    private List<Order> mOrderList;

    private PayOrder payOrder;
    // 订单附加信息
    private PayInfoHolder payInfoHolder = new PayInfoHolder();
    // 订单结算结果显示
    private PayResultHolder payResHolder = new PayResultHolder();

    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        initData();
    }

    private void initView() {
        lvShopCart = (ListView) findViewById(R.id.lv_shop_cart);
        lvShopCart.setVisibility(View.VISIBLE);
        initFooter();
        mOrderListAdapter = new CartListAdapter(this, CartListAdapter.TYPE_PAY_ORDER_ADAPTER);
        lvShopCart.setAdapter(mOrderListAdapter);

        initResultHolder();
    }

    private void initFooter() {
        View footer = LayoutInflater.from(this).inflate(R.layout.list_footer_pay_order, null);
        lvShopCart.addFooterView(footer);
        Button btnPayOrder = (Button) footer.findViewById(R.id.btn_pay_order);
        btnPayOrder.setOnClickListener(this);
        ViewGroup vgSetPayMethod = (ViewGroup) findViewById(R.id.rl_set_pay_method);
        vgSetPayMethod.setOnClickListener(this);
        ViewGroup vgSetAddress = (ViewGroup) findViewById(R.id.rl_set_address);
        vgSetAddress.setOnClickListener(this);

        payInfoHolder.vgPayMethod = (ViewGroup) footer.findViewById(R.id.ll_pay_order_method);
        payInfoHolder.vgPayAddress = (ViewGroup) footer.findViewById(R.id.ll_pay_order_address);
        payInfoHolder.tvPayMethod = (TextView) footer.findViewById(R.id.tv_pay_order_method);
        payInfoHolder.tvPayAddress = (TextView) footer.findViewById(R.id.tv_pay_order_address);

        payInfoHolder.vgPayMethod.setVisibility(View.GONE);
        payInfoHolder.vgPayAddress.setVisibility(View.GONE);
    }

    /**
     * 订单结算结果显示
     */
    private void initResultHolder() {
        payResHolder.vgPayResult = (ViewGroup) findViewById(R.id.rl_pay_order_result);
        payResHolder.imgPayResult = (ImageView) findViewById(R.id.img_pay_order_result);
        payResHolder.tvPayResult = (TextView) findViewById(R.id.tv_pay_order_result);

        payResHolder.vgPayResult.setVisibility(View.GONE);
    }


    private void initData() {
        mOrderList = ShopCartModule.getInstance().getAll();
        if (mOrderList != null) {
            mOrderListAdapter.refresh(mOrderList);
            return;
        }

        curUser = BmobUser.getCurrentUser(this, User.class);

        // 刷新购物车
        ShopCartModule.getInstance().refresh();
    }

    /**
     * 初始化结算订单
     *
     * @return 创建成功返回 true
     */
    private boolean initPayOrder() {

        // 订单信息是否完整
        boolean isInfoVaild = false;
        if (payInfoHolder != null) {
            if (!payInfoHolder.tvPayMethod.getText().equals("") && !payInfoHolder.tvPayAddress.getText().equals(""))
                isInfoVaild = true;
        }

        if (!isInfoVaild) {
            ToastUtils.showToast("订单信息不足，请完善后结算订单");
            return false;
        }

        // 计算付款金额
        double money = totalPay();
        if (money < 0) {
            ToastUtils.showToast("订单付款金额计算失败");
            return false;
        }

        // 调试
        if (BmobConfig.DEBUG)
            money = 0.01;

        showProgressDialog();
        payOrder = new PayOrder();
        BmobRelation orders = new BmobRelation();
        for (Order order : mOrderList)
            orders.add(order);
        payOrder.setOrders(orders);
        payOrder.setUser(curUser);
        payOrder.setCount(mOrderList.size());
        payOrder.setMoney(money);
        payOrder.setPayMethod(payInfoHolder.tvPayMethod.getText().toString());
        payOrder.setAddress(payInfoHolder.tvPayAddress.getText().toString());
        payOrder.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();

                // 更新购物车订单数据状态, 并接受广播事件，是否成功批量更新所有订单的状态
                updOrders(PayState.PAY_STATE_NOT_PAY, true);

                //TODO  修改此处代码
                // 广播更新结算订单数据[首页]
                Intent intent = new Intent(NavigationDrawerFragment.RECEIVER_UPDATE_PAY_ORDER_ACTION);
                LocalBroadcasts.sendLocalBroadcast(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                payOrder = null;
                ToastUtils.showToast("结算订单提交失败");
            }
        });
        return true;
    }

    /**
     * 计算订单总价格
     */
    private double totalPay() {
        if (mOrderList == null) {
            ToastUtils.showToast("获取购物车订单数据失败");
            return -1;
        }

        if (mOrderList.isEmpty()) {
            ToastUtils.showToast("购物车订单数据为空，无法申请结算");
            return -1;
        }

        double totalPay = 0;
        Iterator<Order> iterator = mOrderList.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            totalPay += order.getCost();
        }

        return totalPay;
    }

    /**
     * 更新购物车中订单状态
     *
     * @param payState 待支付｜支付失败｜支付成功
     */
    private void updOrders(int payState, boolean callback) {
        if (mOrderList == null) {
            ToastUtils.showToast("购物车结算异常");
            return;
        }

        String orderState = Order.ORDER_STATE_NOT_PAY;
        switch (payState) {
            case PayState.PAY_STATE_NOT_PAY:
                orderState = Order.ORDER_STATE_NOT_PAY;
                break;
            case PayState.PAY_STATE_FAIL:
                orderState = Order.ORDER_STATE_PAY_FAIL;
            case PayState.PAY_STATE_SUCCESS:
                orderState = Order.ORDER_STATE_PAY_SUCCESS;
                break;
        }

        List<BmobObject> updBatchOrders = new ArrayList<>();
        Iterator<Order> iterator = mOrderList.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            order.setState(orderState);
            updBatchOrders.add(order);
        }

        //TODO 代码修改
        //批量更新购物车中订单状态
        //showProgressDialog();
        ShopCartModule.getInstance().batchUpdateOrders(updBatchOrders, callback);
    }

    /**
     * 支付结果回调后， 更新结算订单状态
     */
    private void updPayOrder(final int payType) {
        if (payOrder == null) {
            ToastUtils.showToast("结算订单数据异常");
            return;
        }

        if (payType == PayState.PAY_STATE_FAIL)
            payOrder.setState(Order.ORDER_STATE_PAY_FAIL);
        else if (payType == PayState.PAY_STATE_SUCCESS)
            payOrder.setState(Order.ORDER_STATE_PAY_SUCCESS);
        else if(payType == PayState.PAY_STATE_NOT_PAY)
            payOrder.setState(Order.ORDER_STATE_NOT_PAY);

        showProgressDialog();
        payOrder.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("成功更新结算订单状态");

                // 不接受广播事件回调
                updOrders(payType, false);

                // 广播更新结算订单数据
                Intent intent = new Intent(NavigationDrawerFragment.RECEIVER_UPDATE_PAY_ORDER_ACTION);
                LocalBroadcasts.sendLocalBroadcast(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("更新结算订单失败[ %d: %s]", i, s));
                updOrders(payType, false);
            }
        });

    }


    /**
     * 显示订单结算结果
     *
     * @param payState  订单支付状态
     */
    private void handlePayResult(int payState) {
        payResHolder.vgPayResult.setVisibility(View.VISIBLE);
        lvShopCart.setVisibility(View.GONE);
        String payStateStr = "";
        boolean isSuccess = false;
        switch (payState) {
            case PayState.PAY_STATE_NOT_PAY:
                payStateStr = "未支付， 请重新支付";
                isSuccess = false;
                break;
            case PayState.PAY_STATE_FAIL:
                payStateStr = "抱歉，支付失败";
                isSuccess = false;
                break;
            case PayState.PAY_STATE_CANCEL:
                payStateStr = "取消支付， 请立即支付";
                isSuccess = false;
                break;
            case PayState.PAY_STATE_SUCCESS:
                payStateStr = "恭喜！订单支付成功";
                isSuccess = true;
                break;
        }
        if (isSuccess) {
            payResHolder.imgPayResult.setBackgroundResource(R.drawable.ic_cb_checked_p);
            payResHolder.tvPayResult.setText(payStateStr);
        } else {
            payResHolder.imgPayResult.setBackgroundResource(R.drawable.ic_close);
            payResHolder.tvPayResult.setText(payStateStr);
        }
    }


    /**
     * 生成结算订单并跳转支付页面
     */
    private void goPay() {

        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
            return;
        }

        // 初始化结算订单
        initPayOrder();
    }

    private void selectPayMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择支付方式")
                .setItems(R.array.pay_methods, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadPayMethod(getResources().getStringArray(R.array.pay_methods)[which]);
                    }
                });
        //builder.create().show();
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }

    private void setAddress() {
        //TODO 跳转设置收货地址
        Intent intent = new Intent(PayOrderActivity.this, AddressActivity.class);
        intent.putExtra(AddressActivity.KEY_TYPE_SELECTED_ADDRESS, AddressActivity.TYPE_SELECT_ADDRESS);
        startActivityForResult(intent, REQUEST_CODE_SET_ADDRESS);
    }

    /**
     * 显示收款方式
     *
     * @param method
     */
    private void loadPayMethod(String method) {
        payInfoHolder.vgPayMethod.setVisibility(View.VISIBLE);
        payInfoHolder.tvPayMethod.setText("收款方式: " + method);
    }

    /**
     * 显示收货地址
     *
     * @param address
     */
    private void loadAddress(String address) {
        payInfoHolder.vgPayAddress.setVisibility(View.VISIBLE);
        StringBuffer info = new StringBuffer();
        info.append("收货人：");
        User curUser = BmobUser.getCurrentUser(this, User.class);
        if (!curUser.getNickname().equals(""))
            info.append(curUser.getNickname());
        else
            info.append(curUser.getUsername());
        info.append("\n地址：" + address);
        info.append("\n联系电话：" + curUser.getPhone());
        payInfoHolder.tvPayAddress.setText(info.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] actions = {RECEIVER_BATCH_UPDATE_ORDERS_RESULT, RECEIVER_PAY_ORDER_RESULT};
        LocalBroadcasts.registerLocalReceiver(receiver, actions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcasts.unregisterLocalReceiver(receiver);
    }

    long lastClickTimeMills = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 订单结算
            case R.id.btn_pay_order:
                long curTimeMills = System.currentTimeMillis();
                if(curTimeMills - lastClickTimeMills <500) {
                    lastClickTimeMills = curTimeMills;
                    return;
                }
                goPay();
                break;

            case R.id.rl_set_pay_method:
                selectPayMethod();
                break;

            case R.id.rl_set_address:
                setAddress();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 填写收货地址
        if (requestCode == REQUEST_CODE_SET_ADDRESS) {
            if (resultCode == RESULT_OK) {
                String address = data.getStringExtra(AddressActivity.KEY_EXTRA_SELECTED_ADDRESS);
                if (address.equals("")) {
                    ToastUtils.showToast("地址为空，请重新填写收货地址");
                } else {
                    loadAddress(address);
                }
            }
        }

        // 订单支付
        else if (requestCode == REQUEST_CODE_PAY_ORDER) {
            int payState = resultCode;
            String payStateStrFormat = "支付状态 : %1$s[ %2$d ]";
            String payStateStr = "";
            switch (payState) {
                case PayState.PAY_STATE_NOT_PAY:
                    payStateStr = String.format(payStateStrFormat, "未支付", payState);
                    break;
                case PayState.PAY_STATE_CANCEL:
                    Log.d(TAG, String.format(payStateStrFormat, "取消支付", payState));
                    break;
                case PayState.PAY_STATE_FAIL:
                    Log.d(TAG, String.format(payStateStrFormat, "支付失败", payState));
                    break;
                case PayState.PAY_STATE_SUCCESS:
                    Log.d(TAG, String.format(payStateStrFormat, "支付成功", payState));
                    break;
            }
            Log.d(TAG, payStateStr);
            ToastUtils.showLongToast(payStateStr);
            handlePayResult(payState);

            // 更新结算订单状态
            updPayOrder(payState);
        }

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismissProgressDialog();
            //订单批量更新是否成功
            if(intent.getAction().equals(RECEIVER_BATCH_UPDATE_ORDERS_RESULT)) {
                boolean isSuccess = intent.getBooleanExtra(ShopCartModule.KEY_EXTRA_INIT_PAY_ORDER_RESULT, false);
                if (BmobConfig.DEBUG) {
                    ToastUtils.showToast("接受订单结算初始化结果广播: " + (isSuccess ? "初始化成功" : "初始化失败"));
                    Log.d(TAG, "接受订单结算初始化结果广播: " + (isSuccess ? "初始化成功" : "初始化失败"));
                }

                if(isSuccess) {
                    Intent payIntent = new Intent(PayOrderActivity.this, PayActivity.class);
                    payIntent.putExtra(PayActivity.KEY_PAY_ORDER_MONEY, payOrder.getMoney());
                    payIntent.putExtra(PayActivity.KEY_PAY_ORDER_ID, payOrder.getObjectId());
                    startActivityForResult(payIntent, REQUEST_CODE_PAY_ORDER);
                }
            }

            /*else if(intent.getAction().equals(RECEIVER_PAY_ORDER_RESULT)) {
                boolean isSuccess = intent.getBooleanExtra(ShopCartModule.KEY_EXTRA_PAY_ORDER_RESULT, false);
                if (BmobConfig.DEBUG) {
                    ToastUtils.showToast("接受订单结算结果广播: " + (isSuccess ? "结算成功" : "结算失败"));
                    Log.d(TAG, "接受订单结算结果广播: " + (isSuccess ? "结算成功" : "结算失败"));
                }
                handlePayResult(isSuccess);
            }*/

        }
    };


    public class PayInfoHolder {
        public ViewGroup vgPayMethod;
        public ViewGroup vgPayAddress;
        public TextView tvPayMethod;
        public TextView tvPayAddress;
    }

    public class PayResultHolder {
        public ViewGroup vgPayResult;
        public ImageView imgPayResult;
        public TextView tvPayResult;
        public Button btnPayResult;
    }

    public interface PayState {
        int PAY_STATE_NOT_PAY = 1001;
        int PAY_STATE_FAIL = 1002;
        int PAY_STATE_CANCEL = 1003;
        int PAY_STATE_SUCCESS = 1004;
    }

}
