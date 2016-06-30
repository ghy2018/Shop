package com.stone.shop.main.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.R;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.util.LocalBroadcasts;
import com.stone.shop.base.util.MD5;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.main.FixLoginService;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.user.model.User;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 登陆界面
 *
 * @author Stone
 * @date 2014-4-24
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActicity";

    private AQuery aq;

    private Button btnLogin;
    private Button btnReg;
    private Button btnResetPsd;
    private TextView tvUsername;
    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbLoginByXiaoCai;
    private CheckBox cbLoginBySchool;

    private String username;
    private String password;

    private Boolean isNotNullOfUsername = false;    //判断用户名输入是否为空
    private Boolean isNotNullOfPassword = false;    ////判断密码输入是否为空

    // 三方登陆支持
    private TextView mUserInfo;
    private ImageView mUserLogo;
    private ImageView mNewLoginButton;

//    Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0) {
//                mUserInfo.setVisibility(android.view.View.VISIBLE);
//                mUserInfo.setText(msg.getData().getString("nickname"));
//            } else if (msg.what == 1) {
//                Bitmap bitmap = (Bitmap) msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
//            } else if (msg.what == MessageType.MSG_LOGIN_BY_SCHOOL_FINISHED) {
//                // 检查登陆结果
//                isLoginSuccess(loginResponse);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络连接异常");
        }

        // Bmob自动更新组件
        BmobUpdateAgent.initAppVersion(this);
        BmobUpdateAgent.setUpdateOnlyWifi(true);
        BmobUpdateAgent.update(this);

        initView();

        autoCompleteLoginInfo();
    }

    @Override
    protected void onRestart() {
        if(!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络连接异常");
        }
        super.onRestart();
    }

    private void initView() {

        aq = new AQuery(this);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReg = (Button) findViewById(R.id.btn_register);
        btnResetPsd = (Button) findViewById(R.id.btn_reset_psd);

        tvUsername = (TextView) findViewById(R.id.tv_username);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        cbLoginByXiaoCai = (CheckBox) findViewById(R.id.cb_login_by_xiaocai);
        cbLoginBySchool = (CheckBox) findViewById(R.id.cb_login_by_school);

        cbLoginByXiaoCai.setChecked(true);
        cbLoginBySchool.setChecked(false);

        etUsername.addTextChangedListener(textWatcher1);
        etPassword.addTextChangedListener(textWatcher2);

        cbLoginByXiaoCai
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            cbLoginByXiaoCai.setChecked(true);
                            cbLoginBySchool.setChecked(false);
                            tvUsername.setText("用户名");
                            etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else {
                            cbLoginByXiaoCai.setChecked(false);
                            cbLoginBySchool.setChecked(true);
                            tvUsername.setText("学号");
                            etUsername
                                    .setInputType(InputType.TYPE_CLASS_NUMBER);
                        }

                        // 自动填充用户名和密码
                        if (cbLoginByXiaoCai.isChecked() && !cbLoginBySchool.isChecked()) {
                            completeLoginInfo(LoginManager.LOGIN_M_XIAOCAI);
                        } else if (!cbLoginByXiaoCai.isChecked() && cbLoginBySchool.isChecked()) {
                            completeLoginInfo(LoginManager.LOGIN_M_SCHOOL);
                        } else {
                            finish();
                        }

                    }
                });

        cbLoginBySchool
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            cbLoginByXiaoCai.setChecked(false);
                            cbLoginBySchool.setChecked(true);
                            tvUsername.setText("学号");
                            etUsername
                                    .setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            cbLoginByXiaoCai.setChecked(true);
                            cbLoginBySchool.setChecked(false);
                            tvUsername.setText("用户名");
                            etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
                        }

                        // 自动填充用户名和密码
                        if (cbLoginByXiaoCai.isChecked() && !cbLoginBySchool.isChecked()) {
                            completeLoginInfo(LoginManager.LOGIN_M_XIAOCAI);
                        } else if (!cbLoginByXiaoCai.isChecked() && cbLoginBySchool.isChecked()) {
                            completeLoginInfo(LoginManager.LOGIN_M_SCHOOL);
                        } else {
                            finish();
                        }
                    }
                });

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        btnResetPsd.setOnClickListener(this);

        mUserInfo = (TextView) findViewById(R.id.user_nickname);
        mUserLogo = (ImageView) findViewById(R.id.user_logo);
        mNewLoginButton = (ImageView) findViewById(R.id.new_login_btn);
        mNewLoginButton.setOnClickListener(this);

    }

    /**
     * 自动填充用户名和密码
     */
    private void autoCompleteLoginInfo() {
        // 自动填充最近使用的账号和密码
        HashMap<String, String> map = LoginManager.getInstance().getLoginInfo();

        if (map == null)
            return;

        if (map.containsKey(LoginManager.SP_KEY_USERNAME) && map.containsKey(LoginManager.SP_KEY_PASSWORD)) {
            String username = map.get(LoginManager.SP_KEY_USERNAME);
            String password = map.get(LoginManager.SP_KEY_PASSWORD);
            String loginMethodStr = map.get(LoginManager.SP_KEY_LOGIN_METHOD);
            etUsername.setText(username);
            etPassword.setText(password);
            if (loginMethodStr.equals(""))
                return;
            int loginMethod = Integer.parseInt(loginMethodStr);
            if (loginMethod == LoginManager.LOGIN_M_XIAOCAI) {
                cbLoginByXiaoCai.setChecked(true);
                cbLoginBySchool.setChecked(false);
            } else if (loginMethod == LoginManager.LOGIN_M_SCHOOL) {
                cbLoginByXiaoCai.setChecked(false);
                cbLoginBySchool.setChecked(true);
            } else {
                //error
            }
        }
    }

    private void completeLoginInfo(int loginMethod) {
        HashMap<String, String> map = LoginManager.getInstance().getUserInfo(loginMethod);
        if (map == null)
            return;

        if (map.containsKey(LoginManager.SP_KEY_USERNAME) && map.containsKey(LoginManager.SP_KEY_PASSWORD)) {
            String username = map.get(LoginManager.SP_KEY_USERNAME);
            String password = map.get(LoginManager.SP_KEY_PASSWORD);
            etUsername.setText(username);
            etPassword.setText(password);
        }
    }

    private void goHome() {

        //加载用户购物车数据
        ShopCartModule.getInstance().refresh();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login() {
        updateLoginBtnText("登录中");
        if(!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
            updateLoginBtnText("重新登陆");
        } else {
            if (cbLoginByXiaoCai.isChecked() && !cbLoginBySchool.isChecked()) {
                // 使用小菜账号登陆
                loginByXiaoCai();
            } else if (!cbLoginByXiaoCai.isChecked()
                    && cbLoginBySchool.isChecked()) {
                // 使用学号登陆
                loginBySchool();
            }
        }
    }

    private boolean loginCheck(String username, String password) {
        boolean isValid = true;
        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
        } else if (username.equals("") || password.equals("")) {
            ToastUtils.showToast("用户名或密码为空");
            updateLoginBtnText("重新登陆");
        } else {
            isValid = true;
        }
        return isValid;
    }


    // 使用小菜账号登陆
    public void loginByXiaoCai() {

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        //登录有效性检测
        if (!loginCheck(username, password))
            return;

        showProgressDialog();
        User bu2 = new User();
        bu2.setUsername(username);
        bu2.setPassword(password);
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {

                dismissProgressDialog();

                // 更新用户状态
                LoginManager.getInstance().updUserState(User.USER_STATE_LOGIN_BY_XIAOCAI);

                // 保存用户登陆方式
                LoginManager.getInstance().saveLoginInfo(username, password, LoginManager.LOGIN_M_XIAOCAI);

                // 跳转到主页
                goHome();

            }

            @Override
            public void onFailure(int code, String msg) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("小菜系统登录失败 [%d: %s]", code, msg));
                updateLoginBtnText("重新登陆");

                // 启动登陆修复程序
                showFixDialog();
            }
        });
    }

    //AQuery登录方式
    public void loginBySchool() {

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (!loginCheck(username, password))
            return;

        showProgressDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("UserName", username);
        map.put("Password", password);
        map.put("Role", "Student");
        String url = HBUTConfig.URL_HBUT_LOGIN;
        aq.ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                dismissProgressDialog();
                if (json != null) {

                    if (isLoginSuccess(json.toString())) {

                        //保存Cookies
                        List<Cookie> list = status.getCookies();
                        ToastUtils.showToast("Cookie Size: " + list.size());
                        CookiesManager.getInstance().saveCookies(list);

                        // 保存用户登陆方式
                        LoginManager.getInstance().saveLoginInfo(username, password, LoginManager.LOGIN_M_SCHOOL);

                        // 判断是否有小菜账号，没有则自动注册一个, 并自动登陆
                        hasXiaoCai(username);
                    }

                } else {
                    updateLoginBtnText("重新登陆");
                    ToastUtils.showToast("教务系统登录失败，请核对用户名密码稍后重试");

                    // 是否启动登陆修复程序
                    showFixDialog();
                }
            }
        });
    }


    // 学号登陆完成以后的UI更新操作
    private boolean isLoginSuccess(String json) {

        dismissProgressDialog();
        HashMap<String, String> map = JSONManager.getLoginResult(json);
        if (null == map)
            return false;
        if (BmobConfig.DEBUG)
            Log.d(TAG, map.toString());
        if (map.containsKey("Status") && map.get("Status").equals("0")) {
            return true;
        } else {
            if (map.containsKey("Message")) {
                ToastUtils.showToast("登录失败： " + map.get("Message"));
                return false;
            }
        }
        return false;
    }

    /**
     * 选择是否修复
     */
    private void showFixDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("校园小菜登陆修复程序").setMessage("因教务系统登陆失败，有可能是接口变动" +
                "或服务器出现问题，是否启动校园小菜自动修复程序?")
                .setNegativeButton("不了，谢谢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("立即启动", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startFixTool();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }

    /**
     * 启动修复程序
     */
    private void startFixTool() {
        showProgressDialog();
        ToastUtils.showToast("自动修复启动中");
        Intent intent = new Intent(LoginActivity.this, FixLoginService.class);
        startService(intent);
    }


    //------------------------------绑定学号-------------------------------------------

    /**
     * 判断是否有小菜账号
     *
     * @param stuID
     */
    private void hasXiaoCai(final String stuID) {
        showProgressDialog();

        //判断是否有小菜账号
        boolean hasXiaoCaiAccount = LoginManager.getInstance().hasXiaoCaiAccount();

        if (BmobConfig.DEBUG)
            Log.d(TAG, "本地是否有小菜账号登陆记录：" + hasXiaoCaiAccount);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("stuID", stuID);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> users) {
                dismissProgressDialog();
                if (users != null && users.size() == 1) {
                    // 绑定当前学号和小菜账号，并登陆小菜账号
                    //updateByStuID(users.get(0), stuID);
                    loginByStuID(stuID);
                } else {
                    //注册一个小菜账号，登陆并绑定该学号
                    registerByStuID(stuID);
                }
            }

            @Override
            public void onError(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast(String.format("查询学号绑定记录失败 [%d: %s]", i, s));
                if(BmobConfig.DEBUG)
                    Log.d(TAG, "查询学号绑定记录失败: " + s);
            }
        });
    }

    private void registerByStuID(final String stuID) {

        if (BmobConfig.DEBUG)
            Log.d(TAG, "注册小菜账号： " + stuID);

        showProgressDialog();
        User user = new User();
        String username = String.format(User.HBUT_USERNAME_FORMAT, stuID);
        String password = String.format(User.HBUT_PASSWORD_FORMAT, stuID);
        user.setUsername(MD5.md5String(username));
        user.setPassword(MD5.md5String(password));
        user.setSchool(getResources().getString(R.string.app_school_d));
        user.setStuID(stuID);
        user.setNickname(stuID);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {

                if (BmobConfig.DEBUG)
                    Log.d(TAG, "注册小菜账号成功： " + stuID);

                dismissProgressDialog();
                loginByStuID(stuID);
            }

            @Override
            public void onFailure(int i, String s) {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "注册小菜账号失败： " + stuID);

                dismissProgressDialog();
                ToastUtils.showToast("学号绑定失败");
            }
        });
    }

    private void updateByStuID(User authUser, final String stuID) {
        if (BmobConfig.DEBUG)
            Log.d(TAG, "小菜账号已存在，将其绑定小菜账号： " + stuID);

        boolean isAuthWithXiaocai = false;
        if (authUser.getStuID().equals(stuID)) {
            isAuthWithXiaocai = true;
            if (BmobConfig.DEBUG)
                Log.d(TAG, String.format("当前学号[%1s]已经绑定了小菜账号[%2s], 直接登陆", authUser.getUsername(), authUser.getStuID()));
        }

        showProgressDialog();
        HashMap<String, String> hashMap = LoginManager.getInstance().getUserInfo(LoginManager.LOGIN_M_XIAOCAI);
        final User user = new User();
        user.setUsername(hashMap.get(LoginManager.SP_KEY_USERNAME));
        user.setPassword(hashMap.get(LoginManager.SP_KEY_PASSWORD));
        user.setStuID(stuID);

        if (isAuthWithXiaocai) {
            loginByUser(authUser);
        } else {
            user.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {
                    if (BmobConfig.DEBUG)
                        Log.d(TAG, "小菜账号绑定学号成功： " + stuID);

                    dismissProgressDialog();
                    ToastUtils.showToast("小菜账号绑定学号成功");

                    loginByUser(user);
                }

                @Override
                public void onFailure(int i, String s) {
                    if (BmobConfig.DEBUG)
                        Log.d(TAG, "小菜账号绑定学号失败： " + stuID);
                    dismissProgressDialog();
                    ToastUtils.showToast("小菜账号绑定学号失败");
                }
            });
        }

    }

    private void loginByUser(final User user) {
        if (user == null)
            return;

        if (BmobConfig.DEBUG)
            Log.d(TAG, "登陆学号所绑定的小菜账号： " + user.getStuID() + " " + user.getUsername());
        showProgressDialog();
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "学号所绑定的小菜账号登陆成功： " + user.getStuID());
                dismissProgressDialog();
                goHome();
            }

            @Override
            public void onFailure(int i, String s) {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "学号所绑定的小菜账号登陆失败： " + user.getStuID());
                dismissProgressDialog();
                ToastUtils.showToast("学号所绑定的小菜账号登陆失败" + s);
            }
        });
    }

    private void loginByStuID(final String stuID) {

        if (BmobConfig.DEBUG)
            Log.d(TAG, "登陆小菜账号： " + stuID);

        showProgressDialog();
        User user = new User();
        String username = String.format(User.HBUT_USERNAME_FORMAT, stuID);
        String password = String.format(User.HBUT_PASSWORD_FORMAT, stuID);
        user.setUsername(MD5.md5String(username));
        user.setPassword(MD5.md5String(password));
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "登陆小菜账号成功： " + stuID);
                dismissProgressDialog();

                //跳转到主页
                goHome();
            }

            @Override
            public void onFailure(int i, String s) {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "登陆小菜账号失败： " + stuID);
                dismissProgressDialog();
                ToastUtils.showToast("学号绑定小菜账户系统失败");
            }
        });
    }

    //------------------------------绑定学号-------------------------------------------

    //登录失败以后更新Button的文字
    public void updateLoginBtnText(String text) {
        if (null != btnLogin) {
            btnLogin.setText(text);
            if (text.equals("重新登陆")) {
                btnLogin.setPressed(false);
            } else {
                btnLogin.setSelected(true);
            }
        } else {
            return;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登陆
            case R.id.btn_login:
                login();
                break;

            //密码重置
            case R.id.btn_reset_psd:
                Intent toResetPsdActivity = new Intent(LoginActivity.this, ResetPsdActivity.class);
                startActivity(toResetPsdActivity);
                break;

            //注册新用户
            case R.id.btn_register:
                Intent toReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toReg);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcasts.registerLocalReceiver(fixReceiver, FixLoginService.ACTION_FIX_LOGIN_RESULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcasts.unregisterLocalReceiver(fixReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


    private BroadcastReceiver fixReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismissProgressDialog();
            if(intent.getAction().equals(FixLoginService.ACTION_FIX_LOGIN_RESULT)) {
                boolean isSuccess = intent.getBooleanExtra(FixLoginService.KEY_EXTRA_FIX_LOGIN_RESULT, false);
                ToastUtils.showToast("修复结果验证中");
                if(BmobConfig.DEBUG)
                    Log.d(TAG, isSuccess? "登陆成功" : "登陆失败" );
                if(isSuccess)
                    goHome();
                else
                    ToastUtils.showToast("校园小菜系统登陆失败");
            }

        }
    };


    // 文本输入监听
    private TextWatcher textWatcher1 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                isNotNullOfUsername = true;
                //Log.d(TAG, "用户名长度"+s.length());
            } else {
                isNotNullOfUsername = false;
            }

            if (isNotNullOfUsername && isNotNullOfPassword) {
                btnLogin.setPressed(false);
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
            } else {
                btnLogin.setPressed(true);
                //禁用登陆按钮
                btnLogin.setEnabled(false);
                btnLogin.setClickable(false);
            }
        }
    };

    private TextWatcher textWatcher2 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                isNotNullOfPassword = true;
                //Log.d(TAG, "密码长度"+s.length());
            } else {
                isNotNullOfPassword = false;
            }

            if (isNotNullOfUsername && isNotNullOfPassword) {
                btnLogin.setPressed(false);
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
            } else {
                btnLogin.setPressed(true);
                //禁用登陆按钮
                btnLogin.setEnabled(false);
                btnLogin.setClickable(false);
            }
        }
    };

}
