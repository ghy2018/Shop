package com.stone.shop.activity.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.main.ui.fragment.NewsListAdapter;
import com.stone.shop.main.model.News;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 主界面
 * 
 * @date 2014-4-24
 * @author Stone
 */
public class NewsActivity extends BaseActivity implements OnItemClickListener {

	private static final String TAG = "NewsActivity";

	// 校园新闻
	private ListView lvNewsList;
	private List<News> newsList = new ArrayList<>();
	private NewsListAdapter newsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// 新闻
		lvNewsList = (ListView) findViewById(R.id.lv_news);
		newsListAdapter = new NewsListAdapter(this, newsList);
		lvNewsList.setAdapter(newsListAdapter);
		lvNewsList.setOnItemClickListener(this);
		lvNewsList.setSelector(R.drawable.selector_default);

		getNewsData();
	}

	/**
	 * 初始化新闻列表数据
	 * @date 2014-5-3
	 * @author Stone
	 */
	public void getNewsData() {
		BmobQuery<News> query = new BmobQuery<News>();
		query.order("-createdAt, updatedAt");
		query.findObjects(this, new FindListener<News>() {

			@Override
			public void onSuccess(List<News> object) {
				newsList = object;
				// 通知Adapter数据更新
				newsListAdapter.refresh((ArrayList<News>) newsList);
				newsListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				toast("都怪小菜我, 获取数据失败了");
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent toNewsDetail = new Intent(NewsActivity.this, NewsDetailActivity.class);
		toNewsDetail.putExtra("NewsID", newsList.get(position).getObjectId());
		startActivity(toNewsDetail);
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}
	
	public void clickBack(View v) {
		finish();
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
