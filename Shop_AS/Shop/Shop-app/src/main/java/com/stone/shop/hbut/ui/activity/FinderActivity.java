package com.stone.shop.hbut.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.main.manager.LoginManager;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;

/**
 * 每日一抽页面
 *
 * @author Stone
 * @date 2014-5-18
 */
public class FinderActivity extends BaseActivity implements OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "FinderActivity";
    private static final int RESULT_REQUEST_AUTH_HBUT = 1007;

    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {
        aq.id(R.id.cv_campus_grade).clicked(this);
        aq.id(R.id.cv_campus_schedule).clicked(this);
        aq.id(R.id.cv_campus_bxt_news).clicked(this);
        aq.id(R.id.cv_campus_2nd_trade).clicked(this);
    }



    private void campusGrade() {
        Intent intent = new Intent(FinderActivity.this, StudentGradeActivity.class);
        startActivity(intent);
    }

    private void campusSchedule() {
    }

    private void hbutBXTNews() {
        Intent intent = new Intent(FinderActivity.this, BXTActivity.class);
        startActivity(intent);
    }


    private void campus2ndTrade() {
        Intent intent = new Intent(FinderActivity.this, SecondTradeActivity.class);
        startActivity(intent);
    }

    /**
     * 检测是否绑定学号
     */
    private void showAuthDialog() {
        if(!LoginManager.getInstance().getStuID().equals("")) {
            campusGrade();
            return;
        }
        User curUser = BmobUser.getCurrentUser(this, User.class);
        String msg = "";
        String school = getResources().getString(R.string.app_school_d);
        if(curUser.getStuID().equals("")) {
            msg = "系统检测到当前账号尚未绑定 " + school + " 学生账号，取消绑定将无法使用本软件中的部分功能，请问是否授权绑定？";
        } else {
            msg = "系统检测到当前账号已绑定学号 " + curUser.getStuID() + " , 由于授权信息已失效，取消绑定将无法使用本软件中的部分功能， 请问是否重新授权绑定？";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告").setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showToast("取消绑定");
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 绑定学号
                        Intent toAuthHbut = new Intent(FinderActivity.this, AuthHBUTActivity.class);
                        startActivityForResult(toAuthHbut, RESULT_REQUEST_AUTH_HBUT);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //查成绩
            case R.id.cv_campus_grade:
                showAuthDialog();
                break;

            //查课表
            case R.id.cv_campus_schedule:
                campusSchedule();
                break;

            //博学堂讲座预告
            case R.id.cv_campus_bxt_news:
                hbutBXTNews();
                break;

            //校园二手街
            case R.id.cv_campus_2nd_trade:
                campus2ndTrade();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_REQUEST_AUTH_HBUT && resultCode == RESULT_OK) {
            campusGrade();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FinderActivity"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FinderActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }


}
