package com.stone.shop.hbut.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.stone.shop.R;
import com.stone.shop.base.application.Options;
import com.stone.shop.base.application.ShopApplication;
import com.stone.shop.hbut.model.SecondTrade;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器--适配二手交易物品列表数据
 *
 * @author Stone
 * @date 2014-9-15
 */
public class TradeItemListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<SecondTrade> mTradeItemList = null; // 物品列表

    //图片现实设置
    DisplayImageOptions options;

    public TradeItemListAdapter(Context context,
                                List<SecondTrade> tradeItemList) {
        mContext = context;
        mTradeItemList = tradeItemList;
        options = Options.getListOptions();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mTradeItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTradeItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<SecondTrade> list) {
        mTradeItemList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TradeItemHolder tradeItemHodler;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_second_trade_all, null);
            tradeItemHodler = new TradeItemHolder();
            tradeItemHodler.imgTradeItem = (ImageView) convertView
                    .findViewById(R.id.img_trade_item);
            tradeItemHodler.tvTradeItemName = (TextView) convertView
                    .findViewById(R.id.tv_trade_item_name);
            tradeItemHodler.tvTradeItemType = (TextView) convertView
                    .findViewById(R.id.tv_trade_item_type);
            tradeItemHodler.tvTradeItemPrice = (TextView) convertView
                    .findViewById(R.id.tv_trade_item_price);
            tradeItemHodler.tvTradeItemOwner = (TextView) convertView
                    .findViewById(R.id.tv_trade_item_owner);
            tradeItemHodler.tvTradeItemTime = (TextView) convertView
                    .findViewById(R.id.tv_trade_item_time);
            convertView.setTag(tradeItemHodler);
        } else {
            tradeItemHodler = (TradeItemHolder) convertView.getTag();
        }

        // 加载缩略图
        if (mTradeItemList.get(position).getPicTradeItem() != null
                && !mTradeItemList.get(position).getPicTradeItem().getFileUrl(mContext)
                .equals("")) {

            Log.i("文件Url", mTradeItemList.get(position).getPicTradeItem()
                    .getFileUrl(mContext));

            // mTradeItemList.get(position).getPicTradeItem().loadImageThumbnail(mContext,
            // tradeItemHodler.imgTradeItem, 300, 300, 100);

            /*ShopApplication.getInstance().getImageLoader().displayImage(
                    mTradeItemList.get(position).getPicTradeItem().getFileUrl(mContext),
                    tradeItemHodler.imgTradeItem,
                    options);*/
            ShopApplication.getAQuery().id(tradeItemHodler.imgTradeItem).image(
                    mTradeItemList.get(position).getPicTradeItem().getFileUrl(mContext), true, true);
        }

        tradeItemHodler.tvTradeItemName.setText(mTradeItemList.get(position)
                .getItem());
        tradeItemHodler.tvTradeItemType.setText(mTradeItemList.get(position)
                .getType());
        tradeItemHodler.tvTradeItemPrice.setText("￥ "
                + mTradeItemList.get(position).getPrice());
        tradeItemHodler.tvTradeItemOwner.setText(mTradeItemList.get(position)
                .getOwner());
        tradeItemHodler.tvTradeItemTime.setText("发布日期 " + mTradeItemList.get(position)
                .getCreatedAt().split(" ")[0]);
        return convertView;
    }


    /**
     * 二手交易物品视图
     *
     * @author Stone
     * @date 2014-9-15
     */
    public class TradeItemHolder {

        public ImageView imgTradeItem;
        public TextView tvTradeItemName;
        public TextView tvTradeItemType;
        public TextView tvTradeItemPrice;
        public TextView tvTradeItemOwner;
        public TextView tvTradeItemTime;

    }

}
