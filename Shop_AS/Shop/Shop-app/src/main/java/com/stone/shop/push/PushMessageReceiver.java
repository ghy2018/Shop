package com.stone.shop.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.stone.shop.R;
import com.stone.shop.activity.old.NewsActivity;
import com.stone.shop.base.application.BaseApplication;
import com.stone.shop.base.util.ToastUtils;

import cn.bmob.push.PushConstants;

/**
 * Created by stone on 15/3/31.
 */
public class PushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            ToastUtils.showLongToast(intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            notifyPushMessage("校园小菜", intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
        }
    }

    private void notifyPushMessage(String title, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BaseApplication.getAppContext())
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle(title)
                        .setContentText(content)
                .setVibrate(new long[]{1000L, 3000L, 5000L});
        Intent resultIntent = new Intent(BaseApplication.getAppContext(), NewsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(BaseApplication.getAppContext());
        stackBuilder.addParentStack(NewsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) BaseApplication.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
