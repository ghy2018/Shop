package com.stone.shop.hbut;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.hbut.model.Semester;
import com.stone.shop.base.util.ToastUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by stone on 15/4/10.
 */
public class GetSemesterService extends IntentService {

    private static final String TAG = "GetSemesterService";

    public GetSemesterService() {
        super(TAG);
    }

    public GetSemesterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getCurrentSemester();
    }

    public void getCurrentSemester() {

        Log.d(TAG, "启动获取当前学期Service");


        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals(""))
            return;
        String url = HBUTConfig.urlCurSemester();
        AjaxCallback<String> callback = new AjaxCallback<String>();
        callback.url(url).type(String.class).weakHandler(ShopApplication.getAppContext(), "callback");
        Map<String, ?> cookiesMap = CookiesManager.getInstance().getCookies();
        Iterator<String> iterator = cookiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String cookieName = iterator.next();
            String cookieValue = (String) cookiesMap.get(cookieName);
            callback.cookie(cookieName, cookieValue);
        }
        ShopApplication.getAQuery().ajax(callback);
    }

    public void callback(String url, String string, AjaxStatus status) {
        Log.d(TAG, "CallBack: " + string);
        if (string != null) {
            Log.d(TAG, "当前学期获取成功");
            Semester s = JSONManager.getCurrentSemester(string);
            HBUTManager.getInstance().setCurSemester(s);
        } else {
            Log.d(TAG, "当前学期获取失败");
            ToastUtils.showToast("当前学期获取失败");
        }
    }


}
