package com.stone.shop.shop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ui.adapter.ShopListAdapter;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.shop.ShopManager;
import com.stone.shop.shop.model.Shop;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 某一分类下的所有店铺页面
 *
 * @author Stone
 * @date 2014-4-26
 */
public class ShopAllActivity extends BaseActivity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    @SuppressWarnings("unused")
    private static final String TAG = "ShopAllActivity";

    private TextView tvTitle;
    private TextView tvEmptyBg;  //当数据为空时现实的视图

    private ListView lvShopAllList;
    private ShopListAdapter shopListAdapter;
    private SwipeRefreshLayout swipeLayout;

    private ArrayList<Shop> shopList = new ArrayList<Shop>();

    //下拉刷新
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    //上级页面的标记
    private String isFrom = "";

    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_all);

        initView();

        //获取商店数据
        queryData(0, STATE_REFRESH);
    }

    public void initView() {

        //上级页面的标记
        isFrom = getIntent().getStringExtra("isFrom");

        if (BmobConfig.DEBUG) {
            Log.i(TAG, "isFrom = " + isFrom);
        }

        //设置标题
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(isFrom);

        tvEmptyBg = (TextView) findViewById(R.id.ll_msg_empty);
        tvEmptyBg.setVisibility(View.GONE);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.lv_shop_all_swipe_container);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        lvShopAllList = (ListView) findViewById(R.id.lv_shop_all);
        shopListAdapter = new ShopListAdapter(this, shopList);
        lvShopAllList.setAdapter(shopListAdapter);
        lvShopAllList.setOnItemClickListener(this);
        lvShopAllList.setOnScrollListener(this);
        //lvShopAllList.setSelector(R.drawable.selector_default);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ShopManager.getInstance().setSelectedShop(shopList.get(position));
        Intent toShopItem = new Intent(ShopAllActivity.this, ShopItemActivity.class);
        startActivity(toShopItem);
    }

    /**
     * 分页获取数据
     *
     * @param page       页码
     * @param actionType ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(final int page, final int actionType) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        if (actionType == STATE_REFRESH || page == 0) {
            curPage = 0;
            shopList.clear();
        }

        showProgressDialog();
        BmobQuery<Shop> query = new BmobQuery<Shop>();
        String[] states = {Shop.SHOP_STATE_ON_SALE, Shop.SHOP_STATE_OFF_SALE}; //店铺状态控制
        query.addWhereContainsAll("state", Arrays.asList(states));
        query.setSkip(page * limit);        // 从第几条数据开始
        query.setLimit(limit);            // 设置每页多少条数据
        query.order("-isOfficial, -isPromoted");
        query.findObjects(this, new FindListener<Shop>() {

            @Override
            public void onSuccess(List<Shop> list) {

                dismissProgressDialog();

                if (list.size() > 0) {

                    for (Shop shop : list) {
                        shopList.add(shop);
                    }
                    // 通知Adapter数据更新
                    shopListAdapter.refresh(shopList);
                    shopListAdapter.notifyDataSetChanged();
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    ToastUtils.showToast("第" + (page + 1) + "页数据加载完成");
                } else {
                    if (page == 0) {
                        tvEmptyBg.setVisibility(View.VISIBLE);
                    } else {
                        tvEmptyBg.setVisibility(View.GONE);
                    }
//                    if (page == 1) {
//                        //刷新第一页
//                        shopList.clear();
//                        // 通知Adapter数据更新
//                        shopListAdapter.refresh(shopList);
//                        shopListAdapter.notifyDataSetChanged();
//                        queryData(0, STATE_REFRESH);
//                    }
                    ToastUtils.showToast("没有更多数据了");
                }
            }

            @Override
            public void onError(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("加载首页数据出错 [%d: %s]", code, msg));
            }
        });
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                queryData(0, STATE_REFRESH);
            }
        }, 1000);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {

                if (shopList.size() < limit) {
                    ToastUtils.showToast("已经加载所有数据");
                    return;
                }

                if (curPage == 0)
                    return;

                new Handler().post(new Runnable() {
                    public void run() {
                        queryData(curPage, STATE_MORE);
                    }
                });
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
