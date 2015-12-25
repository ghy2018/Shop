package com.stone.shop.hbut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.hbut.model.Lover;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表适配器
 *
 * @author Stone
 * @date 2014-5-3
 */
public class LoverWallListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<Lover> mLoversList = null; // 所选分类下的所有店铺列表

    public LoverWallListAdapter(Context context, List<Lover> loversList) {
        mContext = context;
        mLoversList = loversList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;    //测试
    }

    @Override
    public Object getItem(int position) {
        return mLoversList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<Lover> list) {
        mLoversList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LoverWallHolder loverWallHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_lover_wall, null);
            loverWallHolder = new LoverWallHolder();

            //人名
            loverWallHolder.tvBoyName = (TextView) convertView
                    .findViewById(R.id.tv_boy_name);
            loverWallHolder.tvGirlName = (TextView) convertView
                    .findViewById(R.id.tv_girl_name);

            //蜜语
            loverWallHolder.etLoverWords = (EditText) convertView
                    .findViewById(R.id.et_lover_words);

            //计数器
            loverWallHolder.tvCountZYQ = (TextView) convertView
                    .findViewById(R.id.tv_count_zyq);
            loverWallHolder.tvCountWQJ = (TextView) convertView
                    .findViewById(R.id.tv_count_wqj);
            loverWallHolder.tvCountDBC = (TextView) convertView
                    .findViewById(R.id.tv_count_dbc);

            //按钮
            loverWallHolder.btnZYQ = (Button) convertView
                    .findViewById(R.id.btn_zyq);
            loverWallHolder.btnWQJ = (Button) convertView
                    .findViewById(R.id.btn_wqj);
            loverWallHolder.btnDBC = (Button) convertView
                    .findViewById(R.id.btn_dbc);
            convertView.setTag(loverWallHolder);
        } else {
            loverWallHolder = (LoverWallHolder) convertView.getTag();
        }
        return convertView;
    }


    public class LoverWallHolder {

        public TextView tvBoyName;        //高富帅
        public TextView tvGirlName;        //白富美
        public EditText etLoverWords;    //蜜语

        //计数器
        public TextView tvCountZYQ;
        public TextView tvCountWQJ;
        public TextView tvCountDBC;

        public Button btnZYQ;  //在一起
        public Button btnWQJ;    //挖墙脚
        public Button btnDBC;    //丢白菜

    }

}
