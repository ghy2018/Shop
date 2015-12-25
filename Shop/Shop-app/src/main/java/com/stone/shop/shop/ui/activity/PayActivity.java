package com.stone.shop.shop.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.androidquery.AQuery;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import cn.bmob.v3.BmobUser;

/**
 * 支付页面
 * Created by stone on 15/4/25.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG  = PayActivity.class.getSimpleName();

    private static final boolean DEBUG = false;
    public static final String KEY_PAY_ORDER_MONEY = "com.stone.shop.KEY_PAY_ORDER_MONEY";
    public static final String KEY_PAY_ORDER_ID = "com.stone.shop.KEY_PAY_ORDER_ID";

    private CheckBox cbAlipay, cbWeixin;

    private User user;
    private AQuery aq = new AQuery(this);

    // 支付订单描述数据
    private String product = "校园小菜开发者赏金";
    private String desc = "";

    // 外部数据
    private double money = 5.20;
    private String payOrderId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {

        user = BmobUser.getCurrentUser(this, User.class);

        if (null != user) {
            desc =  String.format("[User objectId: %1s]", user.getObjectId());
        } else {
            desc = "支付时空用户";
        }

        if (getIntent().getExtras() != null) {
            money = getIntent().getDoubleExtra(KEY_PAY_ORDER_MONEY, money);
            payOrderId = getIntent().getStringExtra(KEY_PAY_ORDER_ID);
            getSupportActionBar().setTitle("订单支付");
            aq.id(R.id.btn_pay_shop).text("立即支付");
            aq.id(R.id.et_pay_money).enabled(false);
            product = String.format("[微商铺订单号：%s]", payOrderId);
        } else {
            getSupportActionBar().setTitle("打赏");
            aq.id(R.id.btn_pay_shop).text("立即打赏");
            aq.id(R.id.et_pay_money).enabled(true);
        }

        DecimalFormat df = new DecimalFormat("###.00");
        aq.id(R.id.et_pay_money).text(df.format(money));
        aq.id(R.id.btn_pay_shop).clicked(this);
        aq.id(R.id.rl_m_alipay).clicked(this);
        aq.id(R.id.rl_m_weixin).clicked(this);

        cbAlipay = aq.id(R.id.cb_alipay_method).getCheckBox();
        cbWeixin = aq.id(R.id.cb_weixin_method).getCheckBox();

    }

    private void pay(double money) {
        boolean isAlipay = aq.id(R.id.cb_alipay_method).isChecked();
        if (isAlipay) {
            alipay(money);
        } else {
            paywx(money);
        }
    }


    /**
     * 微信支付
     *
     * @param money
     */
    private void paywx(double money) {
        new BmobPay(PayActivity.this).payByWX(money, product, desc, listener);
    }


    /**
     * 支付宝支付
     *
     * @param money
     */
    private void alipay(double money) {
        new BmobPay(PayActivity.this).pay(money, product, desc, listener);
    }


    /**
     * 查询支付结果
     */
    private void checkPay() {
        showProgressDialog();
        new BmobPay(PayActivity.this).query(payOrderId, new OrderQueryListener() {
            @Override
            public void succeed(String s) {
                dismissProgressDialog();
                ToastUtils.showToast("查询支付订单成功: " + s + "  订单是否相等： " + payOrderId.equals(s));
                setResult(PayOrderActivity.PayState.PAY_STATE_SUCCESS);
                finish();
            }

            @Override
            public void fail(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("支付结果查询失败[%d: %s]", i, s));
                setResult(PayOrderActivity.PayState.PAY_STATE_SUCCESS);
                finish();
            }
        });
    }


    /**
     * 支付异常处理
     *
     * @param code
     * @param msg
     */
    private void handleException(int code, String msg) {
        if (code == -3) {
            ToastUtils.showToast("未安装微信插件");
            installPlugin("BmobPayPlugin.apk");// 从assets安装文件
            ToastUtils.showToast("重新支付");
            //paywx(money);
        } else {
            ToastUtils.showToast(String.format("支付失败[%d: %s]", code, msg));
        }
    }


    @Override
    public void onClick(View v) {
        money = Double.parseDouble(aq.id(R.id.et_pay_money).getEditText().getText().toString());

        if (!DEBUG && money < 5.00) {
            //ToastUtils.showToast("打赏最小金额为 1.00 RMB");
            money = 5.20;
            //return;
        }

        switch (v.getId()) {

            case R.id.rl_m_alipay:
                cbWeixin.setChecked(!cbWeixin.isChecked());
                cbAlipay.setChecked(!cbAlipay.isChecked());
                break;

            case R.id.rl_m_weixin:
                cbWeixin.setChecked(!cbWeixin.isChecked());
                cbAlipay.setChecked(!cbAlipay.isChecked());
                break;

            case R.id.btn_pay_shop:
                pay(money);
                break;
        }
    }

    // 从/assets文件夹安装支付插件
    void installPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    PayListener listener = new PayListener() {

        @Override
        public void orderId(String s) {
            payOrderId = s;
            if (BmobConfig.DEBUG)
                ToastUtils.showToast("PayOrderId:  " + s);
        }

        @Override
        public void succeed() {
            checkPay();
        }

        @Override
        public void fail(int code, String msg) {
            Log.d(PayActivity.class.getSimpleName(), "code : " + code + " msg: " + msg);
            handleException(code, msg);
            setResult(PayOrderActivity.PayState.PAY_STATE_FAIL);
            finish();
        }

        @Override
        public void unknow() {
            ToastUtils.showToast("系统出现未知错误，支付失败");
            setResult(PayOrderActivity.PayState.PAY_STATE_FAIL);
            finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(PayOrderActivity.PayState.PAY_STATE_CANCEL);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
