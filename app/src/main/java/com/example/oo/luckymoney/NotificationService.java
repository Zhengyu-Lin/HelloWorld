package com.example.oo.luckymoney;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("NewApi")
@SuppressWarnings("NewApi")
public class NotificationService extends NotificationListenerService {



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (null != notification) {
            Bundle extras = notification.extras;
            if (null != extras) {
                List<String> textList = new ArrayList<String>();
                String title = extras.getString("android.title");
                if (!TextUtils.isEmpty(title)) textList.add(title);

                String detailText = extras.getString("android.text");
                if (!TextUtils.isEmpty(detailText)) textList.add(detailText);


                if (textList.size() > 0) {
                    for (String text : textList) {
                        if (!TextUtils.isEmpty(text) && (text.contains("[微信红包]")||text.contains("[QQ红包]"))) {
                            KeyguardManager km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock"); //参数是LogCat里用的Tag
                            kl.disableKeyguard(); //解锁
                            PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);//获取电源管理器对象
                            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
                            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
                            wl.acquire();//点亮屏幕
                            wl.release();//释放
                            final PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
