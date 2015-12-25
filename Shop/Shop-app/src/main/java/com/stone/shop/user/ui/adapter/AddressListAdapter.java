package com.stone.shop.user.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.user.ui.activity.AddressActivity;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.user.model.Address;
import com.stone.shop.user.model.User;
import com.stone.shop.base.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by stone on 15/5/6.
 */
public class AddressListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{


    private Context mContext;
    private List<Address> mAddressList;
    private LayoutInflater mInflater = null;
    private AddressActivity.OnSelectedListener listener;

    private User curUser;
    private Address lastSelected ;
    private Address curSelected ;

    private boolean isSelectedType = false;

    public AddressListAdapter(Context context, List<Address> addressList) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mAddressList = addressList;
        curUser = BmobUser.getCurrentUser(context, User.class);
    }

    public void setOnSelectedListener(AddressActivity.OnSelectedListener listener) {
        this.listener = listener;
    }

    public void setIsSelectedType(boolean isSelectedType) {
        this.isSelectedType = isSelectedType;
    }

    // 刷新列表中的数据
    public void refresh(List<Address> list) {
        Log.i("BXTNewsAdapter", "Adapter刷新数据");
        mAddressList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAddressList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_address, null);
            viewHolder = new AddressViewHolder();
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_item_address_name);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_item_address);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_item_address_phone);
            viewHolder.tvUpdateTime = (TextView) convertView.findViewById(R.id.tv_item_address_time);
            viewHolder.cbSelected = (CheckBox) convertView.findViewById(R.id.cb_address_selected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AddressViewHolder) convertView.getTag();
        }

        if(!curUser.getNickname().equals(""))
            viewHolder.tvUserName.setText(curUser.getNickname());
        else
            viewHolder.tvUserName.setText(curUser.getUsername());
        viewHolder.tvAddress.setText(mAddressList.get(position).getAddress());
        viewHolder.tvPhone.setText(curUser.getPhone());
        viewHolder.tvUpdateTime.setText(mAddressList.get(position).getUpdatedAt());

        if(isSelectedType) {
            viewHolder.cbSelected.setVisibility(View.VISIBLE);
            viewHolder.cbSelected.setChecked(false);
            viewHolder.cbSelected.setTag(mAddressList.get(position));
            viewHolder.cbSelected.setOnCheckedChangeListener(this);
        } else {
            viewHolder.cbSelected.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(BmobConfig.DEBUG) {
            ToastUtils.showToast("CheckBox 状态改变");
        }
        buttonView.setChecked(isChecked);
        if(listener!=null)
            listener.onSelected((Address) buttonView.getTag());
    }

    public class AddressViewHolder {

        // 联系人
        public TextView tvUserName;

        // 地址
        public TextView tvAddress;

        // 联系电话
        public TextView tvPhone;

        // 修改时间
        public TextView tvUpdateTime;

        // 选择按钮
        public CheckBox cbSelected;

    }
}
