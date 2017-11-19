package com.solarexsoft.bongsdktest;

import android.app.Application;
import android.util.Log;

import com.ginshell.ble.BLEInitCallback;
import com.ginshell.sdk.Bong;

/**
 * Created by houruhou on 19/11/2017.
 */

public class SolarexApplication extends Application {
    public static final String TAG = SolarexApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        MemoryCache.mBong = new Bong(this);
        MemoryCache.mBong.connect("FE:8B:F8:0D:E3:83", new BLEInitCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connect success");
            }

            @Override
            public boolean onFailure(int i) {
                Log.d(TAG, "connect failure: " + i);
                return false;
            }
        });
    }
}
