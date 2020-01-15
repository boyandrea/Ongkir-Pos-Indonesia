package com.doubled.ongkirposindonesia.helper;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.doubled.ongkirposindonesia.model.Message;
import com.doubled.ongkirposindonesia.model.Price;
import com.doubled.ongkirposindonesia.model.Resi;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;
  
    // Database Name
    private static final String DATABASE_NAME = "pos";
  
    // Labels table name
    private static final String TABLE_CITY = "city";
    private static final String TABLE_RESI = "resi";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_NEWS = "news";

    //Atribut Tabel City
    private static final String KEY_CITY_ID = "_id";
    private static final String KEY_CITY_NAME = "name";

    //Atribut Tabel Resi
    private static final String KEY_RESI_ID = "_id";
    private static final String KEY_RESI_NAMA = "number";
    private static final String KEY_RESI_NOMOR = "name";
    private static final String KEY_RESI_TANGGAL = "date";

    //Atribut Tabel History
    private static final String KEY_HISTORY_ID = "_id";
    private static final String KEY_HISTORY_ASAL = "origin";
    private static final String KEY_HISTORY_TUJUAN = "destination";
    private static final String KEY_HISTORY_BERAT = "weight";
    private static final String KEY_HISTORY_CONTENT = "content";

    //Atribut Tabel News
    private static final String KEY_NEWS_ID = "_id";
    private static final String KEY_NEWS_TIME = "time";
    private static final String KEY_NEWS_MESSAGE = "message";
    

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
  
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NEWS_TIME + " TEXT,"
                + KEY_NEWS_MESSAGE + " TEXT)";
        String CREATE_TABLE_RESI = "CREATE TABLE " + TABLE_RESI + "("
                + KEY_RESI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_RESI_NOMOR + " TEXT,"
                + KEY_RESI_NAMA + " TEXT,"
                + KEY_RESI_TANGGAL + " TEXT)";
        String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_HISTORY_ASAL + " TEXT,"
                + KEY_HISTORY_TUJUAN + " TEXT,"
                + KEY_HISTORY_BERAT + " TEXT,"
                + KEY_HISTORY_CONTENT + " TEXT)";

        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_RESI);
        db.execSQL(CREATE_TABLE_HISTORY);
    }
  
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Upgrade", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HISTORY);
        onCreate(db);
    }

    public Cursor fetchCity(String inputText) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, TABLE_CITY, new String[] {KEY_CITY_ID,KEY_CITY_NAME}, KEY_CITY_NAME + " LIKE ?",
                new String[] { "%"+inputText+"%" }, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public ArrayList<Resi> getDataResi(){
        ArrayList<Resi> data = new ArrayList<Resi>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_RESI + " ORDER BY " + KEY_RESI_TANGGAL + " DESC LIMIT 25", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Resi rs = new Resi();
                rs.setNama(c.getString(c.getColumnIndexOrThrow(KEY_RESI_NAMA)));
                rs.setNomor(c.getString(c.getColumnIndexOrThrow(KEY_RESI_NOMOR)));
                rs.setTanggal(c.getString(c.getColumnIndexOrThrow(KEY_RESI_TANGGAL)));
                data.add(rs);
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList<Price> getDataRates(){
        ArrayList<Price> data = new ArrayList<Price>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + KEY_HISTORY_ID + " DESC LIMIT 30", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Price price = new Price();
                price.setAsal(c.getString(c.getColumnIndexOrThrow(KEY_HISTORY_ASAL)));
                price.setTujuan(c.getString(c.getColumnIndexOrThrow(KEY_HISTORY_TUJUAN)));
                price.setBerat(c.getString(c.getColumnIndexOrThrow(KEY_HISTORY_BERAT)));
                price.setContent(c.getString(c.getColumnIndexOrThrow(KEY_HISTORY_CONTENT)));
                data.add(price);
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList<Message> getNews(){
        ArrayList<Message> data = new ArrayList<Message>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NEWS + " ORDER BY " + KEY_NEWS_ID + " DESC LIMIT 30", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Message msg = new Message();
                msg.setMessage(c.getString(c.getColumnIndexOrThrow(KEY_NEWS_MESSAGE)));
                msg.setTimestamp(Long.parseLong(c.getString(c.getColumnIndexOrThrow(KEY_NEWS_TIME))));
                data.add(msg);
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        Log.e("Get News ", "" + data.size());
        return data;
    }
    
    public void insertResi(String nama,String nomor,String tanggal){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Insert Resi",nomor);
        ContentValues values = new ContentValues();
        values.put(KEY_RESI_NAMA,nama);
        values.put(KEY_RESI_NOMOR, nomor);
        values.put(KEY_RESI_TANGGAL, tanggal);
        db.insert(TABLE_RESI, null, values);
        db.close(); // Closing database connection
    }

    public void insertRates(String asal,String tujuan,String berat,String data){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Insert Rates",asal+" to "+tujuan);
        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY_ASAL,asal);
        values.put(KEY_HISTORY_TUJUAN,tujuan);
        values.put(KEY_HISTORY_BERAT,berat);
        values.put(KEY_HISTORY_CONTENT,data);
        db.insert(TABLE_HISTORY, null, values);
        db.close(); // Closing database connection
    }

    public void insertNews(String content,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Insert News ", time + " : " + content);
        ContentValues values = new ContentValues();
        values.put(KEY_NEWS_TIME,time);
        values.put(KEY_NEWS_MESSAGE,content);
        db.insert(TABLE_NEWS, null, values);
        db.close(); // Closing database connection
    }

    public void deleteResi(String nomor){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESI,KEY_RESI_NOMOR+"="+nomor,null);
        db.close();
    }

    public boolean isResiExist(String nomor){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_RESI + " WHERE "+KEY_RESI_NOMOR+"='"+nomor+"'", null);
        if(c.getCount() != 0){
            c.close();
            db.close();
            return false;
        }else {
            c.close();
            db.close();
            return true;
        }
    }
    
}
