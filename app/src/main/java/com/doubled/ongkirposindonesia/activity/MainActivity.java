package com.doubled.ongkirposindonesia.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.adapter.ViewPagerAdapter;
import com.doubled.ongkirposindonesia.helper.Ads;
import com.doubled.ongkirposindonesia.helper.Constanta;
import com.doubled.ongkirposindonesia.helper.RegistrationIntentService;
import com.doubled.ongkirposindonesia.helper.SessionManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private InterstitialAd mInterstitialAd;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int RC_READ_PHONE_STATE = 100;
    SessionManager sessionManager;
    private View mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.coordinatorLayout);
        checkPermission();
        setAdsBanner();

        sessionManager = new SessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private String getIMEI(){
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            imei =telephonyManager.getDeviceId();
            return imei;
        }else{
            return null;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM Info", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_news:
                Intent intentNews = new Intent(this,NewsActivity.class);
                startActivity(intentNews);
                break;
            case R.id.action_help:
                Intent intentHelp = new Intent(this,HelpActivity.class);
                startActivity(intentHelp);
                break;
            case R.id.action_cs:
                Intent intentCS = new Intent(this,CustomerServiceActivity.class);
                startActivity(intentCS);
                break;
            default:
                Intent intentAbout = new Intent(this,AboutActivity.class);
                startActivity(intentAbout);
                break;
        }
        return true;
    }

    private void setAdsBanner(){
        Ads ads = new Ads();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adsMain);
        ads.loadAdsense(this, layout);
    }

    private void displayInterstitial(int key){
        if(key == 1){
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(Constanta.ADS_INTERSTITIAL);
            final AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new RatesFragment());
        listFragments.add(new TrackingFragments());

        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.nav_rates));
        titleList.add(getResources().getString(R.string.nav_tracking));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),listFragments,titleList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(!sessionManager.isRegister()){
            if (checkPlayServices()) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                intent.putExtra("imei",getIMEI());
                startService(intent);
            }else{
                Log.e("Pendaftaran GCM", "Tidak didukung playservices");
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(RC_READ_PHONE_STATE)
    public void checkPermission(){
        if(EasyPermissions.hasPermissions(this,Manifest.permission.READ_PHONE_STATE)){

        }else {
            EasyPermissions.requestPermissions(this,getString(R.string.permission_readphone_rationale),
                    RC_READ_PHONE_STATE,Manifest.permission.READ_PHONE_STATE);
        }
        displayInterstitial(getIntent().getIntExtra("ads", 0));
    }
}
