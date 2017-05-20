package com.example.csci3310gp28.stalkyourfds;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprilbrother.aprilbrothersdk.Beacon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private BeaconController bc;
    private String location;
    private String lastLocation = "";

    final int[] ICONS = new int[]{
            R.drawable.ic_people_black_24dp,
            //R.drawable.ic_chat_black_24dp,
            R.drawable.ic_person_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add fragments to the fragment list
        mFragments = new Vector<>();
        mFragments.add(Fragment.instantiate(this, FdListFragment.class.getName()));
        //mFragments.add(Fragment.instantiate(this, ChatroomFragment.class.getName()));
        mFragments.add(Fragment.instantiate(this, AccountFragment.class.getName()));

        // Set custom colors for tab icons
        ColorStateList colors;
        if (Build.VERSION.SDK_INT >= 23) {
            colors = getResources().getColorStateList(R.color.tab_icon, getTheme());
        } else {
            colors = getResources().getColorStateList(R.color.tab_icon);
        }

        // Set ViewPager adapter
        CustomPagerAdapter pageAdapter = new CustomPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(pageAdapter);

        // Set icons with custom colors
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter != null) {
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
                        if (beacons.size() > 0) {
                            Beacon nearestBeacon = bc.getClosestBeacon(beacons);
                            if (nearestBeacon.getDistance() < 10) {
                                if (nearestBeacon.getName().equals("abeacon_C775")) {
                                    location = "SHB123";
                                } else if (nearestBeacon.getName().equals("abeacon_C7FC")) {
                                    location = "SHB924";
                                } else {
                                    location = "Outside Lab";
                                }
                            } else {
                                location = "Outside Lab";
                            }
                        } else {
                            location = "Outside Lab";
                        }
                        if (!location.equals(lastLocation)) {
                            lastLocation = location;
                            updateLocation(location);

                        }
                    }

                    @Override
                    public void onEntered(ArrayList<Beacon> beacons) {
                    }

                    @Override
                    public void onExit() {
                    }
                };
                bc = new BeaconController(this, cbi);
                bc.connectToService();
            } else {
                Toast.makeText(getApplicationContext(), "No bluetooth device detected!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
     *
     * @param location of the user
     */

    public void updateLocation(String location) {
        Constants.location = location;
        View temp = mFragments.get(1).getView();
        TextView locationTV = null;
        if (temp != null) {
//             locationTV = (TextView) temp.findViewById(R.id.chat_location_tv);
//            locationTV.setText(location);

        }


        SharedPreferences sharedPref = getSharedPreferences("user",Context.MODE_PRIVATE);

        long id = sharedPref.getLong("id", 1);
        String url = "http://104.198.103.187:3000/users/" + id;
        JSONObject request = new JSONObject();
        try {
            request.put("data",
                    new JSONObject()
                            .put("attributes",
                                    new JSONObject()
                                            .put("location", location)

                            )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PATCH, url, request, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(this.getClass().getSimpleName(), "Response: " + response.toString());
                        //display successful msg
//                        Context context = getApplicationContext();
//                        CharSequence text = "Successfully updated location!";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(context, text, duration);
//                        toast.show();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //display failed msg
                        Context context = getApplicationContext();
                        CharSequence text = "Failed to update location: " + new String(error.networkResponse.data) + "(" + error.networkResponse.statusCode + ")";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public String getLocation() {
        return location;
    }
}
