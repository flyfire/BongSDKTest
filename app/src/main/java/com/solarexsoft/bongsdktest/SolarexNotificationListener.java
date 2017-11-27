package com.solarexsoft.bongsdktest;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.ginshell.sdk.BongManager;
import com.ginshell.sdk.ResultCallback;

/**
 * Created by houruhou on 19/11/2017.
 */

public class SolarexNotificationListener extends NotificationListenerService {
    public static final String TAG = SolarexNotificationListener.class.getSimpleName();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, sbn.getPackageName());
        Bundle notification = sbn.getNotification().extras;
        String title = notification.getString(Notification.EXTRA_TITLE);
        final String content = notification.getString(Notification.EXTRA_TEXT);
        Log.d(TAG, title + "@" + content);
        String pkgName = sbn.getPackageName();
        if (!TextUtils.isEmpty(pkgName)) {
            BongManager manager = MemoryCache.mBong.fetchBongManager();
            Log.d(TAG, "manager = " + manager);
            if (manager != null) {
                manager.setMessageNotifyEnable(true, true, true, true, new ResultCallback() {
                    @Override
                    public void finished() {
                        Log.d(TAG, "finished");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
                if (pkgName.equals("com.tencent.mobileqq")) {
                    manager.sendAddAppMsg("QQ", content, 0, 0, new ResultCallback() {
                        @Override
                        public void finished() {
                            Log.d(TAG, "send qq message : " + content);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                } else if (pkgName.equals("com.tencent.mm")) {
                    manager.sendAddAppMsg("微信", content, 0, 0, new ResultCallback() {
                        @Override
                        public void finished() {
                            Log.d(TAG, "send wechat message : " + content);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                } else if (pkgName.equals("com.android.mms")) {
                    manager.sendAddSms("NDY", content, 0, new ResultCallback() {
                        @Override
                        public void finished() {
                            Log.d(TAG, "send sms message : " + content);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
