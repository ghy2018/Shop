package com.stone.shop.activity.old;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.adapter.LoverWallListAdapter;
import com.stone.shop.hbut.model.Lover;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LoverWallActivity extends BaseActivity implements OnClickListener {

	@SuppressWarnings("unused")
	private static final String TAG = "LoverWallActivity";

	private ListView lvLoverWall;
	private LoverWallListAdapter loverWallListAdapter;
	private List<Lover> loverList;

	// 分页加载
	private static final int STATE_REFRESH = 0;// 下拉刷新
	private int limit = 6; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				toast("开始刷新列表");
				// 通知Adapter数据更新
				loverWallListAdapter.refresh((ArrayList<Lover>) loverList);
				loverWallListAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lover_wall);

		initView();
		// loadData(0, STATE_REFRESH);
	}

	private void initView() {
		loverList = new ArrayList<Lover>();
		lvLoverWall = (ListView) findViewById(R.id.lv_lover_wall);
		loverWallListAdapter = new LoverWallListAdapter(this, loverList);
		lvLoverWall.setAdapter(loverWallListAdapter);
	}

	/**
	 * 分页获取数据
	 * 
	 * @param page
	 *            页码
	 * @param actionType
	 *            ListView的操作类型（下拉刷新、上拉加载更多）
	 */
	private void loadData(final int page, final int actionType) {

		BmobQuery<Lover> query = new BmobQuery<Lover>();
		query.order("-createdAt");
		query.setLimit(limit); // 设置每页多少条数据
		query.setSkip(page * limit); // 从第几条数据开始，
		query.findObjects(this, new FindListener<Lover>() {

			@Override
			public void onSuccess(List<Lover> arg0) {

				if (arg0 != null && arg0.size() > 0) {

					// 将本次查询的数据添加到bankCards中
					for (Lover lover : arg0) {
						loverList.add(lover);
					}

					// 通知Adapter数据更新
					Message msg = new Message();
					msg.what = 0;
					mHandler.sendMessage(msg);

					// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
					curPage++;
					toast("第" + (page + 1) + "页数据加载完成");
				} else {
					toast("没有更多数据了");
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				toast("查询失败:" + arg1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_load_more:
			// 加载更多数据
			loadData(curPage, STATE_REFRESH);
			break;
		default:
			break;
		}
	}

	/**
	 * 返回发现
	 * 
	 * @param v
	 */
	public void backToFinder(View v) {
		finish();
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("LoverWallActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("LoverWallActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

}
