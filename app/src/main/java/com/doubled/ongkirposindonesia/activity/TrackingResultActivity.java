package com.doubled.ongkirposindonesia.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.helper.Ads;
import com.doubled.ongkirposindonesia.helper.Constanta;
import com.doubled.ongkirposindonesia.helper.DatabaseHandler;
import com.doubled.ongkirposindonesia.helper.MyApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irfan Septiadi Putra on 01/03/2016.
 */
public class TrackingResultActivity extends AppCompatActivity {

    private WebView myView;
    private Toolbar myToolbar;
    private DatabaseHandler dbHandler;
    boolean isExist = false;
    String nomor = null;
    ProgressDialog loading;
    RequestQueue requestQueue;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tracking);
        dbHandler = new DatabaseHandler(this);
        myView = (WebView) findViewById(R.id.wvTracking);
        myToolbar = (Toolbar) findViewById(R.id.toolbarResultTracking);
        loading = new ProgressDialog(this);
        myToolbar.setTitle("Tracking Result");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadAds();
        setUpAnalytics();
        nomor = getIntent().getStringExtra("nomor");
        isExist = dbHandler.isResiExist(nomor);

        try {
            callingServer();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(TrackingResultActivity.this,"Mohon Maaf Terjadi Kesalahan",Toast.LENGTH_LONG).show();
        }

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

    private void loadAds(){
        Ads ads = new Ads();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adResultTracking);
        ads.loadSmaato(this,layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isExist){
            showDialog();
        }else{
            Toast.makeText(this, "Ooppss.. Resi sudah ada dalam database", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void saveTracking(String nama){
        try{
            dbHandler.insertResi(nama,nomor,getDate());
            Toast.makeText(this,"Resi berhasil disimpan",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"Error ketika menyimpan,silakan coba lagi",Toast.LENGTH_SHORT).show();
        }


    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date); //2014/08/06 15:59:48
    }

    public void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.input_save_resi, null);
        final EditText edtNama = (EditText) dialoglayout.findViewById(R.id.edtInputNama);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("Save Tracking");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nama = edtNama.getText().toString();
                if (nomor.length() > 0) {
                    saveTracking(nama);
                } else {
                    edtNama.setError("Nama Tidak Boleh Kosong");
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setView(dialoglayout);
        builder.show();
    }

    private void callingServer() throws UnsupportedEncodingException {
        loading.setMessage("Mohon tunggu...");
        loading.show();
        requestQueue = Volley.newRequestQueue(this);
        String url = Constanta.URL_RESI;
        url = url.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        parseData(response);
                        sendInfoAnalytics("Tracking Result", "Tracking Berhasil");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(TrackingResultActivity.this, "Terjadi kesalahan, silakan coba kembali", Toast.LENGTH_SHORT).show();
                        sendInfoAnalytics("Tracking Result","Error Tracking Result");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("kiriman",nomor);
                params.put("lacak","Cari Kiriman");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        requestQueue.add(stringRequest);
    }

    private void parseData(String data){
        Document doc = Jsoup.parse(data);
        Elements btnDetail = doc.select("#detail");
        Elements btnCetak = doc.select("#cetak");
        Elements imgPos = doc.select("img[src=image/logo.png]");
        btnDetail.remove();
        btnCetak.remove();
        imgPos.remove();
        data = doc.toString();
        myView.loadData(data, "text/html", "UTF-8");
    }

}
