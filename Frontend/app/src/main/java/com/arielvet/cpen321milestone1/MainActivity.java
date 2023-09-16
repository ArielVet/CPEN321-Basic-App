package com.arielvet.cpen321milestone1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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
            Intent phoneDetailsIntent = new Intent(MainActivity.this, PhoneDetails.class);
            startActivity(phoneDetailsIntent);
        });

        // Button 3: Requests a user logs into Google and then displays some data
        Button serverInfo = findViewById(R.id.server_info_button);
        serverInfo.setOnClickListener(view -> {
            // Launch the Google Maps Activity
            Intent serverInfoIntent = new Intent(MainActivity.this, ServerInfo.class);
            startActivity(serverInfoIntent);
        });

        // Button 4: Surprise Button is a Colour Game
        Button game = findViewById(R.id.colour_game_button);
        game.setOnClickListener(view -> {
            // Launch the Google Maps Activity
            Intent colourGame = new Intent(MainActivity.this, ColourGame.class);
            startActivity(colourGame);
        });
    }


}