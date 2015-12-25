package com.stone.shop.user.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.user.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * MineInfoActivity 项目列表适配器
 *
 * @author Stone
 * @date 2014-5-10
 */
public class MineInfoListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<InfoItem> list = new ArrayList<>();

    public MineInfoListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user == null)
            return;
        list.add(new InfoItem("用户名", user.getUsername()));

        list.add(   new InfoItem("#个人信息", "")   );
        list.add(   new InfoItem("学号", user.getStuID())   );
        list.add(   new InfoItem("学校", user.getSchool()));
        list.add(   new InfoItem("学院", user.getCademy())   );
        list.add(   new InfoItem("校区", user.getDorPart())   );
        list.add(   new InfoItem("寝室号", user.getDorNum())   );

        list.add(   new InfoItem("#联系方式", "")   );
        list.add(   new InfoItem("电话", user.getPhone())   );
        list.add(   new InfoItem("QQ", user.getQQ())   );

        list.add(   new InfoItem("#收货地址", "") );

        List<String> places = user.getAddress();

        if(places==null || places.size()==0)
            return;
        Iterator<String> iterator = places.iterator();
        while (iterator.hasNext()) {
            String place = iterator.next();
            list.add(   new InfoItem(place, "") );
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MineInfoHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_mine_info, null);
            holder = new MineInfoHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_mine_info_icon);
            holder.tvKey = (TextView) convertView.findViewById(R.id.tv_mine_info_key);
            holder.tvValue = (TextView) convertView.findViewById(R.id.tv_mine_info_value);
            convertView.setTag(holder);
        } else {
            holder = (MineInfoHolder) convertView.getTag();
        }
        String key = list.get(position).key;
        holder.tvKey.setText(list.get(position).key);
        holder.tvValue.setText(list.get(position).value);
        holder.infoItem = list.get(position);
        if(key.startsWith("#")) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_white));
            holder.tvKey.setTextColor(mContext.getResources().getColor(R.color.bg_color_first));
            holder.tvKey.setText(key.replace("#", ""));
            holder.tvKey.setTextSize(12);
            holder.tvKey.setMaxHeight(100);
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_color_second));
            holder.tvKey.setTextColor(mContext.getResources().getColor(R.color.text_color_normal));
            holder.tvKey.setTextSize(16);
        }
        return convertView;
    }



    public class InfoItem {
        public String key = "";
        public String value = "";

        public InfoItem(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }


    public class MineInfoHolder {

        public ImageView imgIcon;
        public TextView tvKey;
        public TextView tvValue;

        public InfoItem infoItem;

    }




}
