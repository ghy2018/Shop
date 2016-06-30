package com.stone.shop.activity.old;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 购物车主界面
 *
 * @author Stone
 * @date 2014-4-24
 */
public class WsqActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "CarActivity";

    private static final String URL_WSQ = "http://wx.wsq.qq.com/231782938";
    private WebView wsqWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        wsqWebView = (WebView) findViewById(R.id.wv_wsq);
        init();

    }

    public void init() {
        if (Build.VERSION.SDK_INT >= 19) {
            wsqWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wsqWebView.getSettings().setLoadsImagesAutomatically(false);
        }

        // -----------------------------------------------------------------
        wsqWebView.getSettings().setJavaScriptEnabled(true);    // 设置使用够执行JS脚本
        //wsqWebView.getSettings().setBuiltInZoomControls(true);	// 设置使支持缩放
        wsqWebView.getSettings().setDefaultFontSize(12);
        wsqWebView.setWebChromeClient(new WebChromeClient());
        wsqWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!wsqWebView.getSettings().getLoadsImagesAutomatically()) {
                    wsqWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

        });
        wsqWebView.loadUrl(URL_WSQ);
        // ------------------------------------------------
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WsqActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WsqActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
