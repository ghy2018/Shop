package com.stone.shop.shop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.activity.BXTActivity;
import com.stone.shop.hbut.ui.activity.CoolStuActivity;
import com.stone.shop.activity.old.LoverWallActivity;
import com.stone.shop.activity.old.NewsActivity;
import com.stone.shop.hbut.ui.activity.SecondTradeActivity;
import com.stone.shop.activity.old.WsqActivity;
import com.stone.shop.main.ui.fragment.GridAdapter;
import com.stone.shop.main.ui.fragment.ImagePagerAdapter;
import com.stone.shop.base.widget.AutoScrollViewPager;
import com.stone.shop.base.widget.MyGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品主界面
 *
 * @author Stone
 * @date 2014-4-24
 */
public class ShopActivity extends BaseActivity implements OnItemClickListener,
        OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "ShopActivity";

    // 图片轮播
    private FrameLayout flImageAds;
    private AutoScrollViewPager viewPager;
    private List<View> mImgViews;
    private ImageButton btnHideAds;
    private int[] mImgResId = {R.drawable.ic_banner_5, R.drawable.ic_banner_6,
            R.drawable.ic_banner_7, R.drawable.ic_banner1};

    private MyGridView gvCampus; // 校园
    private MyGridView gvFinder; // 发现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        initView();
        initScollViewPager();
    }

    /**
     * 初始化组件并适配数据
     */
    public void initView() {
        gvCampus = (MyGridView) findViewById(R.id.gv_campus);
        gvFinder = (MyGridView) findViewById(R.id.gv_finder);

        // 校园
        gvCampus.setAdapter(new GridAdapter(this, 0));
        gvCampus.setOnItemClickListener(this);
        gvCampus.setSelector(R.drawable.selector_default);

        // 发现
        gvFinder.setAdapter(new GridAdapter(this, 1));
        gvFinder.setOnItemClickListener(this);
        gvFinder.setSelector(R.drawable.selector_default);

    }

    public void initScollViewPager() {
        flImageAds = (FrameLayout) findViewById(R.id.fl_image_ads);
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        btnHideAds = (ImageButton) findViewById(R.id.btn_hide_ads);

        mImgViews = new ArrayList<View>();
        for (int i = 0; i < mImgResId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(mImgResId[i]);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            mImgViews.add(imageView);
        }

        btnHideAds.setOnClickListener(this);

        viewPager.setAdapter(new ImagePagerAdapter(this, mImgViews));
        viewPager.setInterval(3000); // 设置自动滚动的间隔时间，单位为毫秒
        viewPager.setDirection(AutoScrollViewPager.RIGHT); // 设置自动滚动的方向，默认向右
        viewPager.setCycle(true); // 是否自动循环轮播，默认为true
        viewPager.setScrollDurationFactor(3); // 设置ViewPager滑动动画间隔时间的倍率，达到减慢动画或改变动画速度的效果
        viewPager.setStopScrollWhenTouch(true); // 当手指碰到ViewPager时是否停止自动滚动，默认为true
        viewPager.setBorderAnimation(true); // 设置循环滚动时滑动到从边缘滚动到下一个是否需要动画，默认为true
        viewPager
                .setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);// 滑动到第一个或最后一个Item的处理方式，支持没有任何操作、轮播以及传递到父View三种模式
        viewPager.startAutoScroll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hide_ads:
                flImageAds.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.i("GridView点击了： ", "position" + position);
        Intent intent;
        switch (parent.getId()) {

            // 校园
            case R.id.gv_campus:
                if (position == 0) { // 微社区
                    intent = new Intent(ShopActivity.this, WsqActivity.class);
                    startActivity(intent);
                } else if (position == 1) { // 校园动态
                    intent = new Intent(ShopActivity.this, NewsActivity.class);
                    startActivity(intent);
                } else if (position == 2) { //博学堂
                    intent = new Intent(ShopActivity.this, BXTActivity.class);
                    startActivity(intent);
                } else if (position == 3) { //社团联盟
                    intent = new Intent(ShopActivity.this, ShopAllActivity.class);
                    intent.putExtra("isFrom", "社团联盟");
                    startActivity(intent);
                } else if (position == 4) { //二手交易
                    intent = new Intent(ShopActivity.this, SecondTradeActivity.class);
                    startActivity(intent);
                }
                break;

            // 发现
            case R.id.gv_finder:
                if (position == 0) { // 校花校草
                    intent = new Intent(ShopActivity.this, CoolStuActivity.class);
                    startActivity(intent);
                } else if (position == 1) { // 表白墙
                    intent = new Intent(ShopActivity.this, LoverWallActivity.class);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }

    }

    @SuppressWarnings("unused")
    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    ;

    /**
     * @param title 父分类标题
     * @param type
     */
    @SuppressWarnings("unused")
    private void toShopAllActivity(String title, String type) {
        Intent toShopAll = new Intent(ShopActivity.this, ShopAllActivity.class);
        toShopAll.putExtra("title", title);
        // 当前点击的项的父分类
        toShopAll.putExtra("type", type);
        startActivity(toShopAll);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ShopActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ShopActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
