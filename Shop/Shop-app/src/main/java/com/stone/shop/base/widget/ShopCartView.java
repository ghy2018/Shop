package com.stone.shop.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.shop.ShopCartModule;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by stonekity.shi on 2015/1/20.
 */
public class ShopCartView extends FrameLayout{

    private static final String TAG = "ShopCartView";

    protected Context mContext;
    protected BaseActivity mActivity;
    protected TextView tvCount;
    private OnClickListener onClickListener;

    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            Log.d(TAG, "更新购物车商品数量");
            if(mActivity!=null)
                mActivity.dismissProgressDialog();
            updateShopCart();
        }
    };

    public ShopCartView(Context context) {
        super(context);
        mContext = context;
        mActivity = (BaseActivity) mContext;
        init();
    }

    public ShopCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mActivity = (BaseActivity) mContext;
        initView();
        init();
    }

    public ShopCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mActivity = (BaseActivity) mContext;
        init();
    }

    private void init() {
        Log.d(TAG, "购物车注册监听");
        ShopCartModule.getInstance().registerObserver(observer);
    }

    public void setOnClickListener(OnClickListener listener) {
        if(null != listener) {
            this.onClickListener = listener;
        }
    }

    @Override
    protected void onFinishInflate() {
        //super.onFinishInflate();
        initView();
    }


    protected void initView() {
        tvCount = (TextView) findViewById(R.id.tv_shop_cart_count);
        updateShopCart();
    }


    /**
     * 更新购物车
     */
    public void updateShopCart() {
        if(null != tvCount) {
            int count = ShopCartModule.getInstance().getCount();
            tvCount.setText(String.valueOf(count));
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(onClickListener!=null) {
            onClickListener.onClick(this);
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDetachedFromWindow() {
        ShopCartModule.getInstance().unregisterObserver(observer);
        super.onDetachedFromWindow();
    }

}
