package com.example.myfirstapp;

import android.location.Location;

public class LocationSingleton {
    private static Location location;

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        LocationSingleton.location = location;
    }
}