package com.doubled.ongkirposindonesia.helper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.doubled.ongkirposindonesia.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Irfan Septiadi Putra on 05/03/2016.
 */
public class MyApplication extends MultiDexApplication {

    private Tracker mTracker;
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
