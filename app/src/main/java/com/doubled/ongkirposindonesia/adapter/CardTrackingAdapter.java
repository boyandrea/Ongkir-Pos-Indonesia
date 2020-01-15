package com.doubled.ongkirposindonesia.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doubled.ongkirposindonesia.helper.DatabaseHandler;
import com.doubled.ongkirposindonesia.model.Price;
import com.doubled.ongkirposindonesia.model.Resi;
import com.doubled.ongkirposindonesia.activity.TrackingResultActivity;

import java.util.ArrayList;
import java.util.List;

import com.doubled.ongkirposindonesia.R;

/**
 * Created by Irfan Septiadi Putra on 09/09/2015.
 */
public class CardTrackingAdapter extends RecyclerView.Adapter<CardTrackingAdapter.ViewHolder> {

    List<Resi> listResi;
    Context context;


    public CardTrackingAdapter(ArrayList<Resi> ls, Context ctx){
        listResi = ls;
        context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tracking_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.trackingToolbars.inflateMenu(R.menu.menu_card);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Resi resi = listResi.get(position);
        viewHolder.txtNomor.setText(resi.getNomor());
        viewHolder.txtNama.setText("Nama : "+resi.getNama());
        viewHolder.txtTanggal.setText("Tanggal : "+resi.getTanggal());

    }

    @Override
    public int getItemCount() {
        return listResi.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

        public TextView txtNomor;
        public TextView txtNama;
        public TextView txtTanggal;
        private String nomor;
        public Toolbar trackingToolbars;
        private DatabaseHandler dbHandler;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNomor = (TextView)itemView.findViewById(R.id.txtResi);
            txtNama = (TextView)itemView.findViewById(R.id.txtNama);
            txtTanggal = (TextView)itemView.findViewById(R.id.txtTanggal);
            trackingToolbars = (Toolbar) itemView.findViewById(R.id.menuCardTracking);
            dbHandler = new DatabaseHandler(context);
            itemView.setOnClickListener(this);
            trackingToolbars.setOnMenuItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            nomor = txtNomor.getText().toString();
            showDialog();
        }

        public void showDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle("Tracking");
            builder.setMessage("Tracking "+nomor+" ?");
            builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, TrackingResultActivity.class);
                    intent.putExtra("nomor",nomor);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

        private void showUpdateDialog(){
            nomor = txtNomor.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle("Update Tracking");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

        private void showDeleteDialog(){
            nomor = txtNomor.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle("Delete Tracking "+nomor+" ?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        dbHandler.deleteResi(nomor);
                        listResi.clear();
                        listResi = dbHandler.getDataResi();
                        notifyDataSetChanged();
                        Toast.makeText(context,"Resi "+nomor+" berhasil dihapus",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            showDeleteDialog();
            return false;
        }
    }
}
