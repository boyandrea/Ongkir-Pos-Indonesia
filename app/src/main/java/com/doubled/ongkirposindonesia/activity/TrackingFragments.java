package com.doubled.ongkirposindonesia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.adapter.CardTrackingAdapter;
import com.doubled.ongkirposindonesia.helper.DatabaseHandler;
import com.doubled.ongkirposindonesia.helper.MyApplication;
import com.doubled.ongkirposindonesia.model.Resi;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * Created by Irfan Septiadi Putra on 28/02/2016.
 */
public class TrackingFragments extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    EditText inputResi;
    ImageButton btnCekResi;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<Resi> dataResi = new ArrayList<Resi>();
    DatabaseHandler dbHandler;
    String nomor = null;
    private Tracker mTracker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracking,container,false);
        inputResi = (EditText) rootView.findViewById(R.id.inputResi);
        btnCekResi = (ImageButton) rootView.findViewById(R.id.btnCekResi);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.TrackingRecycleView);

        btnCekResi.setOnClickListener(this);
        inputResi.setOnEditorActionListener(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        initRecycleView();
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

    @Override
    public void onClick(View v) {
        nomor = inputResi.getText().toString();
        if(nomor.length() > 4){
            sendInfoAnalytics("Tracking","Mencoba Tracking");
            Intent intent = new Intent(getActivity(),TrackingResultActivity.class);
            intent.putExtra("nomor",nomor);
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(), "Silakan isi nomor resi", Toast.LENGTH_SHORT).show();
        }
    }

    public void initRecycleView(){
        dbHandler = new DatabaseHandler(getActivity());
        dataResi = dbHandler.getDataResi();
        mAdapter = new CardTrackingAdapter(dataResi,getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!dataResi.isEmpty()){
            dataResi.clear();
        }
        dataResi = dbHandler.getDataResi();
        mAdapter = new CardTrackingAdapter(dataResi,getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            btnCekResi.performClick();
            return true;
        }
        return false;
    }
}
