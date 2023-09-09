package com.arielvet.cpen321milestone1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

        // Button 1: Puts a Pin on world map of my favourite City
        favouriteCity = findViewById(R.id.favourite_city_button);
        favouriteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Google Maps Activity
                Intent cityOnMap = new Intent(MainActivity.this, FaveCity.class);
                startActivity(cityOnMap);
            }
        });

        // Button 2: Get Phone Details such as location, manufacturer, phone model
        phoneDetails = findViewById(R.id.phone_details_button);
        phoneDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Google Maps Activity
                Intent phoneDetails = new Intent(MainActivity.this, PhoneDetails.class);
                startActivity(phoneDetails);
            }
        });

        // Button 3: Requests a user logs into Google and then displays some data
        serverInfo = findViewById(R.id.server_info_button);
        serverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Google Maps Activity
                Intent serverInfo = new Intent(MainActivity.this, ServerInfo.class);
                startActivity(serverInfo);
            }
        });

        //TODO Button 4
        surprise = findViewById(R.id.surprise_button);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Surprise");
            }
        });
    }


}