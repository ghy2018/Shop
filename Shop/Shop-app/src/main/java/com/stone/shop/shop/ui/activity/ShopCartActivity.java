package com.stone.shop.shop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ui.adapter.CartListAdapter;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.shop.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by stone on 15/4/4.
 */
public class ShopCartActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ShopCartActivity.class.getSimpleName();

    private TextView tvLabel;
    private Button btnPayOrder;
    private Button btnTaoYiTao;
    private ViewGroup llHint;
    private ViewGroup emptyView;

    private ListView lvShopCart;
    private CartListAdapter mOrderListAdapter;
    private List<Order> mOrderList;

    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            dismissProgressDialog();
            mOrderList = ShopCartModule.getInstance().getAll();
            mOrderListAdapter.refresh(mOrderList);
            showEmptyView(mOrderList.size() == 0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
    }


    private void initView() {
        llHint = (ViewGroup) findViewById(R.id.ll_shop_cart_hint);
        tvLabel = (TextView) findViewById(R.id.tv_shop_cart_label);
        btnPayOrder = (Button) findViewById(R.id.btn_shop_cart_pay_order);
        btnPayOrder.setOnClickListener(this);
        btnTaoYiTao = (Button) findViewById(R.id.btn_shop_cart_buy);
        btnTaoYiTao.setOnClickListener(this);
        emptyView = (ViewGroup) findViewById(R.id.rl_shop_cart_empty);
        lvShopCart = (ListView) findViewById(R.id.lv_shop_cart);
        mOrderList = new ArrayList<>();
        mOrderListAdapter = new CartListAdapter(this);
        lvShopCart.setAdapter(mOrderListAdapter);

        ShopCartModule.getInstance().registerObserver(observer);
    }


    private void initData() {
        mOrderList = ShopCartModule.getInstance().getAll();
        if (mOrderList != null) {
            mOrderListAdapter.refresh(mOrderList);
            showEmptyView(mOrderList.size() == 0);
            return;
        }

        // 刷新购物车
        ShopCartModule.getInstance().refresh();

    }

    /**
     * 申请结算
     */
    private void payOrder() {
        Intent intent = new Intent(ShopCartActivity.this, PayOrderActivity.class);
        startActivity(intent);
    }

    private void showEmptyView(boolean isShow) {
        if (isShow) {
            emptyView.setVisibility(View.VISIBLE);
            llHint.setVisibility(View.GONE);
            lvShopCart.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            llHint.setVisibility(View.VISIBLE);
            lvShopCart.setVisibility(View.VISIBLE);
            String format = getResources().getString(R.string.text_shop_cart_count_hint);
            tvLabel.setText(String.format(format, mOrderList.size()));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        ShopCartModule.getInstance().registerObserver(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopCartModule.getInstance().unregisterObserver(observer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shop_cart_pay_order:
                payOrder();
                break;

            case R.id.btn_shop_cart_buy:
                finish();
                break;
            default:
                break;
        }
    }
}
