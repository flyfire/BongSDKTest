package com.solarexsoft.bongsdktest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ginshell.sdk.BongManager;
import com.ginshell.sdk.ResultCallback;

/**
 * Created by houruhou on 19/11/2017.
 */

public class IncomingCallService extends Service {
    public static final String KEY_LISTEN_INCOMING = "LISTEN_INCOMING";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context
                .TELEPHONY_SERVICE);
        boolean shouldListen = intent.getBooleanExtra(KEY_LISTEN_INCOMING, true);
        if (shouldListen) {
            telephonyManager.listen(new SolarexPhoneStateListener(), PhoneStateListener
                    .LISTEN_CALL_STATE);
        } else {
            telephonyManager.listen(new SolarexPhoneStateListener(), PhoneStateListener
                    .LISTEN_NONE);
        }
        return START_STICKY;
    }
}

class SolarexPhoneStateListener extends PhoneStateListener {
    public static final String TAG = SolarexPhoneStateListener.class.getSimpleName();

    @Override
    public void onCallStateChanged(int state, final String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        Log.d(TAG, "state = " + state);
        BongManager manager = MemoryCache.mBong.fetchBongManager();
        Log.d(TAG, "manager = " + manager);
        if (manager != null) {
            manager.setMessageNotifyEnable(true, true, true, true, new ResultCallback() {
                @Override
                public void finished() {
                    Log.d(TAG, "setmessagenotifyenable finished");
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    Log.d(TAG, "setmessagenotifyenable error " + throwable.getMessage());
                }
            });
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "来电号码：" + incomingNumber);
                if (manager != null) {
                    manager.sendAddIncomingCallNotify("" + incomingNumber, "" + incomingNumber,
                            new ResultCallback() {


                                @Override
                                public void finished() {
                                    Log.d(TAG, "send call notify " + incomingNumber);
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            });
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, "挂断：" + incomingNumber);
                if (manager != null) {
                    manager.sendDelIncomingCallNotify("" + incomingNumber, "" + incomingNumber,
                            new ResultCallback() {


                                @Override
                                public void finished() {
                                    Log.d(TAG, "send del notify " + incomingNumber);
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
