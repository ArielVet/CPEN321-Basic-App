package com.arielvet.cpen321milestone1;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class MainActivity extends AppCompatActivity {


    // Button Declaration
    private Button favouriteCity;
    private Button phoneDetails;
    private Button serverInfo;
    private Button surprise;

    // Google Sign In Variables
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Buttons click handlers */

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

                Intent serverInfo = new Intent(MainActivity.this, ServerInfo.class);
                startActivity(serverInfo);

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