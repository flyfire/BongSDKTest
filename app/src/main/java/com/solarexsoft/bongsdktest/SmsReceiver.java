package com.solarexsoft.bongsdktest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ginshell.sdk.BongManager;
import com.ginshell.sdk.ResultCallback;

/**
 * Created by houruhou on 19/11/2017.
 */

public class SmsReceiver extends BroadcastReceiver {
    public static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        SmsMessage message = null;
        if (null != extras) {
            Object[] smsObj = (Object[]) extras.get("pdus");
            for (Object o : smsObj) {
                message = SmsMessage.createFromPdu((byte[]) o);
                Log.d(TAG, "message = " + message);
                if (message != null) {
                    String from = message.getOriginatingAddress();
                    final String content = message.getDisplayMessageBody();
                    Log.d(TAG, from + "@" + content);
                    BongManager manager = MemoryCache.mBong.fetchBongManager();
                    Log.d(TAG, "manager = " + manager);
                    if (manager != null) {
                        manager.sendAddSms("NDY", content, 0, new ResultCallback() {
                            @Override
                            public void finished() {
                                Log.d(TAG, "send sms : " + content);
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
    }
}
