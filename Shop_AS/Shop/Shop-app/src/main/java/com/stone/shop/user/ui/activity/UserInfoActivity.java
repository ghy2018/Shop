package com.stone.shop.user.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.base.widget.TitleBarView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @date 2014-5-19
 * @author Stone
 */
public class UserInfoActivity extends BaseActivity {

    public static final String TAG = "UserInfoActivity";
    public static final String EXTRA_KEY_USER_ID = "";

    private TitleBarView titleBar;
    private CircleImageView imgUserIcon;
    private TextView tvUserName;
    private Button btnUserPay;

    private User showUser;
    private User curUser;

    private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUser();

        initView();
	}


    //初始化视图控件
    private void initView() {

        imgUserIcon = (CircleImageView) findViewById(R.id.img_user_info_icon);
        tvUserName = (TextView) findViewById(R.id.tv_user_info_name);
        btnUserPay = (Button) findViewById(R.id.btn_user_info_pay);
        btnUserPay.setText("￥ 1.00 " + btnUserPay.getText());

        btnUserPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Pay
            }
        });
    }


    //填充数据
    private void initData() {
        curUser = BmobUser.getCurrentUser(this, User.class);
        if(showUser == null || curUser == null)
            return;

        if(aq == null) {
            aq = new AQuery(this);
        }
        aq.id(R.id.tv_user_info_name).text(showUser.getUsername());
        aq.id(R.id.img_user_info_icon).image(showUser.getPicUser().getFileUrl(this));
    }



    private void getUser() {
        final String userId = getIntent().getStringExtra(EXTRA_KEY_USER_ID);

        if(userId.equals(""))
            return;

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", userId);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> users) {
                if(users!=null && users.size()==1) {
                    showUser = users.get(0);
                    initData();
                    ToastUtils.showToast("UserID: " + userId);
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showToast("用户不存在");
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("UserInfoActivity"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("UserInfoActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

}
