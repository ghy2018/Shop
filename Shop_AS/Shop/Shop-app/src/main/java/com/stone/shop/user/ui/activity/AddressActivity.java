package com.stone.shop.user.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.user.ui.adapter.AddressListAdapter;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.user.model.Address;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.Utils;
import com.stone.shop.base.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * 收货地址管理
 * <p/>
 * Created by stone on 15/5/6.
 */
public class AddressActivity extends BaseActivity implements AdapterView.OnItemLongClickListener{

    private static final String TAG = "AddressActivity";
    private static final int REQUEST_CODE_SELECTED = 100;

    public static final String KEY_TYPE_SELECTED_ADDRESS = "com.stone.shop.KEY_TYPE_SELECTED_ADDRESS";

    // 显示收货地址列表
    public static final int TYPE_LOAD_ADDRESS = 0;

    // 结算订单时选择收货地址
    public static final int TYPE_SELECT_ADDRESS = 1;

    public static final String KEY_EXTRA_SELECTED_ADDRESS = "com.stone.shop.KEY_EXTRA_SELECTED_ADDRESS";

    private Button btnAdd;

    private ViewGroup vgEmpty;
    private ListView lvAddress;
    private AddressListAdapter mAddressAdapter;
    private List<Address> mAddressList = new ArrayList<>();

    private int type = TYPE_LOAD_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        initData();
    }

    private void initView() {

        if(getIntent().getExtras()!=null) {
            type = getIntent().getIntExtra(KEY_TYPE_SELECTED_ADDRESS, TYPE_LOAD_ADDRESS);
            initActionBar();
        }

        btnAdd = (Button) findViewById(R.id.btn_address_m_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        vgEmpty = (ViewGroup) findViewById(R.id.rl_address_manager_empty);
        lvAddress = (ListView) findViewById(R.id.lv_address);
        mAddressAdapter = new AddressListAdapter(this, mAddressList);
        mAddressAdapter.setIsSelectedType(type==TYPE_SELECT_ADDRESS);
        mAddressAdapter.setOnSelectedListener(listener);
        lvAddress.setAdapter(mAddressAdapter);
        lvAddress.setOnItemLongClickListener(this);
        showEmptyView(false);
    }

    private void initActionBar() {
        if(type == TYPE_LOAD_ADDRESS)
            getSupportActionBar().setTitle(R.string.title_activity_address_manager);
        else if(type == TYPE_SELECT_ADDRESS)
            getSupportActionBar().setTitle(R.string.title_activity_address_select);
    }

    private void initData() {

        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接稍后重试");
            return;
        }

        User user = BmobUser.getCurrentUser(this, User.class);
        if (user == null) {
            ToastUtils.showToast("尚未登陆小菜账号");
            return;
        }

        showProgressDialog();
        BmobQuery<Address> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user.getObjectId());
        query.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> addressList) {

                dismissProgressDialog();

                if (mAddressAdapter != null)
                    mAddressAdapter.refresh(addressList);

                if (addressList.size() == 0)
                    showEmptyView(true);
                else
                    showEmptyView(false);
            }

            @Override
            public void onError(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("查询收货地址失败： " + s);
            }
        });

    }

    private void showEmptyView(boolean isShow) {
        if (isShow) {
            lvAddress.setVisibility(View.GONE);
            vgEmpty.setVisibility(View.VISIBLE);
        } else {
            lvAddress.setVisibility(View.VISIBLE);
            vgEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * 添加收货地址
     */
    private void add() {
        Intent toInputRow = new Intent(AddressActivity.this, InputRowActivity.class);
        toInputRow.putExtra(InputRowActivity.KEY_INPUT_TYPE, InputRowActivity.TYPE_INPUT_ADDRESS);
        startActivityForResult(toInputRow, 100);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_address_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_add:
                add();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            initData();
        }
    }


    private OnSelectedListener listener = new OnSelectedListener() {
        @Override
        public void onSelected(Address address) {
            if(BmobConfig.DEBUG) {
                ToastUtils.showToast("接受到选择事件");
            }
            Intent intent = new Intent();
            intent.putExtra(KEY_EXTRA_SELECTED_ADDRESS, address.getAddress());
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    /**
     * 地址选择回调事件
     */
    public interface OnSelectedListener {
        public void onSelected(Address address);
    }

}
