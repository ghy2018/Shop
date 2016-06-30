package com.stone.shop.activity.old;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.util.HttpNetService;
import com.stone.shop.base.util.TaskExecutor;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends BaseActivity {

	private MineWebView mWv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        init();
        setContentView(mWv);
    }

    public void init () {
    	mWv = new MineWebView(this);
    	
	    if(Build.VERSION.SDK_INT >= 19) {
	    	mWv.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	    	mWv.getSettings().setLoadsImagesAutomatically(false);
	    }
	    mWv.getSettings().setJavaScriptEnabled(true);	// 设置使用够执行JS脚本
	    mWv.getSettings().setBuiltInZoomControls(true);	// 设置使支持缩放
	    mWv.getSettings().setDefaultFontSize(12);
	    mWv.setWebChromeClient(new WebChromeClient());
	    mWv.loadUrl("http://wx.wsq.qq.com/231782938");
	}

    protected class MineWebView extends WebView {
        private String mLoadUrl;

        public MineWebView(Context context) {
            super(context);
        }

        private Runnable mFetchRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String htmlStr = HttpNetService.fetchHtml(mLoadUrl);
                    if (htmlStr != null) {
                        // 远程网页html代码中可以使用 file:///android_asset/...来访问apk本地资源
                        TaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadDataWithBaseURL(mLoadUrl, htmlStr, "text/html", "UTF-8", "");
                                WebActivity.this.setTitle("加载完成");
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    android.util.Log.e("MineWebView", "Exception:" + e.getMessage());
                }

                TaskExecutor.runTaskOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        android.util.Log.e("MineWebView", "fetch html failed");
                        // TODO 出错时可以处理自定义界面或其他事务 .....
                        WebActivity.this.setTitle("加载失败");
                        Toast.makeText(WebActivity.this, "fetch html failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        @Override
        public void loadUrl (String url) {
            mLoadUrl = url;
            WebActivity.this.setTitle("正在加载...");
            TaskExecutor.executeTask(mFetchRunnable);
        }
    }
    
    @Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("WebActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("WebActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}


}
