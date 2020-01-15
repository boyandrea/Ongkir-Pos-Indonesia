package com.doubled.ongkirposindonesia.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Irfan Septiadi Putra on 13/09/2015.
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "ongkir_pos";
    private static final String IS_LOGIN = "login";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public SessionManager(Context ctx){
        this._context = ctx;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createRegister(String key){
        editor.putString("key",key);
        editor.putBoolean(IS_LOGIN,true);
        editor.commit();
    }


    public boolean isRegister(){
        return  pref.getBoolean(IS_LOGIN,false);
    }

}
