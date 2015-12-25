package com.stone.shop.hbut.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class CoolAddActivity extends BaseActivity {
	
	@SuppressWarnings("unused")
	private static final String TAG = "CoolAddActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cool_add);
		
	}
	
	/**
	 * 返回上级Activity
	 * @param v
	 */
	public void backToCoolStu(View v){
		finish();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("CoolAddActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("CoolAddActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

}
