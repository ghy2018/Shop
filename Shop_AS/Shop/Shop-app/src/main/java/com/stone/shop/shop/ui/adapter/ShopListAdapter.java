package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.shop.model.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器--适配某一分类下的店铺列表数据
 *
 * @author Stone
 * @date 2014-4-29
 */
public class ShopListAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<Shop> mShopList = null; // 所选分类下的所有店铺列表
    private AQuery aq;

    public ShopListAdapter(Context context, ArrayList<Shop> shopList) {
        mContext = context;
        mShopList = shopList;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(mContext);
    }

    @Override
    public int getCount() {
        return mShopList.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<Shop> list) {
        mShopList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopHolder shopHodler;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_shop_all, null);
            shopHodler = new ShopHolder();
            shopHodler.imgShopPic = (ImageView) convertView
                    .findViewById(R.id.img_shop);
            shopHodler.tvShopName = (TextView) convertView
                    .findViewById(R.id.tv_shop_name);
            shopHodler.tvTagOfficial = (TextView) convertView
                    .findViewById(R.id.tv_tag_official);
            shopHodler.tvTagPromoted = (TextView) convertView
                    .findViewById(R.id.tv_tag_promoted);
            shopHodler.tvShopScrope = (TextView) convertView
                    .findViewById(R.id.tv_shop_type);
            shopHodler.tvShopLoc = (TextView) convertView
                    .findViewById(R.id.tv_shop_loc);
            shopHodler.rbShopRate = (RatingBar) convertView
                    .findViewById(R.id.rb_rate);
            //店铺缩略图
//            if (mShopList.get(position).getPicShop() != null) {
//                mShopList
//                        .get(position)
//                        .getPicShop()
//                        .loadImageThumbnail(mContext, shopHodler.imgShopPic, 150,
//                                100, 100);
//            }
            convertView.setTag(shopHodler);
        } else {
            shopHodler = (ShopHolder) convertView.getTag();
        }

        AQuery aqImg = new AQuery(shopHodler.imgShopPic);
        Shop shop = mShopList.get(position);
        if (null != shop.getPicShop() &&  !shop.getPicShop().getFileUrl(mContext).isEmpty()) {
            String url = shop.getPicShop().getFileUrl(mContext);
            if(url != null && !url.isEmpty()) {
                //shouldDelay(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, String url)
                if(!aqImg.shouldDelay(position, false, convertView, parent, url))
                    aqImg.image(url, true, true, 240, 0, null, android.R.anim.fade_in, 0.8f);
            }
        } else {
            aqImg.id(R.id.img_shop).image(R.drawable.ic_1);
        }

        //官方认证标识
        if (null != shop.getIsOfficial() && shop.getIsOfficial()) {
            shopHodler.tvTagOfficial.setVisibility(View.VISIBLE);
        } else {
            shopHodler.tvTagOfficial.setVisibility(View.GONE);
        }

        //活动推广标识
        if (null != shop.getIsPromoted() && shop.getIsPromoted()) {
            shopHodler.tvTagPromoted.setVisibility(View.VISIBLE);
        } else {
            shopHodler.tvTagPromoted.setVisibility(View.GONE);
        }

        shopHodler.tvShopName.setText(shop.getName());
        // 商店的类型需要单独处理
        shopHodler.tvShopScrope.setText(shop.getScrope());
        shopHodler.tvShopLoc.setText(shop.getLocation());
        shopHodler.rbShopRate.setRating(shop.getRates());
        return convertView;
    }


    /**
     * 商店视图
     *
     * @author Stone
     * @date 2014-4-29
     */
    public class ShopHolder {

        public ImageView imgShopPic;
        public TextView tvShopName;
        public TextView tvShopScrope;
        public TextView tvShopLoc;
        public RatingBar rbShopRate;

        public TextView tvTagOfficial;
        public TextView tvTagPromoted;


    }
}
