package com.stone.shop.hbut.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.adapter.BXTListAdapter;
import com.stone.shop.hbut.model.BXTNews;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 教学类-博学堂界面
 *
 * @author Stone
 * @date 2014-5-10
 */
public class BXTActivity extends BaseActivity implements OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, OnScrollListener {

    @SuppressWarnings("unused")
    private static final String TAG = "BXTActivity";

    private ListView lvBXTNews;
    private BXTListAdapter mBxtListAdapter;
    private List<BXTNews> mBXTNewsList;
    private SwipeRefreshLayout swipeLayout;

    // 下拉刷新
    private static final int STATE_REFRESH = 0;// 下拉刷新
    @SuppressWarnings("unused")
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxt);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        queryData(0, STATE_REFRESH);

    }

    private void initView() {

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.lv_shop_all_swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvBXTNews = (ListView) findViewById(R.id.lv_bxt_news);
        mBXTNewsList = new ArrayList<BXTNews>();
        mBxtListAdapter = new BXTListAdapter(this, mBXTNewsList);
        lvBXTNews.setAdapter(mBxtListAdapter);
        lvBXTNews.setOnItemClickListener(this);
        lvBXTNews.setOnScrollListener(this);
        //lvBXTNews.setSelector(R.drawable.selector_default);
    }

    @SuppressWarnings("unused")
    private void initData() {
        BmobQuery<BXTNews> query = new BmobQuery<BXTNews>();
        query.findObjects(this, new FindListener<BXTNews>() {

            @Override
            public void onSuccess(List<BXTNews> newsList) {
                // toast("查询商品成功, 共" + newsList.size());
                if (newsList.size() == 0)
                    toast("亲, 暂时还木有讲座哦");
                else {
                    mBXTNewsList = newsList;
                    mBxtListAdapter.refresh(newsList);
                    mBxtListAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(int arg0, String arg1) {
                toast("查询失败");
            }
        });
    }

    /**
     * 分页获取数据
     *
     * @param page       页码
     * @param actionType ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(final int page, final int actionType) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:"
                + actionType);

        showProgressDialog();

        if (actionType == STATE_REFRESH) {
            curPage = 0;
            mBXTNewsList.clear();
            mBxtListAdapter.refresh(mBXTNewsList);
            mBxtListAdapter.notifyDataSetChanged();
        }

        BmobQuery<BXTNews> query = new BmobQuery<BXTNews>();
        query.order("-createdAt");
        query.setLimit(limit); // 设置每页多少条数据
        query.setSkip(page * limit); // 从第几条数据开始，
        query.findObjects(this, new FindListener<BXTNews>() {

            @Override
            public void onSuccess(List<BXTNews> arg0) {

                dismissProgressDialog();

                if (arg0.size() > 0) {

                    // 将本次查询的数据添加到bankCards中
                    for (BXTNews bxtNews : arg0) {
                        mBXTNewsList.add(bxtNews);
                    }
                    // 通知Adapter数据更新
                    mBxtListAdapter.refresh((ArrayList<BXTNews>) mBXTNewsList);
                    mBxtListAdapter.notifyDataSetChanged();
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    //toast("第" + (page + 1) + "页数据加载完成");
                } else {
                    toast("没有更多数据了");
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                dismissProgressDialog();
                toast("查询失败:" + arg1);
            }
        });
    }

    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent toBXTNewsActivity = new Intent(BXTActivity.this,
                BXTNewsActivity.class);
        toBXTNewsActivity.putExtra("title", mBXTNewsList.get(position)
                .getTitle());
        toBXTNewsActivity.putExtra("topic", mBXTNewsList.get(position)
                .getTopic());
        toBXTNewsActivity.putExtra("speaker", mBXTNewsList.get(position)
                .getSpeaker());
        toBXTNewsActivity
                .putExtra("time", mBXTNewsList.get(position).getTime());
        toBXTNewsActivity.putExtra("location", mBXTNewsList.get(position)
                .getLocation());
        toBXTNewsActivity.putExtra("holder1", mBXTNewsList.get(position)
                .getHolder1());
        toBXTNewsActivity.putExtra("holder2", mBXTNewsList.get(position)
                .getHolder2());
        toBXTNewsActivity.putExtra("points", mBXTNewsList.get(position)
                .getPoints());
        toBXTNewsActivity.putExtra("speakerinfo", mBXTNewsList.get(position)
                .getSpeakerinfo());
        startActivity(toBXTNewsActivity);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(true);
                queryData(0, STATE_REFRESH);
                swipeLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BXTActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BXTActivity"); // 保证 onPageEnd 在onPause 之前调用,因为
        // onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {

                if(mBXTNewsList.size()<limit) {
                    ToastUtils.showToast("已经加载所有数据");
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        queryData(curPage, STATE_MORE);
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

}
