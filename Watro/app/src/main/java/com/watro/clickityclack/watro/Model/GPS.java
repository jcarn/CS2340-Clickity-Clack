package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/13/2017.
 */
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

public final class GPS implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static GPS _instance = new GPS();
    private static Activity _activity;

    private static boolean _isGPSEnabled = false;
    private static boolean _isNetworkEnabled = false;
    private static boolean _canGetLocation = false;
    private static boolean _isPermissionEnabled = false;

    private Location _location;
    private double _latitude;
    private double _longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    private static LocationManager _locationManager;

    private LocationPermissionResponseListener _locationPermissionListener;
    public static final int LOCATION_REQUEST_CODE = 200;

    private GPS() {}

    public static GPS sharedInstance(Activity activity) {
        _activity = activity;
        _locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
        _isGPSEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        _isNetworkEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!_isGPSEnabled && !_isNetworkEnabled) {
            _canGetLocation = false;
        } else {
            _canGetLocation = true;
        }

        if (ActivityCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _isPermissionEnabled = true;
        } else {
            _isPermissionEnabled = false;
            ActivityCompat.requestPermissions(_activity, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, GPS.LOCATION_REQUEST_CODE);
        }

        return _instance;
    }

    public Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _isPermissionEnabled = false;
        } else {
            if (_canGetLocation) {
                if (_isNetworkEnabled) {
                    _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (_locationManager != null) {
                        _location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (_location != null) {
                            _latitude = _location.getLatitude();
                            _longitude = _location.getLongitude();
                        }
                    }
                }

                if (_isGPSEnabled) {
                    if (_location == null) {
                        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (_locationManager != null) {
                            _location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (_location != null) {
                                _latitude = _location.getLatitude();
                                _longitude = _location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        return _location;
    }

    public void stopUsingGPS() {
        if (_locationManager != null) {
            if (ActivityCompat.checkSelfPermission(_activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                _locationManager.removeUpdates(GPS.this);
            }
        }
    }

    public double getLatitude() {
        if (_locationManager != null) {
            _latitude = _location.getLatitude();
        }

        return _latitude;
    }

    public double getLongitude() {
        if (_locationManager != null) {
            _longitude = _location.getLongitude();
        }

        return _longitude;
    }

    public boolean canGetLocation() {
        return _canGetLocation;
    }

    public boolean isPermissionEnabled() {
        return _isPermissionEnabled;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_activity);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        _activity.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void requestLocationPermission(LocationPermissionResponseListener listener) {
        _locationPermissionListener = listener;

        ActivityCompat.requestPermissions(_activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(Location location) {
        this._location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GPS.LOCATION_REQUEST_CODE: {
                _locationPermissionListener.onResponse(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                _isPermissionEnabled = true;
            }
        }
    }

    public static interface LocationPermissionResponseListener {
        public void onResponse(Boolean permissionGranted);
    }

}
