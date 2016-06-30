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
import com.stone.shop.hbut.model.StudentGrade;
import com.stone.shop.base.util.ToastUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by stone on 15/4/10.
 */
public class GetStuGradeService extends IntentService {

    private static final String TAG = "GetStuGradeService";

    public GetStuGradeService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getStuAllGrade();
    }

    public void getStuAllGrade() {

        Log.d(TAG, "启动获取成绩Service");

        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals(""))
            return;
        String url = HBUTConfig.urlStuAllGrade(stuID);
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
            Log.d(TAG, "学生成绩获取成功");
            StudentGrade sg = JSONManager.getStudentGrade(string);
            HBUTManager.getInstance().setStudentGrade(sg);
        } else {
            Log.d(TAG, "学生成绩获取失败");
            ToastUtils.showToast("获取学生成绩失败");
        }
    }


}
