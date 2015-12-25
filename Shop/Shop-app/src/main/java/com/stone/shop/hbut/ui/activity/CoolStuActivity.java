package com.stone.shop.hbut.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.adapter.CoolListAdapter;
import com.stone.shop.hbut.model.Cool;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CoolStuActivity extends BaseActivity implements OnClickListener {

	@SuppressWarnings("unused")
	private static final String TAG = "CoolStuActivity";

	private Button btnCoolListOfBoy;
	private Button btnCoolListOfGirl;
	private Button btnLoadMoreCool;

	private ListView lvCoolStu;
	private CoolListAdapter coolListAdapter;
	private List<Cool> coolList;
	private List<Cool> coolListCopy;

	// 分页加载
	private static final int STATE_REFRESH = 0;// 下拉刷新
	private int limit = 6; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始
	
	private ProgressDialog progress;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				//toast("开始刷新列表");
				// 通知Adapter数据更新
				coolListAdapter.refresh((ArrayList<Cool>) coolList);
				coolListAdapter.notifyDataSetChanged();
				progress.dismiss();
			}
			if (msg.what == 1) {
				//toast("开始刷新列表");
				// 通知Adapter数据更新
				coolListAdapter.refresh((ArrayList<Cool>) coolListCopy);
				coolListAdapter.notifyDataSetChanged();
				progress.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cool_stu);

		initView();
		loadData(0, STATE_REFRESH);
	}

	private void initView() {
		coolList = new ArrayList<Cool>();
		coolListCopy = new ArrayList<Cool>();
		lvCoolStu = (ListView) findViewById(R.id.lv_cool_all);
		coolListAdapter = new CoolListAdapter(this, coolList);
		lvCoolStu.setAdapter(coolListAdapter);
		lvCoolStu.setSelector(R.drawable.selector_default);

		btnCoolListOfBoy = (Button) findViewById(R.id.btn_cool_boy);
		btnCoolListOfGirl = (Button) findViewById(R.id.btn_cool_girl);
		btnLoadMoreCool = (Button) findViewById(R.id.btn_load_more);

		btnCoolListOfBoy.setOnClickListener(this);
		btnCoolListOfGirl.setOnClickListener(this);
		btnLoadMoreCool.setOnClickListener(this);
		
		progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);

	}

	private void reloadData(String tag) {
		coolListCopy.clear();
		coolListCopy.addAll(coolList);
		Iterator<Cool> iterator = coolListCopy.iterator();
		while (iterator.hasNext()) {
			if (!iterator.next().getSex().equals(tag)) {
				iterator.remove();
			}
		}

		// 从本地缓存重新加载
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
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

		progress.show();
		
		BmobQuery<Cool> query = new BmobQuery<Cool>();
		//query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.order("-createdAt");
		query.setLimit(limit); // 设置每页多少条数据
		query.setSkip(page * limit); // 从第几条数据开始，
		query.findObjects(this, new FindListener<Cool>() {

			@Override
			public void onSuccess(List<Cool> arg0) {
				
				if (arg0!=null && arg0.size() > 0) {

					// 将本次查询的数据添加到bankCards中
					for (Cool cool : arg0) {
						if(cool.getPhoto()!=null){
							//获得图片的下载地址
							//cool.getPhoto().getFileUrl(this);
						}
						coolList.add(cool);
					}
					
					// 通知Adapter数据更新
					Message msg = new Message();
					msg.what = 0;
					mHandler.sendMessage(msg);

					// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
					curPage++;
					//toast("第" + (page + 1) + "页数据加载完成");
				} else {
					toast("没有更多数据了");
					progress.dismiss();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				progress.dismiss();
				toast("查询失败:" + arg1);
			}
		});
	}
	
	/**
	 * 增加男神&女神
	 * @param v
	 */
	public void addCool(View v)
	{
		Intent toAddCool = new Intent(CoolStuActivity.this, CoolAddActivity.class);
		startActivity(toAddCool);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cool_boy:
			btnCoolListOfBoy.setEnabled(false);
			btnCoolListOfGirl.setEnabled(true);

			btnCoolListOfBoy.setTextColor(this.getResources().getColor(R.color.text_color_white));
			btnCoolListOfGirl.setTextColor(this.getResources().getColor(R.color.bg_color_first));

			reloadData("男");
			break;

		case R.id.btn_cool_girl:
			btnCoolListOfBoy.setEnabled(true);
			btnCoolListOfGirl.setEnabled(false);

			btnCoolListOfBoy.setTextColor(this.getResources().getColor(R.color.bg_color_first));
			btnCoolListOfGirl.setTextColor(this.getResources().getColor(R.color.text_color_white));

			reloadData("女");
			break;
		case R.id.btn_load_more:
			//加载更多数据
			loadData(curPage, STATE_REFRESH);
			break;
		default:
			break;
		}
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("CoolStuActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("CoolStuActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

}
