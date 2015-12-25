package com.stone.shop.user.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.user.ui.adapter.MineSoftAdapter;
import com.stone.shop.base.widget.TitleBarView;
import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.AppVersion;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 软件相关
 *
 * @author Stone
 * @date 2014-5-21
 */
public class MineSoftActivity extends BaseActivity implements OnItemClickListener {

    private String[] softItemNames = {"意见反馈", "检查更新", "使用协议", "关于我们"};
    private String[] softItemContents = {"", "", "", ""};
    private ListView lvMineSoft;

    private TitleBarView titleBar;

    private MineSoftAdapter softListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft);


        //initTitleBar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        //检查软件更新
        checkSoftUpdate();

    }

    private void initTitleBar() {
        titleBar = (TitleBarView) findViewById(R.id.titlebar);
        titleBar.getLeftBtn().setVisibility(View.VISIBLE);
        titleBar.getLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        lvMineSoft = (ListView) findViewById(R.id.lv_mine_soft);
        softListAdapter = new MineSoftAdapter(this, softItemNames,
                softItemContents);
        lvMineSoft.setAdapter(softListAdapter);
        lvMineSoft.setOnItemClickListener(this);
        lvMineSoft.setSelector(R.drawable.selector_default);
    }

    private void checkSoftUpdate() {
        BmobQuery<AppVersion> query = new BmobQuery<AppVersion>();
        query.findObjects(this, new FindListener<AppVersion>() {
            @Override
            public void onSuccess(List<AppVersion> list) {
                if (getAppVersionCode() > 0) {
                    Iterator<AppVersion> iterator = list.iterator();
                    AppVersion appVersion;
                    while (iterator.hasNext()) {
                        appVersion = iterator.next();
                        //判断软件是否有版本更新
                        if (appVersion.getVersion_i() > getAppVersionCode()) {
                            softItemContents[1] = "new";
                            break;
                        }
                    }
                    softListAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(int code, String msg) {
                toast("检查软件更新失败：" + msg);
            }
        });
    }

    //获取当前应用的版本号
    private int getAppVersionCode() {
        int versionCode = -1;
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);

            // 当前应用的版本名称
            //versionName = info.versionName;

            // 当前版本的版本号
            if (info != null) {
                versionCode = info.versionCode;
            }

            // 当前版本的包名
            //String packageNames = info.packageName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return versionCode;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (position) {
            case 0:
                Intent toFeedBack = new Intent(MineSoftActivity.this,
                        FeedBackActivity.class);
                startActivity(toFeedBack);
                break;
            case 1:
                BmobUpdateAgent.forceUpdate(this);
                break;
            case 2:
                break;
            case 3:
                Intent toAboutSoft = new Intent(MineSoftActivity.this,
                        AboutActivity.class);
                startActivity(toAboutSoft);
                break;

            default:
                break;
        }

    }

    public void clickSoftBack(View v) {
        finish();
    }

    @SuppressWarnings("unused")
    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineSoftActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineSoftActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
    }
}
