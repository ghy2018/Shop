package com.stone.shop.hbut;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.hbut.model.StudentSchedule;
import com.stone.shop.base.util.ToastUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by stone on 15/4/10.
 */
public class GetStuSchduleService extends Service {

    private static final String TAG = "GetStuSchduleService";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "启动获取学生当前课表Service");
        getStuCurSchdule();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getStuCurSchdule() {

        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals(""))
            return;
        String url = HBUTConfig.urlStuCurSchdule(stuID);
        AjaxCallback<String> callback = new AjaxCallback<String>();
        callback.url(url).type(String.class).weakHandler(this, "callback");
        Map<String, ?> cookiesMap = CookiesManager.getInstance().getCookies();
        Iterator<String> iterator = cookiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String cookieName = iterator.next();
            String cookieValue = (String) cookiesMap.get(cookieName);
            callback.cookie(cookieName, cookieValue);
        }
        ShopApplication.getAQuery().ajax(callback);
    }

    private void callback(String url, String string, AjaxStatus status) {
        if (string != null) {
            Log.d(TAG, "学生课表获取成功");
            StudentSchedule ss = JSONManager.getStudentSchedule(string);
            HBUTManager.getInstance().setStudentSchedule(ss);
        } else {
            ToastUtils.showToast("获取学生课表失败");
        }
    }
}
