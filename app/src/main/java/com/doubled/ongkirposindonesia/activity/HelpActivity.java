package com.doubled.ongkirposindonesia.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doubled.ongkirposindonesia.R;


public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBantuan;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        btnBantuan = (Button) findViewById(R.id.btnBantuan);
        btnBantuan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "irfanseptiadiputra@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bantuan Ongkir POS Indonesia v2.2");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

}
