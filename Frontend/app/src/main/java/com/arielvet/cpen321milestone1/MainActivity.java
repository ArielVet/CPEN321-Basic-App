package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Button Declaration
    private Button favouriteCity;
    private Button phoneDetails;
    private Button serverInfo;
    private Button surprise;

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button 1: Puts pin on mpa of favourite City
        favouriteCity = findViewById(R.id.favourite_city_button);
        favouriteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "My Fav City");

                Intent cityOnMap = new Intent(MainActivity.this, FaveCity.class);
                startActivity(cityOnMap);
            }
        });

        // Button 2
        phoneDetails = findViewById(R.id.phone_details_button);
        phoneDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "My Phone Details");

                Intent phoneDetails = new Intent(MainActivity.this, PhoneDetails.class);
                startActivity(phoneDetails);
            }
        });

        // Button 3
        serverInfo = findViewById(R.id.server_info_button);
        serverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Server Info");
            }
        });

        //Button 4
        surprise = findViewById(R.id.surprise_button);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Surprise");
            }
        });
    }



}