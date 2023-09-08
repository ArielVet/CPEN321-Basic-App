package com.arielvet.cpen321milestone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PhoneDetails extends AppCompatActivity {

    private TextView cityCap;
    private TextView manufacturerCap;
    private TextView modelCap;

    private String LocationCap;

    private Geocoder geocoder;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private final static String NoLoc = "Unknown";
    private final static Long UpdateTime = 2L;

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_details);

        // Requests the user for permissions if they havent been given yet
        requestLocationPermissions();

        setUpLocationServices();

        //Saves TextView elements to variables
        cityCap = findViewById(R.id.current_city_text);
        manufacturerCap = findViewById(R.id.manuf_text);
        modelCap = findViewById(R.id.model_text);

        //Get the caption for city Location
        LocationCap = getString(R.string.curr_city_cap);

        // If We have access to locations, activate the location finder to find and set city caption,
        // else set it to unknown
        if (checkLocationPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UpdateTime * 1000, 0, locationListener);
        }
        else {
            cityCap.setText(LocationCap + " " + NoLoc);
        }

        //Set Manufacturer and Phone Model Captions
        manufacturerCap.setText(getString(R.string.manuf_cap) + " " + android.os.Build.MANUFACTURER);
        modelCap.setText(getString(R.string.model_cap) + " " + android.os.Build.MODEL);

    }

    /**
     * Purpose: Functions checks location Permissions
     * @return True if has access, false if not
     */
    private boolean checkLocationPermissions() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /**  Purpose: Functions requests location Permissions
     *   Returns: void
     * */
    private void requestLocationPermissions() {

        String LOC_COARSE = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        String LOC_FINE = Manifest.permission.ACCESS_FINE_LOCATION;

        // If both are granted then return else
        if (!checkLocationPermissions()){
            // If either permission is denied run code
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, LOC_COARSE) || ActivityCompat.shouldShowRequestPermissionRationale(this, LOC_FINE)) {

                // Now that they dont have permission anymore, e gotta re request it
                new AlertDialog.Builder(this)
                        .setTitle("Need Location Permissions")
                        .setMessage("Need perms to mark location on map")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(PhoneDetails.this, "We need location permissions for full functionality", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(PhoneDetails.this, new String[] {LOC_COARSE, LOC_FINE}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {LOC_COARSE, LOC_FINE}, 1);
            }

        }
    }

    /**
     * Purpose: Sets up the Geocoder to convert cords -> city and
     *          the location manager/listner to track the cords
     *
     */
    private void setUpLocationServices(){
        //Set up Coordinates to city converter
        geocoder = new Geocoder(this, Locale.getDefault());

        // Sets up location Manager and Listner to find location of person.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Find coordinates of person, converts it to city, and updates city caption
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    cityCap.setText(LocationCap + " " + addresses.get(0).getLocality());
                } catch (IOException e) {
                    cityCap.setText(LocationCap + " " + NoLoc);
                }
            }
        };
    }

    /**
     * Purpose: Once the user interacts with the permission box, check if they have permissions, if they do
     *          if they do, run the location listener to get city names
     * */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (checkLocationPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UpdateTime * 1000, 0, locationListener);
        }
    }
}