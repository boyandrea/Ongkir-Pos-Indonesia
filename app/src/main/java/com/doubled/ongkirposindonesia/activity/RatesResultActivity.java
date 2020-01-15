package com.doubled.ongkirposindonesia.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.helper.Ads;
import com.doubled.ongkirposindonesia.helper.MyApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Septiadi Putra on 28/02/2016.
 */
public class RatesResultActivity extends AppCompatActivity {

    private TextView txtAsal,txtTujuan,txtBerat;
    private ListView listView;
    String asal,tujuan,berat,content="";
    private Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarResultRates);
        setSupportActionBar(toolbar);
        setUpAnalytics();

        txtAsal = (TextView) findViewById(R.id.txtKotaAsal);
        txtTujuan = (TextView) findViewById(R.id.txtKotaTujuan);
        txtBerat = (TextView) findViewById(R.id.txtBerat);
        listView = (ListView) findViewById(R.id.listRates);
        setAdsBanner();
        String data = getIntent().getStringExtra("data");
        asal = getIntent().getStringExtra("asal");
        tujuan = getIntent().getStringExtra("tujuan");
        berat = getIntent().getStringExtra("berat");
        try{
            txtAsal.setText(asal);
            txtTujuan.setText(tujuan);
            txtBerat.setText(berat+" Gram");
            showRates(data);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_rates, menu);
        return true;
    }

    private void setUpAnalytics(){
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    private void sendInfoAnalytics(String Category, String Action){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Category)
                .setAction(Action)
                .build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                shareOngkir();
                break;
            case R.id.action_copy:
                copyContent();
                break;
        }

        return true;
    }

    public void shareOngkir(){
        String data = asal+" Ke \n"+tujuan+"\n Berat "+berat+" gram \n "+content;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void copyContent(){
        String textToCopy = asal+" Ke \n"+tujuan+"\n Berat "+berat+" gram \n "+content;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",textToCopy);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Harga Ongkos Kirim Berhasil Dicopy", Toast.LENGTH_SHORT).show();
    }

    private void setAdsBanner(){
        Ads ads = new Ads();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adsRateResult);
        ads.loadAdsense(this, layout);
    }

    private void showRates(String data){
        List<String> listData = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            for(int i=0;i<jsonObject.length();i++){
                String temp = jsonObject.getString(""+(i+1));
                listData.add(temp);
                content += temp+"\n";
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listData);
            listView.setAdapter(adapter);
            sendInfoAnalytics("Rates Result","Cek Ongkir Sukses");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
