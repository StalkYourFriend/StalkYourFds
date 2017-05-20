package com.example.csci3310gp28.stalkyourfds;

import java.util.Comparator;
import com.aprilbrother.aprilbrothersdk.Beacon;

/**
 * Created by kalok on 19/05/2017.
 */

public class BeaconComparator implements Comparator {
    public BeaconComparator(){

    }

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        try {
            Beacon b1 = (Beacon) o1;
            Beacon b2 = (Beacon) o2;
            if (b1.getDistance() < b2.getDistance()) {
                result = -1;
            } else if (b1.getDistance() > b2.getDistance()) {
                result = 1;
            }
        }catch(Exception e){

        }
        return result;
    }
}
