package com.arielvet.cpen321milestone1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // TODO remove this
    /**
     * List of chatGPT uses:
     *  - help identify TextView.setText() to override existing text
     *  - identify onRequestPermissionsResult as a method to detect when the user interacted with the permission window
     * - teahc how to use callbacks
     * - teach how to use requestsingleupdate
     */


    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button 1: Puts a Pin on world map of my favourite City
        Button favouriteCity = findViewById(R.id.favourite_city_button);
        favouriteCity.setOnClickListener(view -> {
            // Launch the Google Maps Activity
            Intent cityOnMap = new Intent(MainActivity.this, FaveCity.class);
            startActivity(cityOnMap);
        });

        // Button 2: Get Phone Details such as location, manufacturer, phone model
        Button phoneDetails = findViewById(R.id.phone_details_button);
        phoneDetails.setOnClickListener(view -> {
            // Launch the Google Maps Activity
            Intent phoneDetails1 = new Intent(MainActivity.this, PhoneDetails.class);
            startActivity(phoneDetails1);
        });

        // Button 3: Requests a user logs into Google and then displays some data
        Button serverInfo = findViewById(R.id.server_info_button);
        serverInfo.setOnClickListener(view -> {
            // Launch the Google Maps Activity
            Intent serverInfo1 = new Intent(MainActivity.this, ServerInfo.class);
            startActivity(serverInfo1);
        });

        //TODO Button 4
        Button surprise = findViewById(R.id.surprise_button);
        surprise.setOnClickListener(view -> Log.d(TAG, "Surprise"));
    }


}