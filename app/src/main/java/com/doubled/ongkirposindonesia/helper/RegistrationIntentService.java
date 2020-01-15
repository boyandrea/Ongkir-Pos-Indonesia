package com.doubled.ongkirposindonesia.helper;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doubled.ongkirposindonesia.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Irfan Septiadi Putra on 25/02/2016.
 */
public class RegistrationIntentService extends IntentService {


    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SessionManager sessionManager = new SessionManager(this);
        String imei = intent.getStringExtra("imei");
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token,imei);
            sessionManager.createRegister(token);
            Intent registrationComplete = new Intent(SessionManager.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error Register GCM", ""+e.getCause() );
        }

    }

    private void sendRegistrationToServer(String token,String imei) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Constanta.URL_GATEWAY+"app=pos&token="+token+"&imei="+imei;
        url = url.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response",""+error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}
