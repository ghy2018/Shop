package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.shop.model.SComment;

import java.util.List;

/**
 * 店铺评论列表适配器
 *
 * @author Stone
 * @date 2014-5-3
 */
public class SCommentAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<SComment> mSComList = null; // 所选分类下的所有店铺列表

    public SCommentAdapter(Context context, List<SComment> list) {
        mContext = context;
        mSComList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSComList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSComList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SComHolder scomHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_scom, null);
            scomHolder = new SComHolder();
            scomHolder.tvComUser = (TextView) convertView
                    .findViewById(R.id.tv_commit_user);
            scomHolder.tvComContent = (TextView) convertView
                    .findViewById(R.id.tv_commit_content);
            convertView.setTag(scomHolder);
        } else {
            scomHolder = (SComHolder) convertView.getTag();
        }
        scomHolder.tvComUser.setText(mSComList.get(position).getUserName());
        scomHolder.tvComContent.setText(mSComList.get(position).getContent());
        return convertView;
    }


    public class SComHolder {

        public TextView tvComUser;
        public TextView tvComContent;

    }


}
