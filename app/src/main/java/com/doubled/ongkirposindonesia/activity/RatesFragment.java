package com.doubled.ongkirposindonesia.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

import java.io.UnsupportedEncodingException;

/**
 * Created by Irfan Septiadi Putra on 28/02/2016.
 */
public class RatesFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private Button btnCekOngkir;
    AutoCompleteTextView atcKotaAsal,atcKotaTujuan;
    EditText editBerat,edtPanjang,edtLebar,edtTinggi,edtNilai,edtDiameter;
    RequestQueue requestQueue;
    String asal,tujuan,berat,panjang="0",lebar="0",tinggi="0",diameter="0",nilai="";
    DatabaseHandler dbHandler;
    ProgressDialog loading;
    private Tracker mTracker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rates,container,false);
        dbHandler = new DatabaseHandler(getContext());
        btnCekOngkir = (Button) rootView.findViewById(R.id.btnOngkir);
        btnCekOngkir.setOnClickListener(this);
        atcKotaAsal = (AutoCompleteTextView) rootView.findViewById(R.id.autotxtAsal);
        atcKotaTujuan = (AutoCompleteTextView) rootView.findViewById(R.id.autotxtTujuan);
        editBerat = (EditText) rootView.findViewById(R.id.editBerat);
        editBerat.setOnEditorActionListener(this);
        edtPanjang = (EditText) rootView.findViewById(R.id.editPanjang);
        edtLebar = (EditText) rootView.findViewById(R.id.editLebar);
        edtTinggi = (EditText) rootView.findViewById(R.id.editTinggi);
        edtDiameter = (EditText) rootView.findViewById(R.id.editDiameter);
        edtNilai = (EditText) rootView.findViewById(R.id.editNilai);
        loading = new ProgressDialog(getContext());
        settingAutoComplete();
        setUpAnalytics();
        return rootView;
    }

    private void setUpAnalytics(){
        MyApplication application = (MyApplication) getContext().getApplicationContext();
        mTracker = application.getDefaultTracker();
    }

    private void sendInfoAnalytics(String Category, String Action){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Category)
                .setAction(Action)
                .build());
    }

    private void settingAutoComplete(){
        dbHandler = new DatabaseHandler(getActivity());
        TujuanAdapter tujuanadapter = new TujuanAdapter(dbHandler);
        AsalAdapter asalAdapter = new AsalAdapter(dbHandler);
        atcKotaTujuan.setAdapter(tujuanadapter);
        atcKotaAsal.setAdapter(asalAdapter);
    }

    @Override
    public void onClick(View v) {
        asal = atcKotaAsal.getText().toString();
        tujuan = atcKotaTujuan.getText().toString();
        berat = editBerat.getText().toString();

        if(asal.length() >3 && tujuan.length() > 3 && berat.length() > 0){
                if(edtPanjang.getText().length() != 0 && edtTinggi.getText().length() != 0 && edtLebar.getText().length() != 0){
                    panjang = edtPanjang.getText().toString();
                    lebar = edtLebar.getText().toString();
                    tinggi = edtTinggi.getText().toString();
                    try {
                        sendInfoAnalytics("Rates","Mencoba Check Ongkir");
                        callingServer();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else if(edtDiameter.getText().length() != 0){
                    diameter = edtDiameter.getText().toString();
                    try {
                        sendInfoAnalytics("Rates","Mencoba Check Ongkir");
                        callingServer();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else if(edtNilai.getText().length() != 0){
                    nilai = edtNilai.getText().toString();
                    try {
                        sendInfoAnalytics("Rates","Mencoba Check Ongkir");
                        callingServer();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        sendInfoAnalytics("Rates","Mencoba Check Ongkir");
                        callingServer();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                Toast.makeText(getActivity(),"Silakan isi form dengan benar",Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(requestQueue != null){
            requestQueue.cancelAll("Stop Queue");
        }
    }

    private void callingServer() throws UnsupportedEncodingException {
        loading.setMessage("Mohon tunggu...");
        loading.show();
        requestQueue = Volley.newRequestQueue(getContext());
        String url = Constanta.URL_RATES+"asal="+asal+"&tujuan="+tujuan+"&berat="+berat+"&pjg="+panjang+"&lbr="+lebar+"&tgi="+tinggi+"&dmtr="+diameter+"&nb="+nilai;
        url = url.replaceAll(" ", "%20");
        final StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                if(response != null){
                    showResult(response);
                }else{
                    sendInfoAnalytics("Rates","Error null");
                    Toast.makeText(getContext(),"Terjadi kesalahan, silakan coba kembali",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("Error",""+networkResponse.statusCode);
                sendInfoAnalytics("Rates","Server tidak merespon");
                Toast.makeText(getContext(),"Masalah pada jaringan, silakan coba kembali",Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        requestQueue.add(stringRequest);
    }

    private void showResult(String data){
            Intent resultIntent = new Intent(getContext(),RatesResultActivity.class);
            resultIntent.putExtra("data",data);
            resultIntent.putExtra("asal",asal);
            resultIntent.putExtra("tujuan",tujuan);
            resultIntent.putExtra("berat",berat);
            startActivity(resultIntent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            btnCekOngkir.performClick();
            return true;
        }
        return false;
    }

    public class TujuanAdapter extends CursorAdapter implements AdapterView.OnItemClickListener{

        private DatabaseHandler dbHelper;
        public TujuanAdapter(DatabaseHandler dh){
            super(getActivity(),null);
            this.dbHelper = dh;

        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (getFilterQueryProvider() != null) {
                return getFilterQueryProvider().runQuery(constraint);
            }

            Cursor cursor = dbHelper.fetchCity(
                    (constraint != null ? constraint.toString() : "@@@@"));

            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final String kota = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            return kota;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) atcKotaTujuan.getAdapter().getItem(position);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.row_tujuan,parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String kota = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            TextView txtTujuan = (TextView) view.findViewById(R.id.txtTujuan);
            txtTujuan.setText(kota);
        }
    }

    public class AsalAdapter extends CursorAdapter implements AdapterView.OnItemClickListener{

        private DatabaseHandler dbHelper;
        public AsalAdapter(DatabaseHandler dh){
            super(getActivity(),null);
            this.dbHelper = dh;
        }
        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (getFilterQueryProvider() != null) {
                return getFilterQueryProvider().runQuery(constraint);
            }

            Cursor cursor = dbHelper.fetchCity(
                    (constraint != null ? constraint.toString() : "@@@@"));

            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final String kota = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            return kota;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) atcKotaAsal.getAdapter().getItem(position);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.row_tujuan,parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String kota = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            TextView txtTujuan = (TextView) view.findViewById(R.id.txtTujuan);
            txtTujuan.setText(kota);
        }
    }
}
