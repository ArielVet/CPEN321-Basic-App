package com.arielvet.cpen321milestone1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class locationServices {

    private Activity myActivity;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private String city;
    private String country;

    interface Callback {
        void setValid(String name);
        void setInvalid();
    }

    public locationServices(Activity activity) {
        this.myActivity = activity;
    }

    /**
     * Purpose: Functions checks location Permissions
     * @return True if has access, false if not
     */
    public boolean checkPerms() {
        return (ContextCompat.checkSelfPermission(myActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(myActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Purpose: Functions requests location Permissions
     */
    public void requestPerms() {

        String LOC_COARSE = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        String LOC_FINE = Manifest.permission.ACCESS_FINE_LOCATION;

        // If both are granted then return else
        if (!checkPerms()){
            // If either permission is denied run code
            if(ActivityCompat.shouldShowRequestPermissionRationale(myActivity, LOC_COARSE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(myActivity, LOC_FINE)) {

                // Now that they don't have permission anymore, e gotta re request it
                new AlertDialog.Builder(myActivity)
                        .setTitle("Need Location Permissions")
                        .setMessage("Need perms to mark location on map")
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                            Toast.makeText(myActivity, "We need location permissions for full functionality", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        })
                        .setPositiveButton("OK", (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(myActivity, new String[] {LOC_COARSE, LOC_FINE}, 1)
                        )
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(myActivity, new String[] {LOC_COARSE, LOC_FINE}, 1);
            }

        }
    }

    /**
     * Purpose: Sets up the Geocoder to convert cords -> city and
     *          the location manager/listner to track the cords
     *          Based On: https://stackoverflow.com/questions/22323974/how-to-get-city-name-by-latitude-longitude-in-android
     */
    @SuppressLint("SetTextI18n")
    public void setUpLocationServices(Callback cb){
        //Set up Coordinates to city converter
        geocoder = new Geocoder(myActivity, Locale.getDefault());

        // Sets up location Manager and Listner to find location of person.
        locationManager = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            // Find coordinates of person, converts it to city, and updates city caption
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null){
                    city = addresses.get(0).getLocality();
                    cb.setValid(city);
                }
                else {
                    cb.setInvalid();
                }
            } catch (IOException e) {
                cb.setInvalid();
            }
        };
    }

    /**
     * updateTime in seconds
     * minDistance is idk
     * */
    @SuppressLint("MissingPermission")
    public boolean setLocationUpdater(Long updateTime, int minDistance){
        if (checkPerms()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, minDistance, locationListener);
            return true;
        }

        return false;
    }

}
