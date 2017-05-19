package com.example.csci3310gp28.stalkyourfds;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.aprilbrother.aprilbrothersdk.Beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private BeaconController bc;
    private String location;

    final int[] ICONS = new int[] {
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_person_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add fragments to the fragment list
        mFragments = new Vector<>();
        mFragments.add(Fragment.instantiate(this, FdListFragment.class.getName()));
        mFragments.add(Fragment.instantiate(this, ChatroomFragment.class.getName()));
        mFragments.add(Fragment.instantiate(this, AccountFragment.class.getName()));

        // Set custom colors for tab icons
        ColorStateList colors;
        if (Build.VERSION.SDK_INT >= 23) {
            colors = getResources().getColorStateList(R.color.tab_icon, getTheme());
        }
        else {
            colors = getResources().getColorStateList(R.color.tab_icon);
        }

        // Set ViewPager adapter
        CustomPagerAdapter pageAdapter = new CustomPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(pageAdapter);

        // Set icons with custom colors
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i=0; i<mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setIcon(ICONS[i]);
            Drawable icon = tab.getIcon();
            if (icon != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(icon, colors);
            }
        }

        // Grant permission before detecting beacons
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        // Update current location to server
        CallbackInterface cbi = new CallbackInterface() {
            @Override
            public void onRanged(ArrayList<Beacon> beacons) {
                /*int id = -1;
                for(int i = 0;i < beacons.size();i++){
                    Beacon b = beacons.get(i);
                    if(b.getDistance() < 10){
                        if(b.getName().equals("abeacon_C775")){
                            updateLocation("SHB 123");
                        }else if(b.getName().equals("abeacon_C7FC")){
                            updateLocation("SHB 924");
                        }else{
                            updateLocation("Outside lab");
                        }
                    }
                }*/
                location = "";
                if(beacons.size() > 0) {
                    Beacon nearestBeacon = bc.getClosestBeacon(beacons);
                    if(nearestBeacon.getDistance() < 10){
                        if(nearestBeacon.getName().equals("abeacon_C775")){
                            location = "SHB123";
                        }else if(nearestBeacon.getName().equals("abeacon_C7FC")){
                            location = "SHB924";
                        }else{
                            location = "Outside Lab";
                        }
                    }else{
                        location = "Outside Lab";
                    }
                }else{
                    location = "Outside Lab";
                }

                updateLocation(location);
            }
            @Override
            public void onEntered(ArrayList<Beacon> beacons) {
            }

            @Override
            public void onExit() {
            }
        };
        bc = new BeaconController(this,cbi);
        bc.connectToService();

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//
//        SampleFragmentPagerAdapter pagerAdapter =
//                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);
//        viewPager.setAdapter(pagerAdapter);
//
//        tabLayout.setupWithViewPager(viewPager);
//
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(pagerAdapter.getTabView(i));
//            }
//        }
//
//        viewPager.setCurrentItem(1);
    }

    /**
     * Update the current location to database.
     * @param location of the user
     */
    public void updateLocation(String location){
        TextView locationTV = (TextView) mFragments.get(1).getView().findViewById(R.id.chat_location_tv);
        locationTV.setText(location);
    }

    public String getLocation(){
        return location;
    }
}
