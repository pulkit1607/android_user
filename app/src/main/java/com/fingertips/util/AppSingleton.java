package com.fingertips.util;


public class AppSingleton {
    private boolean mLocationPermissionGranted;

    private static final AppSingleton ourInstance = new AppSingleton();
    public static AppSingleton getInstance() {
        return ourInstance;
    }

    private AppSingleton() {
    }


    public static AppSingleton getOurInstance() {
        return ourInstance;
    }



    public void setmLocationPermissionGranted(boolean mLocationPermissionGranted) {
        this.mLocationPermissionGranted = mLocationPermissionGranted;
    }
}
