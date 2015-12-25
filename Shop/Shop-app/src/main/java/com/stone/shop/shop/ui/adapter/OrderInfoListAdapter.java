package com.stone.shop.shop.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.shop.model.Order;
import com.stone.shop.shop.model.PayOrder;
import com.stone.shop.shop.ui.activity.PayActivity;

import java.util.List;

/**
 * 适配器--适配订单列表中的数据
 *
 * @author Stone
 * @date 2014-5-27
 */
public class OrderInfoListAdapter extends BaseAdapter /*implements StickyListHeadersAdapter*/ {

    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<PayOrder> mPayOrderList = null; // 所选分类下的所有店铺列表

    private String mType; // 商店的分类

    public OrderInfoListAdapter(Context context, List<PayOrder> orderList) {
        mContext = context;
        mPayOrderList = orderList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPayOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPayOrderList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<PayOrder> list) {
        mPayOrderList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderInfoHolder orderInfoHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_order_info, null);
            orderInfoHolder = new OrderInfoHolder();
            orderInfoHolder.initView(convertView);
            convertView.setTag(orderInfoHolder);
        } else {
            orderInfoHolder = (OrderInfoHolder) convertView.getTag();
        }

        PayOrder payOrder = mPayOrderList.get(position);
        orderInfoHolder.initData(payOrder);
        return convertView;
    }

    OnItemClickListener clickListener = new OnItemClickListener() {
        @Override
        public void onClick(PayOrder payOrder, View clickView) {
            if(payOrder == null || clickView == null)
                return;
            int id = clickView.getId();
            switch (id) {
                case R.id.btn_order_info_pay:
                    Intent intent = new Intent(mContext, PayActivity.class);
                    intent.putExtra(PayActivity.KEY_PAY_ORDER_ID, payOrder.getObjectId());
                    intent.putExtra(PayActivity.KEY_PAY_ORDER_MONEY, payOrder.getMoney());
                    mContext.startActivity(intent);
                    //clickView.setEnabled(false);
                    break;
                case R.id.btn_order_info_score:
                    //clickView.setEnabled(false);
                    ToastUtils.showLongToast("已评分");
                    break;
                case R.id.btn_order_info_praise:
                    //clickView.setEnabled(false);
                    ToastUtils.showLongToast("已赞");
                    break;
            }
        }
    };

    public class OrderInfoHolder {

        public View root;
        public TextView tvOrderInfoObjectId;   //订单号

        public TextView tvOrderInfoGoodName;   //商品名称
        public TextView tvOrderInfoShopName;   //店铺名称
        public TextView tvOrderInfoShopLocation;   //店铺地址

        public TextView tvOrderInfoMyLocation;      //收货地址

        public TextView tvOrderInfoCount;      //订单数量
        public TextView tvOrderInfoPrice;      //订单价格
        public TextView tvOrderInfoState;      //订单状态
        public TextView tvOrderInfoTime;       //下单时间

        public ViewGroup vgOrderValue;
        public Button btnOrderScore;    //评分
        public Button btnOrderPraise;   //点赞
        public Button btnOrderPay;

        private PayOrder payOrder;

        public void initView(View convertView) {
            root = convertView;

            tvOrderInfoObjectId = (TextView) convertView
                    .findViewById(R.id.tv_order_info_id);
            tvOrderInfoGoodName = (TextView) convertView
                    .findViewById(R.id.tv_order_info_good_name);
            tvOrderInfoShopName = (TextView) convertView
                    .findViewById(R.id.tv_order_info_shop_name);
            tvOrderInfoShopLocation = (TextView) convertView
                    .findViewById(R.id.tv_order_info_shop_loc);

            // 收货地址(扩展)
            tvOrderInfoMyLocation = (TextView) convertView
                    .findViewById(R.id.tv_order_info_my_loc);

            tvOrderInfoCount = (TextView) convertView
                    .findViewById(R.id.tv_order_info_count);
            tvOrderInfoPrice = (TextView) convertView
                    .findViewById(R.id.tv_order_info_price);
            tvOrderInfoState = (TextView) convertView
                    .findViewById(R.id.tv_order_info_state);
            tvOrderInfoTime = (TextView) convertView
                    .findViewById(R.id.tv_order_info_time);
            // 评分、点赞(扩展)
            btnOrderScore = (Button) convertView
                    .findViewById(R.id.btn_order_info_score);
            btnOrderPraise = (Button) convertView
                    .findViewById(R.id.btn_order_info_praise);
            btnOrderPay = (Button) convertView.findViewById(R.id.btn_order_info_pay);

            btnOrderScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(payOrder, v);
                }
            });
            btnOrderPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(payOrder, v);
                }
            });
            btnOrderPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(payOrder, v);
                }
            });

            vgOrderValue = (ViewGroup) convertView.findViewById(R.id.vg_order_info_value);
            btnOrderPay.setVisibility(View.GONE);
            vgOrderValue.setVisibility(View.GONE);
        }

        public void initData(PayOrder payOrder) {
            if(payOrder == null)
                return;
            this.payOrder = payOrder;
            tvOrderInfoObjectId.setText("订单号 " + payOrder.getObjectId());
            //orderInfoHolder.tvOrderInfoGoodName.setText("商品:  " + mPayOrderList.get(position).getGood().getName());
            //orderInfoHolder.tvOrderInfoShopName.setText("店铺:  " + mPayOrderList.get(position).getGood().getShop().getName());

            //店铺地址(扩展)
            //orderInfoHolder.tvOrderInfoShopLocation.setText(mPayOrderList.get(position).get);

            tvOrderInfoCount.setText("数量: " + payOrder.getCount());
            tvOrderInfoPrice.setText("￥" + payOrder.getMoney());
            tvOrderInfoState.setText(payOrder.getState());
            tvOrderInfoTime.setText("下单时间:  " + payOrder.getCreatedAt());

            //TODO 需要修改逻辑， 根据Code状态码俩判断
            if(payOrder.getState().equals(Order.ORDER_STATE_PAY_SUCCESS)) {
                vgOrderValue.setVisibility(View.VISIBLE);
                btnOrderPay.setVisibility(View.GONE);
            } else {
                vgOrderValue.setVisibility(View.GONE);
                btnOrderPay.setVisibility(View.VISIBLE);
            }
        }

    }

    public interface OnItemClickListener{
        void onClick(PayOrder payOrder, View clickView);
    }

}
