package com.arielvet.cpen321milestone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;


public class PhoneDetails extends AppCompatActivity {

    /* Constant */
    private String DEFAULT;

    /* Text Elements */
    private TextView cityCap;
    private String cityCap_text;

    /* Extra Objects */
    private locationServices locService;

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_details);

        //Saves TextView elements to variables
        cityCap = findViewById(R.id.current_city_text);
        TextView manufacturerCap = findViewById(R.id.manuf_text);
        TextView modelCap = findViewById(R.id.model_text);

        //Get the default TEXT
        DEFAULT = getString(R.string.default_value);

        // Location service declaration with a callback to say what happens
        // when we find a valid city and when we don't
        locService = new locationServices(this, new locationServices.Callback() {
            @Override
            public void setValid() {
                cityCap.setText(locService.getCity());
            }

            @Override
            public void setInvalid() {
                cityCap.setText(DEFAULT);
            }
        });
        locService.activateLocationUpdater();

        //Set Manufacturer and Phone Model Captions
        manufacturerCap.setText(android.os.Build.MANUFACTURER);
        modelCap.setText(android.os.Build.MODEL);

    }

    /**
     * Purpose: Once the user interacts with the permission box, check if they have permissions, if they do
     *          if they do, run the location listener to get city names
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locService.activateLocationUpdater();
    }
}