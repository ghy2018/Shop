package com.stone.shop.activity.old;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.model.LuckyUser;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 每日一抽页面
 *
 * @author Stone
 * @date 2014-5-18
 */
public class AwardActivity extends BaseActivity {

    private EditText etAwardNew;
    private EditText etAwardOld;

    private String awardNew;
    private String awardOld;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        initData();
    }

    private void initView() {
        etAwardNew = (EditText) findViewById(R.id.et_award_new);
        etAwardOld = (EditText) findViewById(R.id.et_award_old);

        etAwardNew.setText(awardNew);
        etAwardOld.setText(awardOld);
    }

    private void initData() {
        BmobQuery<LuckyUser> query = new BmobQuery<LuckyUser>();
        query.order("-updateAt");
        query.findObjects(this, new FindListener<LuckyUser>() {

            @Override
            public void onSuccess(List<LuckyUser> list) {
                awardNew = list.get(0).getUsername() + "      " + list.get(0).getAward();
                awardOld = list.get(1).getUsername() + "      " + list.get(1).getAward();
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int arg0, String arg1) {
                toast("获取中奖名单失败");
            }
        });
    }

    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public void clickBack(View v) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AwardActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AwardActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


}
