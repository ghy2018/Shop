package com.stone.shop.hbut.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.model.BXTNews;
import com.umeng.analytics.MobclickAgent;

/**
 * 教学类-博学堂-讲座详情界面
 * @date 2014-5-10
 * @author Stone
 */
public class BXTNewsActivity extends BaseActivity {
	
	@SuppressWarnings("unused")
	private static final String TAG = "BXTNewsActivity"; 
	
	@SuppressWarnings("unused")
	private BXTNews news;
	
	private LinearLayout llBXTNewsTopic;
	private LinearLayout llBXTNewsSpeaker;
	private LinearLayout llBXTNewsTime;
	private LinearLayout llBXTNewsLoc;
	private LinearLayout llBXTNewsHolder1;
	private LinearLayout llBXTNewsHolder2;
	private LinearLayout llBXTNewsPoints;
	private LinearLayout llBXTNewsSpeakerInfo;
	
	private TextView tvBXTNewsTitle;
	private TextView tvBXTNewsTopic;
	private TextView tvBXTNewsSpeaker;
	private TextView tvBXTNewsTime;
	private TextView tvBXTNewsLoc;
	private TextView tvBXTNewsHolder1;
	private TextView tvBXTNewsHolder2;
	private TextView tvBXTNewsPoints;
	private TextView tvBXTNewsSpeakerInfo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bxt_news);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		initView();
	}
	
	private void initView() {
		
		tvBXTNewsTitle = (TextView) findViewById(R.id.tv_bxt_news_title);

		tvBXTNewsTopic = (TextView) findViewById(R.id.tv_bxt_news_topic);
		tvBXTNewsSpeaker = (TextView) findViewById(R.id.tv_bxt_news_speaker);
		tvBXTNewsTime = (TextView) findViewById(R.id.tv_bxt_news_time);
		tvBXTNewsLoc = (TextView) findViewById(R.id.tv_bxt_news_loc);
		tvBXTNewsHolder1 = (TextView) findViewById(R.id.tv_bxt_news_holder1);
		tvBXTNewsHolder2 = (TextView) findViewById(R.id.tv_bxt_news_holder2);
		tvBXTNewsPoints = (TextView) findViewById(R.id.tv_bxt_news_point);
		tvBXTNewsSpeakerInfo = (TextView) findViewById(R.id.tv_bxt_news_speaker_info);
		
		llBXTNewsTopic = (LinearLayout) findViewById(R.id.ll_bxt_news_topic);
		llBXTNewsSpeaker = (LinearLayout) findViewById(R.id.ll_bxt_news_speaker);
		llBXTNewsTime = (LinearLayout) findViewById(R.id.ll_bxt_news_time);
		llBXTNewsLoc = (LinearLayout) findViewById(R.id.ll_bxt_news_loc);
		llBXTNewsHolder1 = (LinearLayout) findViewById(R.id.ll_bxt_news_holder1);
		llBXTNewsHolder2 = (LinearLayout) findViewById(R.id.ll_bxt_news_holder2);
		llBXTNewsPoints = (LinearLayout) findViewById(R.id.ll_bxt_news_point);
		llBXTNewsSpeakerInfo = (LinearLayout) findViewById(R.id.ll_bxt_news_speaker_info);
		
		tvBXTNewsTitle.setText(getIntent().getStringExtra("title"));

		if(getIntent().getStringExtra("topic")==null || getIntent().getStringExtra("topic").trim().equals(""))
			llBXTNewsTopic.setVisibility(View.GONE);
		else {
			tvBXTNewsTopic.setText(getIntent().getStringExtra("topic"));
		}
			
		
		if(getIntent().getStringExtra("speaker")==null || getIntent().getStringExtra("speaker").trim().equals(""))
			llBXTNewsSpeaker.setVisibility(View.GONE);
		else
			tvBXTNewsSpeaker.setText(getIntent().getStringExtra("speaker"));
		
		if(getIntent().getStringExtra("time")==null || getIntent().getStringExtra("time").trim().equals(""))
			llBXTNewsTime.setVisibility(View.GONE);
		else
			tvBXTNewsTime.setText(getIntent().getStringExtra("time"));
		
		if(getIntent().getStringExtra("location")==null || getIntent().getStringExtra("location").trim().equals(""))
			llBXTNewsLoc.setVisibility(View.GONE);
		else
			tvBXTNewsLoc.setText(getIntent().getStringExtra("location"));
		
		if(getIntent().getStringExtra("holder1")==null || getIntent().getStringExtra("holder1").trim().equals(""))
			llBXTNewsHolder1.setVisibility(View.GONE);
		else
			tvBXTNewsHolder1.setText(getIntent().getStringExtra("holder1"));
		
		if(getIntent().getStringExtra("holder2")==null || getIntent().getStringExtra("holder2").trim().equals(""))
			llBXTNewsHolder2.setVisibility(View.GONE);
		else
			tvBXTNewsHolder2.setText(getIntent().getStringExtra("holder2"));
		
		if(getIntent().getStringExtra("points")==null || getIntent().getStringExtra("points").trim().equals(""))
			llBXTNewsPoints.setVisibility(View.GONE);
		else {
            //tvBXTNewsPoints.setText(getIntent().getStringExtra("points"));
            tvBXTNewsPoints.setText(Html.fromHtml(getIntent().getStringExtra("points")));
        }

		if(getIntent().getStringExtra("speakerinfo")==null || getIntent().getStringExtra("speakerinfo").trim().equals(""))
			llBXTNewsSpeakerInfo.setVisibility(View.GONE);
		else {
			Log.i("接受到的文本", getIntent().getStringExtra("speakerinfo"));
			//tvBXTNewsSpeakerInfo.setText(getIntent().getStringExtra("speakerinfo").replace("\\n", "n"));
            tvBXTNewsSpeakerInfo.setText(Html.fromHtml(getIntent().getStringExtra("speakerinfo")));
		}
		
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("BXTNewsActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("BXTNewsActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

	
}
