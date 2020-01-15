package com.doubled.ongkirposindonesia.helper;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Irfan Septiadi Putra on 25/02/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
