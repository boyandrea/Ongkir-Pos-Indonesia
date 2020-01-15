package com.doubled.ongkirposindonesia.helper;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.smaato.soma.BannerView;

/**
 * Created by Irfan Septiadi Putra on 28/02/2016.
 */
public class Ads {

    AdView banner;

    public void loadAdsense(Context contex,View v) {
        // TODO Auto-generated method stub
        banner = new AdView(contex);
        banner.setAdSize(AdSize.BANNER);
        banner.setAdUnitId(Constanta.ADS_BANNER);
        RelativeLayout layout = (RelativeLayout) v.findViewById(v.getId());
        layout.addView(banner);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1F729740DCB36C2BA19A28A4B3F7A695")
                .build();

        // Start loading the ad in the background.
        banner.loadAd(adRequest);
    }

    public void loadSmaato(Context context, RelativeLayout mLayout){
        BannerView mBanner = new BannerView (context);
        mLayout.addView(mBanner, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,70 ));
        mBanner.getAdSettings().setPublisherId(Constanta.SMAATO_PUB_ID);
        mBanner.getAdSettings().setAdspaceId(Constanta.SMAATO_ADS_ID);
        mBanner.asyncLoadNewBanner();
        mBanner.setScalingEnabled(false);

    }

    public int randomAds(){
        int range = (5 - 0) + 1;
        return (int)(Math.random() * range) + 0;
    }

}
