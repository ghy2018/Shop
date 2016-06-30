package com.stone.shop.hbut.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.hbut.config.HBUTConfig;
import com.stone.shop.hbut.ui.fragment.StudentGradeFragment;
import com.stone.shop.main.manager.CookiesManager;
import com.stone.shop.hbut.HBUTManager;
import com.stone.shop.main.manager.JSONManager;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.hbut.model.Semester;
import com.stone.shop.hbut.model.StudentGrade;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.base.widget.TitleBarView;

import java.util.Iterator;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class StudentGradeActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "StudentGradeActivity";

    private TitleBarView titleBar;

    private ViewGroup vgHints;
    private Button btnHideHints;
    private StudentGradeFragment fgStuGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbut_arrage_task);

        //initTitleBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        initData();
    }

    private void initTitleBar() {
        titleBar = (TitleBarView) findViewById(R.id.titlebar);
        titleBar.getLeftBtn().setVisibility(View.VISIBLE);
        titleBar.getLeftBtn().setText(getResources().getString(R.string.text_btn_back));
        titleBar.getLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        vgHints = (ViewGroup) findViewById(R.id.ll_stu_grade_hints);
        vgHints.setVisibility(View.GONE);

        btnHideHints = (Button) findViewById(R.id.btn_hide_hints);
        btnHideHints.setOnClickListener(this);
    }

    private void initFragment() {
        if (null == fgStuGrade)
            fgStuGrade = new StudentGradeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fg_stu_grade, fgStuGrade);
        ft.commit();
    }

    private void initData() {
        if (HBUTManager.getInstance().getStudentGrade() != null) {
            initFragment();
        } else {
            getCurSemester();
        }
    }

    /**
     * 获取当前学期
     */
    public void getCurSemester() {

        if (HBUTManager.getInstance().getCurSemester() != null)
            return;

        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals("")) {  //本地没有当前账号的学号绑定信息
            ToastUtils.showToast("未检测到学号登陆记录");
            return;
        }

        String url = HBUTConfig.urlCurSemester();
        AjaxCallback<String> callback = new AjaxCallback<String>();
        callback.url(url).type(String.class).weakHandler(this, "semesterCallBack");
        Map<String, ?> cookiesMap = CookiesManager.getInstance().getCookies();
        Iterator<String> iterator = cookiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String cookieName = iterator.next();
            String cookieValue = (String) cookiesMap.get(cookieName);
            callback.cookie(cookieName, cookieValue);
        }
        ShopApplication.getAQuery().ajax(callback);

    }

    public void semesterCallBack(String url, String string, AjaxStatus status) {
        Log.d(TAG, "semesterCallBack: " + string);
        if (string != null) {
            Log.d(TAG, "获取当前学期成功");
            Semester s = JSONManager.getCurrentSemester(string);
            HBUTManager.getInstance().setCurSemester(s);

            //获取成绩
            getStuAllGrade(false);
        } else {
            Log.d(TAG, "获取当前学期失败");
            ToastUtils.showToast("获取当前学期失败");
        }
    }


    /**
     * 获取学生所有成绩
     */
    public void getStuAllGrade(boolean isForcePullDataFromInternet) {

        if (!isForcePullDataFromInternet && HBUTManager.getInstance().getStudentGrade() != null)
            return;

        Log.d(TAG, "开始从服务器获取成绩");


        showProgressDialog();
        String stuID = LoginManager.getInstance().getStuID();
        if (stuID.equals("")) {
            ToastUtils.showToast("未检测到学号登陆记录");
            Intent toAuthHbut = new Intent(StudentGradeActivity.this, AuthHBUTActivity.class);
            startActivity(toAuthHbut);
            finish();
            return;
        }

        String url = HBUTConfig.urlStuAllGrade(stuID);
        AjaxCallback<String> callback = new AjaxCallback<String>();
        callback.url(url).type(String.class).weakHandler(this, "gradeCallBack");
        Map<String, ?> cookiesMap = CookiesManager.getInstance().getCookies();
        Iterator<String> iterator = cookiesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String cookieName = iterator.next();
            String cookieValue = (String) cookiesMap.get(cookieName);
            callback.cookie(cookieName, cookieValue);
        }
        ShopApplication.getAQuery().ajax(callback);
    }

    public void gradeCallBack(String url, String string, AjaxStatus status) {
        Log.d(TAG, "gradeCallBack: " + string);

        dismissProgressDialog();
        if (string != null) {
            Log.d(TAG, "学生成绩获取成功");
            StudentGrade sg = JSONManager.getStudentGrade(string);
            // 本地数据缓存
            HBUTManager.getInstance().setStudentGrade(sg);
            // 截取用户信息
            updateUserInfo();
            // 显示成绩列表
            showGradeChart();
        } else {
            Log.d(TAG, "学生成绩获取失败");
            ToastUtils.showToast("获取学生成绩失败");
        }
    }

    private void showGradeChart() {
        if (fgStuGrade != null && fgStuGrade.isAdded()) {
            fgStuGrade.refreshData();
        } else {
            initFragment();
        }
    }

    /**
     * 更新用户资料
     */
    private void updateUserInfo() {
        StudentGrade sg = HBUTManager.getInstance().getStudentGrade();
        if (sg == null)
            return;
        String[] strs = sg.getTitle().split(" ");
        String classname = "";
        String nickname = "";
        String stuID = "";
        if (strs.length == 4) {
            classname = strs[0];

            int length = strs[1].length();
            nickname = strs[1].substring(0, length - 12);
            stuID = strs[1].substring(length - 12 + 1, length - 1);
        }

        User user = BmobUser.getCurrentUser(this, User.class);
        user.setNickname(nickname);
        user.setClassName(classname);
        if (user.getStuID().equals("") || !user.getStuID().equals(stuID))
            user.setStuID(stuID);
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "学生信息更新成功");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast("学生信息更新失败");
            }
        });
    }


    /**
     * 显示提示信息
     */
    private void showHints() {
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 0.8f);
        alphaAnimation.setDuration(2000);
        animationSet.addAnimation(alphaAnimation);

        vgHints.setVisibility(View.VISIBLE);
        vgHints.startAnimation(animationSet);
    }

    /**
     * 隐藏提示信息
     */
    private void hideHints() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        animationSet.addAnimation(scaleAnimation);
        vgHints.startAnimation(animationSet);
        vgHints.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hbut_stu_grade, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync_data) {
            getStuAllGrade(true);
            return true;
        }

        if (id == R.id.action_sync_date_help) {
            showHints();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_hide_hints) {
            hideHints();
        }
    }
}
