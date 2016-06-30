package com.stone.shop.main.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.main.ui.fragment.NavigationDrawerFragment;
import com.stone.shop.shop.ui.fragment.ShopListFragment;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;

/**
 * Created by stone on 15/4/16.
 */
public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavFragment;

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;

    private ShopListFragment mShopListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavFragment.setUp(R.id.navigation_drawer, mDrawerLayout);
        onNavigationDrawerItemSelected(0);

        mShopListFragment = new ShopListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, mShopListFragment);
        ft.commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        ToastUtils.showToast(position + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().updUserState(User.USER_STATE_LOGOUT);
    }

    private long exitTime = 0;

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToast("再按一次退出校园小菜");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
