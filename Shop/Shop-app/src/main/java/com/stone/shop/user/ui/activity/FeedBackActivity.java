package com.stone.shop.user.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.model.FeedBack;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 意见反馈界面
 *
 * @author Stone
 * @date 2014-5-27
 */
public class FeedBackActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "FeedBackActivity";

    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {
        etContent = (EditText) findViewById(R.id.et_feedback_content);
    }

    /**
     * 提交用户的反馈信息
     */
    private void submit() {
        String content = etContent.getText().toString();
        if (content.equals("")) {
            ToastUtils.showToast("先写点东西吧");
        } else {
            User user = BmobUser.getCurrentUser(this, User.class);
            FeedBack fb = new FeedBack();
            fb.setUser(user);
            fb.setState(FeedBack.FEEDBACK_STATE_NO_REPLY);
            fb.setContent(content);
            fb.save(this, new SaveListener() {

                @Override
                public void onSuccess() {
                    ToastUtils.showToast("提交成功, 感谢您对校园小菜的支持");
                    finish();
                }

                @Override
                public void onFailure(int arg0, String msg) {
                    ToastUtils.showToast("提交失败");
                }

            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FeedBackActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FeedBackActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


}
