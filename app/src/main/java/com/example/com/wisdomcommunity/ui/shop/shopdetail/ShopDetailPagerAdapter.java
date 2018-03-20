package com.example.com.wisdomcommunity.ui.shop.shopdetail;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by rhm on 2018/3/19.
 */

public class ShopDetailPagerAdapter extends FragmentPagerAdapter {
    private String subTitles[];
    private Fragment[] fragments;


    public ShopDetailPagerAdapter(FragmentManager fragmentManager, Fragment[] fragments, String[] subTitles) {
        super(fragmentManager);
        this.subTitles = subTitles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return subTitles[position];
    }
}
