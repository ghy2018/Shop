package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.shop.model.Order;

import java.util.ArrayList;
import java.util.List;


/**
 * 购物车商品列表数据适配器
 *
 * @author Stone
 * @date 2014-4-29
 */
public class CartListAdapter extends BaseAdapter {

    public static final String TAG = "CartListAdapter";
    public static final int TYPE_ORDER_ADAPTER = 0x001;
    public static final int TYPE_PAY_ORDER_ADAPTER = 0x002;

    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<Order> mOrderList = new ArrayList<>(); // 所选分类下的所有店铺列表
    private AQuery aq;
    private int type = TYPE_ORDER_ADAPTER;

    public CartListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(context);
    }

    public CartListAdapter(Context context, int adapterType) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(context);
        type = adapterType;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(List<Order> list) {
        mOrderList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CartHolder cartItemHodler;
        if (convertView == null) {
            if(type == TYPE_ORDER_ADAPTER)
                convertView = mInflater.inflate(R.layout.list_item_cart_good, null);
            else if(type == TYPE_PAY_ORDER_ADAPTER)
                convertView = mInflater.inflate(R.layout.list_item_pay_order, null);
            cartItemHodler = new CartHolder();
            cartItemHodler.imgGoodPic = (ImageView) convertView
                    .findViewById(R.id.img_cart_good_pic);
            cartItemHodler.tvGoodName = (TextView) convertView
                    .findViewById(R.id.tv_cart_good_name);
            cartItemHodler.tvGoodCount = (TextView) convertView
                    .findViewById(R.id.tv_cart_good_count);
            cartItemHodler.tvGoodPrice = (TextView) convertView
                    .findViewById(R.id.tv_cart_good_price);
            cartItemHodler.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_cart_good_total_price);
            cartItemHodler.tvShopName = (TextView) convertView.findViewById(R.id.tv_cart_order_shop_name);
            cartItemHodler.btnDelete = convertView.findViewById(R.id.btn_shop_cart_del);
            cartItemHodler.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity activity = (BaseActivity)mContext;
                    activity.showProgressDialog();
                    ShopCartModule.getInstance().remove(mOrderList.get(position));
                }
            });
            convertView.setTag(cartItemHodler);
        } else {
            cartItemHodler = (CartHolder) convertView.getTag();
        }

        AQuery aqimg = aq.recycle(cartItemHodler.imgGoodPic);
        Order order = mOrderList.get(position);
        Double price = Double.parseDouble(order.getGood().getPrice());
        cartItemHodler.tvGoodName.setText(order.getGood().getName());
        // 商店的类型需要单独处理

        if(type == TYPE_PAY_ORDER_ADAPTER) {
            cartItemHodler.tvGoodPrice.setText("¥ " + price);
            cartItemHodler.tvGoodCount.setText("x " + order.getCount());
            cartItemHodler.tvTotalPrice.setText("¥ " + order.getCost());
        } else if(type == TYPE_ORDER_ADAPTER) {
            cartItemHodler.tvGoodPrice.setText("单价: ¥" + price);
            cartItemHodler.tvGoodCount.setText("数量: " + order.getCount());
            cartItemHodler.tvTotalPrice.setText("总价: ¥" + order.getCost());
        }
        cartItemHodler.imgGoodPic.setTag(false);

        if (order.getGood().getShop() != null) {
            cartItemHodler.tvShopName.setText(order.getGood().getShop().getName());
        } else {
            cartItemHodler.tvShopName.setText("校园小菜");
        }
        if (order.getGood()!=null && order.getGood().getPicGood()!=null && !order.getGood().getPicGood().getUrl().isEmpty()) {
            aqimg.id(R.id.img_cart_good_pic).image(order.getGood().getPicGood().getFileUrl(mContext), true, true);
        } else {
            aqimg.id(R.id.img_cart_good_pic).image(R.drawable.ic_shop_defalut);
        }
        return convertView;
    }


    /**
     * 购物车Item视图
     *
     * @author Stone
     * @date 2014-4-29
     */
    public class CartHolder {

        //商品图片
        public ImageView imgGoodPic;

        //名称
        public TextView tvGoodName;

        //价格
        public TextView tvGoodPrice;

        //数量
        public TextView tvGoodCount;

        //总价
        public TextView tvTotalPrice;

        public TextView tvShopName;

        public View btnDelete;

    }

}
