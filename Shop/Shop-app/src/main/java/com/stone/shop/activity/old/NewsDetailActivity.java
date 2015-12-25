package com.stone.shop.activity.old;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.application.Options;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.main.model.News;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 新闻内容显示界面
 * 
 * @date 2014-5-8
 * @author Stone
 */
public class NewsDetailActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "NewsDetailActivity";

	private TextView tvNewsTitle;
	private TextView tvNewsAuthor;
	private TextView tvNewsTime;
	private TextView tvNewsContent;
	private ImageView imgNews;

	private String newsID;
	private String newsTitle;
	private String newsAuthor;
	private String newsTime;
	private String newsContent;

	private News news;

	private DisplayImageOptions options;

	// private ProgressDialog progress;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				refresh();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		initView();

		getIntentData();
		getNewsByID();

	}

	private void initView() {

		tvNewsTitle = (TextView) findViewById(R.id.tv_news_title);
		tvNewsAuthor = (TextView) findViewById(R.id.tv_news_author);
		tvNewsTime = (TextView) findViewById(R.id.tv_news_time);
		tvNewsContent = (TextView) findViewById(R.id.tv_news_content);
		imgNews = (ImageView) findViewById(R.id.img_news);

		// 初始化图片加载设置
		options = Options.getListOptions();
		// progress = new ProgressDialog(this);
		// progress.setCanceledOnTouchOutside(false);
	}

	private void refresh() {

		// 加载图片
		// loadImage();
		if (news != null) {
			tvNewsTitle.setText(news.getTitle());
			tvNewsAuthor.setText("作者: " + news.getAuthor());
			tvNewsTime.setText("发布日期 : " + news.getCreatedAt());
			tvNewsContent.setText(news.getContent());

			// 加载图片
			/*if(null!=news.getPicNews()) {
				ShopApplication
				.getInstance()
				.getImageLoader()
				.displayImage(news.getPicNews().getFileUrl(this), imgNews,
						options);
			}*/

			AQuery aq = ShopApplication.getAQuery();
			if(aq != null && news.getPicNews()!=null) {
				aq.id(imgNews).image(news.getPicNews().getFileUrl(this), true, true);
			}
		}

	}

	// 获取Intent中传入的新闻数据
	private void getIntentData() {
		newsID = getIntent().getStringExtra("NewsID");
	}

	/**
	 * 根据ID查找新闻
	 * 
	 * @date 2014-9-16
	 * @author Stone
	 */
	private void getNewsByID() {

		// progress.show();
		news = new News();
		BmobQuery<News> query = new BmobQuery<News>();
		query.addWhereEqualTo("objectId", newsID);
		query.findObjects(this, new FindListener<News>() {

			@Override
			public void onSuccess(List<News> object) {
				// progress.dismiss();
				if (object != null) {
					news = object.get(0);
					// 发送消息开始加载图片
					Message msg = new Message();
					msg.what = 0;
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// progress.dismiss();
				toast("都怪小菜我, 获取数据失败了");
			}
		});
	}

	/**
	 * 加载图片显示
	 * 
	 * @author Stone
	 * @date 2014-9-16
	 */
	@Deprecated
	private void loadImage() {
		// 只加载图片,后面两个参数是图片的大小
		if (news.getPicNews() != null)
			news.getPicNews().loadImageThumbnail(this, imgNews, 700, 350, 60);
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
}
