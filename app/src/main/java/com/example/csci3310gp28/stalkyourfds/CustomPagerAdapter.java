package com.example.csci3310gp28.stalkyourfds;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = {"Friends", "Chatroom", "Account"};

    private List<Fragment> mFragments;

    public CustomPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

//        switch(position) {
//            case 0:
//                fragment = new FdListFragment();
//                break;
//            case 1:
//                fragment = new FdListFragment();
//                break;
//            case 2:
//                fragment = new FdListFragment();
//                break;
//            default:
//                fragment = null;
//                break;
//        }

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
