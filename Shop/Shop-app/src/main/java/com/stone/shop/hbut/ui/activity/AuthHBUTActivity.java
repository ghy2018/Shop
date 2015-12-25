package com.stone.shop.hbut.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stone on 15/5/5.
 */
public class AuthHBUTActivity extends BaseActivity {

    private static final String TAG = "AuthHBUTActivity";
    private String username;
    private String password;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnAuthHbut;

    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_hbut);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView(){
        etUsername = (EditText) findViewById(R.id.et_hbut_stuid);
        etPassword = (EditText) findViewById(R.id.et_hbut_psd);
        btnAuthHbut = (Button) findViewById(R.id.btn_hbut_auth);

        btnAuthHbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBySchool();
            }
        });

        aq = new AQuery(this);
    }


    private void updateLoginBtnText(String text) {
        if (null != btnAuthHbut) {
            btnAuthHbut.setText(text);
            if (text.equals("重新绑定")) {
                btnAuthHbut.setPressed(false);
            } else {
                btnAuthHbut.setSelected(true);
            }
        } else {
            return;
        }
    }

    private boolean loginCheck(String username, String password) {
        boolean isValid = false;
        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
        } else if (username.equals("") || password.equals("")) {
            ToastUtils.showToast("学号或密码为空");
            updateLoginBtnText("重新绑定");
        } else {
            isValid = true;
        }
        return isValid;
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

                        // 绑定当前用户
                        checkAuthHbut(username);

                    }

                } else {
                    updateLoginBtnText("重新绑定");
                    ToastUtils.showToast("绑定失败，请核对学号密码后重新绑定");
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
                ToastUtils.showToast("验证失败： " + map.get("Message"));
                return false;
            }
        }

        return false;
    }


    private void checkAuthHbut(final String stuID) {
        User user = User.getCurrentUser(this, User.class);
        if(user==null)
            return;

        if(!user.getStuID().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告").setMessage("当前小菜账号已经绑定学号 " + user.getStuID() + "是否重新绑定 ?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showToast("取消绑定");
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            authCurrentUser(stuID);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialogTitleLineColor(dialog);
        } else {
            authCurrentUser(stuID);
        }
    }


    private void authCurrentUser(String stuID) {
        User user = BmobUser.getCurrentUser(this, User.class);
        if(null == user)
            return;
        showProgressDialog();
        user.setStuID(stuID);
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("绑定成功");
                setResult(RESULT_OK, null);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("绑定失败");
            }
        });
    }


}
