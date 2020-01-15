package com.doubled.ongkirposindonesia.model;

/**
 * Created by Irfan Septiadi Putra on 09/09/2015.
 */
public class Resi {

    private String nomor;
    private String nama;
    private String tanggal;

    public String getNomor(){
        return nomor;
    }

    public String getNama(){
        return nama;
    }
    public String getTanggal(){
        return tanggal;
    }

    public void setNomor(String no){
        nomor = no;
    }
    public void setNama (String nm){
        nama = nm;
    }
    public void setTanggal(String tgl){
        tanggal = tgl;
    }

}
