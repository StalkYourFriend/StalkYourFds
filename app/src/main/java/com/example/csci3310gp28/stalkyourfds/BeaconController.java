package com.example.csci3310gp28.stalkyourfds;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.utils.AprilL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeaconController {
    private BeaconManager beaconManager;
    private ArrayList<Beacon> myBeacons;
    private Activity activity;
    private static final Region ALL_BEACONS_REGION = new Region(
            "customRegionName", null, null, null);

    public BeaconController(final Activity activity, final CallbackInterface cbi){
        myBeacons = new ArrayList<Beacon>();
        AprilL.enableDebugLogging(true);
        this.activity = activity;
        beaconManager = new BeaconManager(activity.getApplicationContext());

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                myBeacons.clear();
                myBeacons.addAll(beacons);
                cbi.onRanged(myBeacons);
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                myBeacons.clear();
                myBeacons.addAll(beacons);
                cbi.onEntered(myBeacons);
            }

            @Override
            public void onExitedRegion(Region region) {
                cbi.onExit();
            }
        });
    }

    public BeaconManager getBeaconManager(){
        return beaconManager;
    }

    public void connectToService() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_BEACONS_REGION);
                    // beaconManager.startMonitoring(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    Log.i("BeaconController", e.toString());
                }
            }
        });
    }
}
