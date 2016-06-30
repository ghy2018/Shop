package com.stone.shop.shop.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ui.adapter.OrderInfoListAdapter;
import com.stone.shop.shop.model.PayOrder;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * 订单详情页面
 *
 * @author Stone
 * @date 2014-5-27
 */
public class OrderInfoActivity extends BaseActivity implements OnItemLongClickListener, View.OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "OrderInfoActivity";
    public static final String KEY_INTENT_ORDER_TYPE = "com.stone.shop.KEY_INTENT_ORDER_TYPE";

    /*private ExpandableStickyListHeadersListView lvOrderInfo;*/
    private ListView lvOrderInfo;
    private ViewGroup emptyView;
    private Button btnTaoYiTao;
    private OrderInfoListAdapter orderInfoListAdapter;
    private List<PayOrder> orderList = new ArrayList<>();

    // 显示订单类型
    private int showType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        showType = getIntent().getIntExtra(KEY_INTENT_ORDER_TYPE, -1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
    }

    private void initView() {
        /*lvOrderInfo = (ExpandableStickyListHeadersListView) findViewById(R.id.lv_order_info);*/
        lvOrderInfo = (ListView) findViewById(R.id.lv_order_info);
        emptyView = (ViewGroup) findViewById(R.id.rl_order_list_empty);
        btnTaoYiTao = (Button) findViewById(R.id.btn_order_list_buy);
        btnTaoYiTao.setOnClickListener(this);
        orderInfoListAdapter = new OrderInfoListAdapter(this, orderList);
        lvOrderInfo.setAdapter(orderInfoListAdapter);
        lvOrderInfo.setOnItemLongClickListener(this);

        /*
        lvOrderInfo.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(lvOrderInfo.isHeaderCollapsed(headerId)){
                    lvOrderInfo.expand(headerId);
                }else {
                    lvOrderInfo.collapse(headerId);
                }
            }
        });*/
    }

    // 初始化列表菜单中数据
    public void initData() {

        if (showType == -1) {
            ToastUtils.showToast("订单参数获取错误");
            return;
        }

        // 获取用户
        User user = BmobUser.getCurrentUser(this, User.class);

        // 获取小菜订单(数量)
        showProgressDialog();
        BmobQuery<PayOrder> query = new BmobQuery<>();
        //query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("code", showType);
        //query.include("orders.good.shop");
        query.order("-updatedAt, -createdAt, state");
        query.findObjects(this, new FindListener<PayOrder>() {

            @Override
            public void onSuccess(List<PayOrder> object) {

                dismissProgressDialog();

                if (object.size() == 0)
                    ToastUtils.showToast("当前木有待支付订单");
                ToastUtils.showToast(String.format("当前订单共有 %d 笔", object.size()));
                orderList = object;
                // 通知Adapter数据更新
                orderInfoListAdapter.refresh(orderList);
                showEmptyView(orderList.size() == 0);
            }

            @Override
            public void onError(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("获取订单数据失败 [%d: %s]", code, msg));
            }
        });

    }

    private void showEmptyView(boolean isShow) {
        if (isShow) {
            emptyView.setVisibility(View.VISIBLE);
            lvOrderInfo.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            lvOrderInfo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_order_list_buy:
                finish();
                break;

            default:
                break;
        }
    }

    //订单长按响应事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        PopupMenu popup = new PopupMenu(this, lvOrderInfo);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(OrderInfoActivity.this, "Clicked popup menu item " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OrderInfoActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("OrderInfoActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
