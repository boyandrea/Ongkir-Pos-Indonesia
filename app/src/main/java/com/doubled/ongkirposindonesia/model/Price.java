package com.doubled.ongkirposindonesia.model;

/**
 * Created by Irfan Septiadi Putra on 09/09/2015.
 */
public class Price {

    private String id;
    private String asal;
    private String tujuan;
    private String berat;
    private String content;

    public String getID(){
        return id;
    }
    public String getAsal(){return asal;}
    public String getTujuan(){return tujuan;}
    public String getBerat(){
        return berat;
    }
    public String getContent(){
        return content;
    }

    public void setID(String id){
        id = id;
    }
    public void setAsal (String as){asal = as;}
    public void setTujuan(String tj){
        tujuan = tj;
    }
    public void setBerat(String br){berat = br;}
    public void setContent(String ct){content = ct;}

}
