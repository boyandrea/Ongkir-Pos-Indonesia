package com.doubled.ongkirposindonesia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.helper.DatabaseCopy;
import com.doubled.ongkirposindonesia.helper.DatabaseHandler;

public class Start extends Activity {

    DatabaseCopy cp;
    DatabaseHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        cp = new DatabaseCopy(getApplicationContext());
        dh = new DatabaseHandler(getApplicationContext());


        if(!doesDatabaseExist(getApplication(), "pos")){
            new copyData().execute();
        } else {
            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(3000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        Intent intent = new Intent(Start.this,MainActivity.class);
                        intent.putExtra("ads",1);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }

    }

    public boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private class copyData extends AsyncTask<String,String[], String> {

        ProgressDialog wait = new ProgressDialog(Start.this);


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            wait.setMessage("Sedang menyalin database....");
            wait.show();
            wait.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String data = null;
            try {
                cp.CopyDataBaseFromAsset();
                data = "Berhasil";
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error Copy", "" + e.getCause());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            wait.dismiss();
            if (result != null){
                Toast.makeText(Start.this, "Berhasil menyalin database", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Start.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
               AlertDialog alertDialog = new AlertDialog.Builder(Start.this).create();
                alertDialog.setTitle("Oppss!!");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setMessage("Gagal menyalin database");
                alertDialog.show();
            }
        }
    }

}
