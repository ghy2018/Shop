package com.stone.shop.user.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.user.model.Address;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.SIMCardInfo;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 支持输入一行
 * <p/>
 * Created by stone on 15/5/6.
 */
public class InputRowActivity extends BaseActivity {

    public static final String TAG = "InputRowActivity";

    public static final String KEY_INPUT_TYPE = "com.stone.shop.KEY_INPUT_TYPE";
    public static final String KEY_RESULT_DATA = "com.stone.shop.KEY_RESULT_DATA";

    public static final int TYPE_INPUT_ERROR = 0;
    public static final int TYPE_INPUT_ADDRESS = 1;
    public static final int TYPE_INPUT_DOR_PART = 2;
    public static final int TYPE_INPUT_DOR_NUM = 3;
    public static final int TYPE_INPUT_PHONE = 4;
    public static final int TYPE_INPUT_QQ = 5;


    private EditText etInputRow;

    private int curType;
    private String title;
    private String hintText;
    private String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_row);

        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题.
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        initView();
    }


    private void init() {
        curType = getIntent().getIntExtra(KEY_INPUT_TYPE, TYPE_INPUT_ERROR);
        switch (curType) {
            case TYPE_INPUT_ADDRESS:
                title = "添加收货地址";
                hintText = "请输入收货地址";
                break;

            case TYPE_INPUT_DOR_PART:
                title = "填写寝室楼号";
                hintText = "例如：西区19栋、草莓园3栋";
                break;

            case TYPE_INPUT_DOR_NUM:
                title = "填写寝室号";
                hintText = "例如：418";
                break;

            case TYPE_INPUT_PHONE:
                title = "绑定手机号";
                hintText = "请输入手机号";
                SIMCardInfo simCardInfo = new SIMCardInfo(this);
                text = simCardInfo.getNativePhoneNumber();
                break;

            case TYPE_INPUT_QQ:
                title = "绑定QQ";
                hintText = "请输入QQ";
                break;

            case TYPE_INPUT_ERROR:
            default:
                title = "错误参数";
                hintText = title;
                break;
        }
    }


    private void initView() {

        getSupportActionBar().setTitle(title);

        etInputRow = (EditText) findViewById(R.id.et_input_row);
        etInputRow.setHint(hintText);
        etInputRow.setText(text);
    }


    /**
     * 提交
     */
    private void submit() {

        if(!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接稍后重试");
            return;
        }


        switch (curType) {

            case TYPE_INPUT_ADDRESS:
                submitAddress();
                break;

            case TYPE_INPUT_DOR_PART:
            case TYPE_INPUT_DOR_NUM:
            case TYPE_INPUT_PHONE:
            case TYPE_INPUT_QQ:
                setResult();
                break;

            case TYPE_INPUT_ERROR:
            default:
                break;
        }

    }

    /**
     * 提交收货地址
     */
    private void submitAddress() {

        User user = BmobUser.getCurrentUser(this, User.class);
        if(user == null)
            return;

        final String str = etInputRow.getText().toString();
        if(str.equals("")) {
            ToastUtils.showToast("请填写有效真实的收货地址后再次提交");
            return;
        }

        showProgressDialog();
        final Address address = new Address();
        address.setAddress(str);
        address.setUser(user);
        address.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("提交成功");

                Intent intent = new Intent();
                intent.putExtra(KEY_RESULT_DATA, address);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("提交失败，" + s);
            }
        });
    }

    /**
     * 返回用户填写的信息
     */
    private void setResult() {
        String result = etInputRow.getText().toString();
        if(result.equals("")) {
            ToastUtils.showToast("请先完善信息，稍后重试");
            return;
        }

        if(curType == TYPE_INPUT_DOR_NUM) {
            if(!Utils.isNumeric(result)) {
                ToastUtils.showToast("请输入正确的寝室号，仅包含数字");
                return;
            }
        }

        if(curType == TYPE_INPUT_PHONE) {
            if(!Utils.isPhoneNumberValid(result)) {
                ToastUtils.showToast("请输入正确的手机号");
                return;
            }
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_DATA, result);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mine_info_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                submit();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
