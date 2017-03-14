package com.watro.clickityclack.watro.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.HashMap;

/**
 * Created by Uche Nkadi on 3/13/2017.
 */


public class LocationFinder {
    public static HashMap<String, Double> getCoordinates(Activity activity) {
        HashMap<String, Double> coordinatesHashMap = new HashMap<>();
        if (ActivityCompat.checkSelfPermission(
                activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            coordinatesHashMap.put("Longitude", location.getLongitude());
            coordinatesHashMap.put("Latitude", location.getLatitude());
            return coordinatesHashMap;
        }
        return null;
    }
}
