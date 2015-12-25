package com.stone.shop.main.ui.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.readystatesoftware.viewbadger.BadgeView;
import com.stone.shop.R;
import com.stone.shop.user.ui.activity.MineInfoActivity;
import com.stone.shop.user.ui.activity.MineSoftActivity;
import com.stone.shop.shop.ui.activity.OrderInfoActivity;
import com.stone.shop.shop.ui.activity.PayActivity;
import com.stone.shop.shop.ui.activity.ShopCartActivity;
import com.stone.shop.hbut.ui.activity.FinderActivity;
import com.stone.shop.base.application.BaseApplication;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.base.ui.BaseFragment;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.shop.model.Order;
import com.stone.shop.shop.model.PayOrder;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.LocalBroadcasts;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;

import java.util.Observable;
import java.util.Observer;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;

public class NavigationDrawerFragment extends BaseFragment {

    private static final String TAG = "NavigationDrawerFragment";

    private static final String SP_FIRST_DISPLAY_CAMPUS = "com.stone.shop.SP_FIRST_DISPLAY_CAMPUS";
    private static final String KEY_FIRST_DISPLAY_CAMPUS = "com.stone.shop.KEY_FIRST_DISPLAY_CAMPUS";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    private View mFragmentContainerView;

    private boolean mFirstDisplay = true;

    final int radioIds[] = {
            R.id.rb_nav_shop_cart,
            R.id.rb_nav_order_current,
            R.id.rb_nav_order_history,
            R.id.rb_nav_campus,
            R.id.rb_nav_soft_about,
            R.id.rb_nav_pay_coder
    };

    private RelativeLayout rlNavUserInfo;
    private RadioButton radioBtns[] = new RadioButton[radioIds.length];
    private BadgeView badgeCarOrderCount, badgeCurOrderCount, badgeFinishOrderCount, badgeCampus;


    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    protected void initView(View rootView) {
        setHasOptionsMenu(true);

        rlNavUserInfo = (RelativeLayout) getView().findViewById(R.id.rl_nav_user_Info);
        rlNavUserInfo.setOnClickListener(onClickListener);
        for (int i = 0; i < radioIds.length; ++i) {
            radioBtns[i] = (RadioButton) getView().findViewById(radioIds[i]);
            radioBtns[i].setOnClickListener(onClickListener);
        }

        badgeCarOrderCount = (BadgeView) getView().findViewById(R.id.badge_order_car_count);
        badgeCurOrderCount = (BadgeView) getView().findViewById(R.id.badge_order_cur_count);
        badgeFinishOrderCount = (BadgeView) getView().findViewById(R.id.badge_order_finish_count);
        badgeCampus = (BadgeView) getView().findViewById(R.id.badge_campus);

        mFirstDisplay = getStateFromSp();
        if (mFirstDisplay)
            badgeCampus.setVisibility(View.VISIBLE);
        else
            badgeCampus.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        //ShopCartModule.getInstance().registerObserver(observer);
    }

