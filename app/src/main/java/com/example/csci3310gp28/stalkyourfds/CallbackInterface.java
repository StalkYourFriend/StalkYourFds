package com.example.csci3310gp28.stalkyourfds;

import com.aprilbrother.aprilbrothersdk.Beacon;

import java.util.ArrayList;

/**
 * Created by kalok on 15/05/2017.
 */

public interface CallbackInterface{
    void onRanged(ArrayList<Beacon> beacons);
    void onEntered(ArrayList<Beacon> beacons);
    void onExit();
}
