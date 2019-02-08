package com.example.android.schoolfinder.Utility;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class AppLocationService extends Service implements LocationListener {
    private static final String TAG = AppLocationService.class.getSimpleName();
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    protected LocationManager locationManager;
    Location location;
    private Context context;

    public AppLocationService(Context context) {
        locationManager = (LocationManager)
                context.getSystemService(LOCATION_SERVICE);
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(String provider) {
        Log.e(TAG, "GetLocation() callled oo ");
        if (locationManager.isProviderEnabled(provider)) {
            Log.e(TAG, "locationManager.isProviderEnabled(provider) IS true");
//            context.checkSelfPermission()
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE,
//                        MIN_DISTANCE_FOR_UPDATE, this);
//            } else {
//                // Show rationale and request permission.
//            }

            locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE,
                    MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        Log.e(TAG, "locationManager.isProviderEnabled(provider) FALSE");
        return null;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
