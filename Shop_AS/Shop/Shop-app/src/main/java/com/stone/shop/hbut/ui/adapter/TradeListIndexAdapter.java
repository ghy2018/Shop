package com.stone.shop.hbut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stone.shop.R;

import java.util.List;

/**
 * Created by stone on 15/6/18.
 */
public class TradeListIndexAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<String> mIndexList = null;

    public TradeListIndexAdapter(Context context, List<String> list) {
        mContext = context;
        mIndexList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mIndexList.size();
    }

    @Override
    public Object getItem(int position) {
        return mIndexList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TradeIndexHolder tradeIndexHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_second_trade_index, null);
            tradeIndexHolder = new TradeIndexHolder();
            tradeIndexHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_second_trade_index);
            convertView.setTag(tradeIndexHolder);
        } else {
            tradeIndexHolder = (TradeIndexHolder) convertView.getTag();
        }
        tradeIndexHolder.tvIndex.setText(mIndexList.get(position));
        return convertView;
    }


    public class TradeIndexHolder {
        public TextView tvIndex;
    }

}
