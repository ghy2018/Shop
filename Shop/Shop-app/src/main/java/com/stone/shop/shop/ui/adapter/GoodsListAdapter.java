package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ShopCartModule;
import com.stone.shop.shop.model.Good;
import com.stone.shop.shop.model.Order;
import com.stone.shop.user.model.User;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class GoodsListAdapter extends BaseAdapter {

    private Context mContext;
    private BaseActivity mActivity;
    private List<Good> mGoodsList; // 商品列表信息
    private LayoutInflater mInflater = null;
    private AQuery aq;

    public GoodsListAdapter(Context context, List<Good> goodsList) {
        mContext = context;
        mActivity = (BaseActivity) mContext;
        mGoodsList = goodsList;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(mContext);
    }

    @Override
    public int getCount() {
        return mGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(List<Good> list) {
        mGoodsList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        GoodsHolder goodHolder;
        final Good good = mGoodsList.get(pos);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_goods, null);
            goodHolder = new GoodsHolder();
            goodHolder.imgGoodPic = (ImageView) convertView
                    .findViewById(R.id.img_cart_good_pic);
            goodHolder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_good_name);
            goodHolder.tvPrice = (TextView) convertView
                    .findViewById(R.id.tv_good_price);
            goodHolder.btnBuyGood = (Button) convertView
                    .findViewById(R.id.btn_buy_good);
            convertView.setTag(goodHolder);
        } else {
            goodHolder = (GoodsHolder) convertView.getTag();
        }

        AQuery aqimg = aq.recycle(goodHolder.imgGoodPic);
        goodHolder.imgGoodPic.setTag(false);
        goodHolder.btnBuyGood.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 创建临时订单
                createOrder(good);
            }
        });
        if (good != null && good.getPicGood() != null && !good.getPicGood().getUrl().isEmpty()) {
            aqimg.id(R.id.img_cart_good_pic).image(good.getPicGood().getFileUrl(mContext), true, true);
        } else {
            aqimg.id(R.id.img_cart_good_pic).image(R.drawable.ic_shop_defalut);
        }
        goodHolder.tvName.setText(good.getName());
        goodHolder.tvPrice.setText("￥" + good.getPrice());
        return convertView;
    }

    private void createOrder(Good good) {
        if (good == null)
            return;

        if (mActivity != null)
            mActivity.showProgressDialog();

        // 加入购物车
        User user = BmobUser.getCurrentUser(mContext, User.class);
        Order order = new Order();
        order.setCount(1);
        order.setGood(good);
        order.setUser(user);
        Double price = Double.parseDouble(good.getPrice());
        order.setCost(order.getCount() * price);


        ShopCartModule.getInstance().add(order);
    }

    /**
     * 商品视图
     *
     * @author Stone
     * @date 2014-4-26
     */
    public class GoodsHolder {

        public ImageView imgGoodPic; //商品图片
        public TextView tvName;   //商品名称
        public TextView tvPrice;  //商品单价
        public Button btnBuyGood;  //购买按钮

    }

}
