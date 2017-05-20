package com.example.csci3310gp28.stalkyourfds;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    //private final String[] tabTitles = {"Friends", "Chatroom", "Account"};
    private final String[] tabTitles = {"Friends", "Account"};

    private List<Fragment> mFragments;

    public CustomPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
