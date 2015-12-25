package com.stone.shop.user.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.R;
import com.stone.shop.main.ui.activity.LoginActivity;
import com.stone.shop.main.ui.activity.SettingActivity;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.application.BaseApplication;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人资料卡
 *
 * @author Stone
 * @date 2014-5-21
 */
public class MineInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG  = "MineInfoActivity";

    private User curUser;
    private AQuery aq;

    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        initView();
    }

    private void init() {
        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(true);
    }


    private void initView() {

        if(aq == null)
            aq = new AQuery(this);
        curUser = BmobUser.getCurrentUser(this, User.class);


        aq.id(R.id.btn_mine_info_logout).clicked(this);
        if(curUser==null)
            return;

        if(!curUser.getNickname().equals(""))
            aq.id(R.id.tv_mineinfo_username).text(curUser.getNickname());
        else
            aq.id(R.id.tv_mineinfo_username).text(curUser.getUsername());
        aq.id(R.id.tv_mineinfo_stuid).text(curUser.getStuID());
        aq.id(R.id.tv_mineinfo_school).text(curUser.getSchool());
        aq.id(R.id.tv_mineinfo_cademy).text(curUser.getCademy());
        aq.id(R.id.tv_mineinfo_dorpart).text(curUser.getDorPart());
        aq.id(R.id.tv_mineinfo_dornum).text(curUser.getDorNum());
        aq.id(R.id.tv_mineinfo_phone).text(curUser.getPhone());
        aq.id(R.id.tv_mineinfo_qq).text(curUser.getQQ());


        if(curUser.getPicUser()!=null)
            aq.id(R.id.img_mine_info_icon).image(curUser.getPicUser().getFileUrl(this));
        else
            aq.id(R.id.img_mine_info_icon).image(R.drawable.ic_xiaocai_weixin);

        if(curUser.getSex().equals(User.SEX_MALE)) {
            aq.id(R.id.img_mine_info_sex).getImageView().setVisibility(View.VISIBLE);
            aq.id(R.id.img_mine_info_sex).image(R.drawable.ic_sex_boy);
        }
        else if(curUser.getSex().equals(User.SEX_FEMALE)) {
            aq.id(R.id.img_mine_info_sex).getImageView().setVisibility(View.VISIBLE);
            aq.id(R.id.img_mine_info_sex).image(R.drawable.ic_sex_girl);
        }
        else {
            aq.id(R.id.img_mine_info_sex).getImageView().setVisibility(View.GONE);
        }

        aq.id(R.id.ll_address_manager).clicked(this);
        aq.id(R.id.ll_setting_manager).clicked(this);

    }

    /**
     * 编辑资料卡
     */
    private void gotoEdit() {
        Intent toEditMineInfo = new Intent(MineInfoActivity.this, MineInfoEditActivity.class);
        startActivityForResult(toEditMineInfo, 200);
    }



    /**
     * 返回到登陆页面
     */
    private void goLogin() {
        Intent intent = new Intent(MineInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 退出登陆－有两种方式，根据当前的登陆方式选择登出操作 1. Bmob账号 2. 学号
     */
    private void logout() {
        if(curUser.getStuID().equals("") || LoginManager.getInstance().getStuID().equals("")) {
            logoutByXiaoCai();
        } else {
            logoutBySchool();
        }
    }


    /**
     * 小菜账号退出登陆
     */
    private void logoutByXiaoCai() {
        progress.show();
        User user = BmobUser.getCurrentUser(this, User.class);
        user.setState(User.USER_STATE_LOGOUT);
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                BmobUser.logOut(BaseApplication.getAppContext());
                LoginManager.getInstance().updUserState(User.USER_STATE_LOGOUT);
                goLogin();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast("退出登陆失败，请稍后再试");
                progress.dismiss();
            }
        });
    }


    /**
     * 学号退出登陆 并退出对应的小菜账号
     */
    public void logoutBySchool() {

        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals("")) {
            ToastUtils.showToast("未检测到学号登陆记录");
            goLogin();
            return;
        }

        progress.show();

        String url = HBUTConfig.urlLogout();
        AjaxCallback<String> callback = new AjaxCallback<String>();
        callback.url(url).type(String.class).weakHandler(this, "logoutCallBack");
        Map<String, ?> cookiesMap = CookiesManager.getInstance().getCookies();
        Iterator<String> iterator = cookiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String cookieName = iterator.next();
            String cookieValue = (String) cookiesMap.get(cookieName);
            callback.cookie(cookieName, cookieValue);
        }
        ShopApplication.getAQuery().ajax(callback);

    }

    public void logoutCallBack(String url, String string, AjaxStatus status) {

        progress.dismiss();

        if(BmobConfig.DEBUG)
            Log.d(TAG, "logoutCallBack: " + string);

        if (string != null) {
            if(isLogoutSuccess(string)) {
                if(BmobConfig.DEBUG)
                    Log.d(TAG, "退出登陆成功");

                //推出当前登陆的小菜账号
                logoutByXiaoCai();

            } else {
                ToastUtils.showToast("HBUT服务器数据异常，请稍后重试");
            }

        } else {
            if(BmobConfig.DEBUG)
                Log.d(TAG, "退出登陆失败");
            ToastUtils.showToast("退出登陆失败，请检查网络稍后重试");
        }
    }


    /**
     * 判断是否退出登陆成功
     * @param json
     * @return
     */
    private boolean isLogoutSuccess(String json) {
        HashMap<String, String> map = JSONManager.getLoginResult(json);

        if(null == map)
            return false;

        if (BmobConfig.DEBUG)
            Log.d(TAG, map.toString());

        if (map.containsKey("Status") && map.get("Status").equals("0")) {
            return true;
        } else {
            if (map.containsKey("Message")) {
                ToastUtils.showToast("退出登录失败： " + map.get("Message"));
                return false;
            }
        }

        return false;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_mine_info_logout:
                logout();
                break;

            //地址管理
            case R.id.ll_address_manager:
                goAddressManager();
                break;

            //设置
            case R.id.ll_setting_manager:
                goSettingManager();
                break;

            default:
                break;
        }
    }

    private void goAddressManager() {
        Intent intent = new Intent(MineInfoActivity.this, AddressActivity.class);
        intent.putExtra(AddressActivity.KEY_TYPE_SELECTED_ADDRESS, AddressActivity.TYPE_LOAD_ADDRESS);
        startActivity(intent);
    }

    private void goSettingManager() {
        Intent intent = new Intent(MineInfoActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            initView();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mine_info, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                gotoEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineInfoActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineInfoActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