    @Override
    protected void initData(View rootView, Bundle savedInstanceState) {

        //获取订单数据
        reCountPayOrder();

        AQuery aq = new AQuery(getActivity());
        User curUser = BmobUser.getCurrentUser(getActivity(), User.class);
        if (curUser == null)
            return;

        if (!curUser.getNickname().equals(""))
            aq.id(R.id.tv_user_name).text(curUser.getNickname());
        else
            aq.id(R.id.tv_user_name).text(curUser.getUsername());
        if (curUser.getPicUser() != null) {
            aq.id(R.id.img_mine_info_icon).image(curUser.getPicUser().getFileUrl(getActivity()));
        } else {
            aq.id(R.id.img_mine_info_icon).image(R.drawable.ic_xiaocai_weixin);
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (mCallbacks != null) {
                    mCallbacks.onNavigationDrawerItemSelected(mSelectMenuPos);
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void showDrawer(boolean show) {
        if (show) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        } else {
            mDrawerLayout.closeDrawers();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
        ShopCartModule.getInstance().registerObserver(observer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        if (observer != null)
            ShopCartModule.getInstance().unregisterObserver(observer);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcasts.registerLocalReceiver(receiver, RECEIVER_UPDATE_PAY_ORDER_ACTION);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcasts.unregisterLocalReceiver(receiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item)
                || super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
    }

    /**
     * 查看个人资料
     */
    private void goMineInfo() {
        Intent intent = new Intent(getActivity(), MineInfoActivity.class);
        startActivity(intent);
    }


    /**
     * 查看购物车
     */
    private void goShopCart() {
        Intent intent = new Intent(getActivity(), ShopCartActivity.class);
        startActivity(intent);
    }

    /**
     * 查看订单列表
     *
     * @param type 订单类型
     */
    private void goOrder(int type) {
        Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
        intent.putExtra(OrderInfoActivity.KEY_INTENT_ORDER_TYPE, type);
        startActivity(intent);
    }

    /**
     * 校园发现
     */
    private void goCampus() {

        mFirstDisplay = false;
        badgeCampus.setVisibility(View.GONE);
        saveStateToSp();

        Intent intent = new Intent(getActivity(), FinderActivity.class);
        startActivity(intent);
    }

    private void saveStateToSp() {
        SharedPreferences sp = BaseApplication.getAppContext().getSharedPreferences(SP_FIRST_DISPLAY_CAMPUS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_FIRST_DISPLAY_CAMPUS, mFirstDisplay).commit();
    }

    private Boolean getStateFromSp() {
        SharedPreferences sp = BaseApplication.getAppContext().getSharedPreferences(SP_FIRST_DISPLAY_CAMPUS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_FIRST_DISPLAY_CAMPUS, true);
    }


    /**
     * 软件相关
     */
    private void goSoftAbout() {
        Intent intent = new Intent(getActivity(), MineSoftActivity.class);
        startActivity(intent);
    }

    private void payCoder() {
        Intent intent = new Intent(getActivity(), PayActivity.class);
        startActivity(intent);
    }

    private int mSelectMenuPos = 0;

    protected void selectItem(int position) {

        showDrawer(false);
        mSelectMenuPos = position;

        switch (mSelectMenuPos) {
            // 购物车
            case 0:
                goShopCart();
                break;
            // 当前订单
            case 1:
                goOrder(Order.ORDER_STATE_CODE_IN_PAY);
                break;
            // 历史订单
            case 2:
                goOrder(Order.ORDER_STATE_CODE_FINISHED);
                break;
            // 校园发现
            case 3:
                goCampus();
                break;
            // 软件相关
            case 4:
                goSoftAbout();
                break;
            case 5:
                payCoder();
            default:
                break;
        }

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.rl_nav_user_Info) {
                goMineInfo();
                showDrawer(false);
            }

            for (int i = 0; i < radioBtns.length; ++i) {
                if (v.equals(radioBtns[i])) {
                    selectItem(i);
                } else {
                    radioBtns[i].setChecked(false);
                }
            }
        }
    };


    public static final String RECEIVER_UPDATE_PAY_ORDER_ACTION = "com.stone.shop.RECEIVER_UPDATE_PAY_ORDER_ACTION";
    /**
     * 结算订单状态变化时更新
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 更新当前订单、历史订单统计
            reCountPayOrder();
        }
    };


    /**
     * 购物车订单数据观察者
     */
    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {

            // 更新购物车订单统计
            int count = ShopCartModule.getInstance().getCarOrderCount();
            if (count > 0) {
                badgeCarOrderCount.setVisibility(View.VISIBLE);
                badgeCarOrderCount.setText(count+"");
            } else {
                badgeCarOrderCount.setVisibility(View.GONE);
                badgeCarOrderCount.setText("");
            }

        }
    };

    // 重新获取结算订单
    private void reCountPayOrder() {

        if (Utils.isNetworkAvailable(getActivity())) {
            ToastUtils.showToast("网络异常，结算订单数量更新失败");
            return;
        }

        BmobQuery<PayOrder> curOrderQuery = new BmobQuery<>();
        curOrderQuery.addWhereEqualTo("code", Order.ORDER_STATE_CODE_IN_PAY);
        curOrderQuery.count(getActivity(), PayOrder.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (BmobConfig.DEBUG) {
                    Log.d(TAG, "查询到当前订单 " + i);
                }
                updPayOrderCount(i, Order.ORDER_STATE_CODE_IN_PAY);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast(String.format("当前订单数据获取失败 [%d: %s]", i, s));
            }
        });

        BmobQuery<PayOrder> finishOrderQuery = new BmobQuery<>();
        finishOrderQuery.addWhereEqualTo("code", Order.ORDER_STATE_CODE_FINISHED);
        finishOrderQuery.count(getActivity(), PayOrder.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (BmobConfig.DEBUG) {
                    Log.d(TAG, "查询到历史订单 " + i);
                }
                updPayOrderCount(i, Order.ORDER_STATE_CODE_FINISHED);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast(String.format("历史订单数据获取失败 [%d: %s]", i, s));
            }
        });
    }

    // 更新当前订单、历史订单统计
    private void updPayOrderCount(int count, int code) {
        ToastUtils.showToast("开始更新订单数量");

        if (code < 0) {
            ToastUtils.showToast("订单数据获取数量异常");
            return;
        }

        if (code == Order.ORDER_STATE_CODE_IN_PAY) {
            if (count == 0) {
                badgeCurOrderCount.setVisibility(View.GONE);
                badgeCurOrderCount.setText("");
            } else {
                badgeCurOrderCount.setVisibility(View.VISIBLE);
                badgeCurOrderCount.setText(count+"");
            }
        } else if (code == Order.ORDER_STATE_CODE_FINISHED) {
            if (count == 0) {
                badgeFinishOrderCount.setVisibility(View.GONE);
                badgeCurOrderCount.setText("");
            } else {
                badgeFinishOrderCount.setVisibility(View.VISIBLE);
                badgeFinishOrderCount.setText(count+"");
            }
        } else {
            badgeFinishOrderCount.setVisibility(View.GONE);
            badgeFinishOrderCount.setText("");
            // do nothing
        }
    }


    /**
     * 选择回调接口
     */
    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }


}
