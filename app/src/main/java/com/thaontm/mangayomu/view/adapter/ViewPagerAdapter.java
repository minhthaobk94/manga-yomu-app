package com.thaontm.mangayomu.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by thao on 1/10/2017.
 * Copyright thao 2017.
 */
public class  ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "NEW";
            case 1:
                return "UPDATED";
            case 2:
                return "POPULAR";
        }
        return null;
    }
}
