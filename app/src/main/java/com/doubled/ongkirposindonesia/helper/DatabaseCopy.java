package com.doubled.ongkirposindonesia.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseCopy extends SQLiteOpenHelper {

   // All Static variables
   // Database Version
   private static final int DATABASE_VERSION = 2;
   private static final String DATABASE_PATH = "/data/data/com.doubled.ongkirposindonesia/databases/";
   // Database Name
   private static final String DATABASE_NAME = "pos";
   private SQLiteDatabase db;
   // Contacts Table Columns names
   
  
   Context ctx;
   public DatabaseCopy(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      ctx = context;
   }
 
  
   
   public void CopyDataBaseFromAsset() throws IOException{
      InputStream in  = ctx.getAssets().open("pos");
      Log.e("sample", "Starting copying" );
      String outputFileName = DATABASE_PATH+DATABASE_NAME;
      File databaseFile = new File( "/data/data/com.doubled.ongkirposindonesia/databases");
       // check if databases folder exists, if not create one and its subfolders
       if (!databaseFile.exists()){
           databaseFile.mkdir();
       }
     
      OutputStream out = new FileOutputStream(outputFileName);
     
      byte[] buffer = new byte[1024];
      int length;
     
     
      while ((length = in.read(buffer))>0){
             out.write(buffer,0,length);
      }
      Log.e("sample", "Completed" );
      out.flush();
      out.close();
      in.close();
     
   }

   public void openDataBase () throws SQLException{
      String path = DATABASE_PATH+DATABASE_NAME;
      db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
   }

      @Override
      public void onCreate(SQLiteDatabase db) {
            
            
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
             // TODO Auto-generated method stub
            
      }
}