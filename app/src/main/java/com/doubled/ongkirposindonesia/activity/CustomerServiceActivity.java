package com.doubled.ongkirposindonesia.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.helper.Constanta;


public class CustomerServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCSWeb,btnCSTwit,btnCSFb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        btnCSWeb = (Button) findViewById(R.id.btnCSWeb);
        btnCSTwit = (Button) findViewById(R.id.btnCSTwitter);
        btnCSFb = (Button) findViewById(R.id.btnCSFacebook);

        btnCSWeb.setOnClickListener(this);
        btnCSTwit.setOnClickListener(this);
        btnCSFb.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCSWeb:
                String url = Constanta.URL_KELUHAN;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.btnCSTwitter:
                String url_twit = Constanta.URL_TWITTER;
                Intent t = new Intent(Intent.ACTION_VIEW);
                t.setData(Uri.parse(url_twit));
                startActivity(t);
                break;
            case R.id.btnCSFacebook:
                String url_fb = Constanta.URL_FACEBOOK;
                Intent fb = new Intent(Intent.ACTION_VIEW);
                fb.setData(Uri.parse(url_fb));
                startActivity(fb);
                break;
        }
    }

}
