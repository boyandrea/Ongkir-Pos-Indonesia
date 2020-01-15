package com.doubled.ongkirposindonesia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Septiadi Putra on 28/02/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private  List<Fragment> mFragmentList = new ArrayList<>();
    private  List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager,List<Fragment> listFragment,List<String> titleFragment){
        super(manager);
        mFragmentList = listFragment;
        mFragmentTitleList = titleFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
