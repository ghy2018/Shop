package com.stone.shop.user.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.R;
import com.stone.shop.main.ui.activity.LoginActivity;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.hbut.ui.activity.StudentGradeActivity;
import com.stone.shop.user.ui.adapter.MineListAdapter;
import com.stone.shop.base.application.BaseApplication;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.base.config.MessageType;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.shop.model.Order;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.shop.ui.activity.OrderInfoActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人中心主界面
 *
 * @author Stone
 * @date 2014-4-24
 */
public class MineActivity extends BaseActivity implements OnItemClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MineActivity";

    private String[] userItemNames = {""};
    private String[] userItemContents = {""};
    private String[] orderItemNames = {"当前订单", "历史订单"};
    private String[] orderItemContents = {"", ""};
    private String[] aboutItemNames = {"通知中心", "软件相关", "推荐给朋友", "退出账号"};
    private String[] aboutItemContents = {"", "", "", ""};

    private int[] userImgIds = {R.drawable.ic_menu_myplaces};
    private int[] orderImgIds = {R.drawable.ic_menu_find_holo_light,
            R.drawable.ic_menu_copy_holo_light};
    private int[] aboutImgIds = {R.drawable.ic_menu_notifications,
            R.drawable.ic_menu_info_details, R.drawable.ic_menu_share,
            R.drawable.ic_menu_myplaces};

    private ListView lvMineUser;
    private ListView lvMineOrder;
    private ListView lvMineAbout;
    private MineListAdapter userListAdapter;
    private MineListAdapter orderListAdapter;
    private MineListAdapter aboutListAdapter;

    private ProgressDialog progress;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.MINE_FINISH_LOAD_DATA:
                    //ToastUtils.showToast("Handler 收到数据加载完成的消息");
                    orderListAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        initData("已取餐");
        initData("未取餐");
        initView();
    }

    private void initView() {

        lvMineUser = (ListView) findViewById(R.id.lv_mine_user);
        lvMineOrder = (ListView) findViewById(R.id.lv_mine_order);
        lvMineAbout = (ListView) findViewById(R.id.lv_mine_about);

        userListAdapter = new MineListAdapter(this, userItemNames,
                userItemContents, userImgIds);
        orderListAdapter = new MineListAdapter(this, orderItemNames,
                orderItemContents, orderImgIds);
        aboutListAdapter = new MineListAdapter(this, aboutItemNames,
                aboutItemContents, aboutImgIds);

        lvMineUser.setAdapter(userListAdapter);
        lvMineOrder.setAdapter(orderListAdapter);
        lvMineAbout.setAdapter(aboutListAdapter);

        lvMineUser.setOnItemClickListener(this);
        lvMineOrder.setOnItemClickListener(this);
        lvMineAbout.setOnItemClickListener(this);

        lvMineUser.setSelector(R.drawable.selector_default);
        lvMineOrder.setSelector(R.drawable.selector_default);
        lvMineAbout.setSelector(R.drawable.selector_default);

        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(false);

    }

    // 初始化列表菜单中数据
    public void initData(final String type) {
        // 获取用户
        BmobUser user = BmobUser.getCurrentUser(this);
        if (user == null) {
            return;
        }
        userItemNames[0] = user.getUsername();

        // 获取小菜订单(数量)
        BmobQuery<Order> query = new BmobQuery<Order>();
        query.order("-updatedAt");
        query.addWhereEqualTo("userName", user.getUsername());
        query.addWhereEqualTo("state", type);
        query.count(this, Order.class, new CountListener() {

            @Override
            public void onSuccess(int count) {
                if (type.equals("未取餐")) {
                    orderItemContents[0] = "( " + count + " )";
                }
                if (type.equals("已取餐")) {
                    orderItemContents[1] = "( " + count + " )";
                }
                Message msg = new Message();
                msg.what = MessageType.MINE_FINISH_LOAD_DATA;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                ToastUtils.showToast("查询失败");
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        // 个人资料
        if (parent.getId() == R.id.lv_mine_user) {
            switch (position) {
                case 0: // 资料卡
                    // toast("点击个人资料");
                    Intent toMineInfo = new Intent(MineActivity.this,
                            MineInfoActivity.class);
                    startActivity(toMineInfo);
                    break;

                default:
                    break;
            }
        }

        // 小菜订单
        if (parent.getId() == R.id.lv_mine_order) {
            // toast("点击了订单区域");
            Intent toOrderInfo;
            switch (position) {
                //当前订单
                case 0:
                    toOrderInfo = new Intent(MineActivity.this,
                            OrderInfoActivity.class);
                    toOrderInfo.putExtra(OrderInfoActivity.KEY_INTENT_ORDER_TYPE, Order.ORDER_STATE_CODE_IN_PAY);
                    startActivity(toOrderInfo);
                    break;
                //历史订单
                case 1:
                    toOrderInfo = new Intent(MineActivity.this,
                            OrderInfoActivity.class);
                    toOrderInfo.putExtra(OrderInfoActivity.KEY_INTENT_ORDER_TYPE, Order.ORDER_STATE_CODE_FINISHED);
                    startActivity(toOrderInfo);
                    break;
                default:
                    break;
            }
        }

        // 其他
        if (parent.getId() == R.id.lv_mine_about) {

            switch (position) {
                case 1: // 软件相关
                    Intent toMineSoft = new Intent(MineActivity.this,
                            MineSoftActivity.class);
                    startActivity(toMineSoft);
                    break;
                case 2: // 推荐给朋友
                    Intent toShare = new Intent(Intent.ACTION_SEND);
                    toShare.setType("text/plain");
                    toShare.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    toShare.putExtra(Intent.EXTRA_TEXT, "校园小菜-贵工院版" + "\n"
                            + "贵工院专属的校园小菜上线了，赶紧下载体验吧" + "http://xiaocai.bmob.cn");
                    startActivity(Intent.createChooser(toShare, "分享到"));
                    break;
                case 3: // 退出当期账号
                    logout();
                    break;

                default:
                    // toast("点击了通知区域");
                    Intent intent = new Intent(MineActivity.this, StudentGradeActivity.class);
                    startActivity(intent);
                    break;
            }

        }

    }


    /**
     * 返回到登陆页面
     */
    private void goLogin() {
        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 退出登陆－有两种方式，根据当前的登陆方式选择登出操作 1. Bmob账号 2. 学号
     */
    private void logout() {
        int loginMethod = LoginManager.getInstance().curLoginMethod;
        if (loginMethod == LoginManager.LOGIN_M_XIAOCAI) {
            logoutByXiaoCai();
        } else if (loginMethod == LoginManager.LOGIN_M_SCHOOL) {
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
                BmobUser.logOut(BaseApplication.getAppContext());
                goLogin();
                progress.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast("退出登陆失败，请稍后再试");
                progress.dismiss();
            }
        });
    }


    /**
     * 学号退出登陆
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

                goLogin();
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
