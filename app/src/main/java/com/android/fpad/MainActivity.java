package com.android.fpad;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class MainActivity extends android.app.Application {
    private static Bus mEventBus;

    public static Bus getBus() {
        return mEventBus;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mEventBus = new Bus(ThreadEnforcer.ANY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}