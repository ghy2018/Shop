package com.stone.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 本地广播接收器
 *
 * Created by stone on 15/4/4.
 */
public class ActivityIntentReceiver extends BroadcastReceiver{

    @Deprecated
    public static final String ACTION_TO_SHOP_CART = "com.stone.shop.ACTION_TO_SHOP_CART";

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
