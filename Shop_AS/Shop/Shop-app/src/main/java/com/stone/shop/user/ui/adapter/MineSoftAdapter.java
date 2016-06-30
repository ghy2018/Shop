package com.stone.shop.user.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stone.shop.R;

/**
 * MineActivity 项目列表适配器
 *
 * @author Stone
 * @date 2014-5-10
 */
public class MineSoftAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    private String[] mItemNames;        // 项目列表名称
    private String[] mItemContents;        //项目列表的备注值
    private LayoutInflater mInflater = null;

    public MineSoftAdapter(Context context, String[] names, String[] contents) {
        mContext = context;
        mItemNames = names;
        mItemContents = contents;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItemNames.length;
    }

    @Override
    public Object getItem(int position) {
        return mItemNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MineSoftHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_mine_soft, null);
            holder = new MineSoftHolder();
            holder.tvItemName = (TextView) convertView
                    .findViewById(R.id.tv_item_name);
            holder.tvItemContent = (TextView) convertView.findViewById(R.id.tv_item_content);
            convertView.setTag(holder);
        } else {
            holder = (MineSoftHolder) convertView.getTag();
        }
        holder.tvItemName.setText(mItemNames[position]);
        if (!mItemContents[position].equals("")) {
            holder.tvItemContent.setText(mItemContents[position]);
            holder.tvItemContent.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemContent.setVisibility(View.GONE);
        }

        return convertView;
    }


    public class MineSoftHolder {

        public TextView tvItemName;        //项目名称
        public TextView tvItemContent;    //项目值

    }
}
