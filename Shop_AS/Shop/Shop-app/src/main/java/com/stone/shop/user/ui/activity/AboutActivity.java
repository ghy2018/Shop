package com.stone.shop.user.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.widget.TitleBarView;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于软件
 *
 * @author Stone
 */
public class AboutActivity extends BaseActivity {

    private TitleBarView titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //initTitleBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    private void initTitleBar() {
        titleBar = (TitleBarView) findViewById(R.id.titlebar);
        titleBar.getLeftBtn().setVisibility(View.VISIBLE);
        titleBar.getLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


}
