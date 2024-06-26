package com.arielvet.cpen321milestone1;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.arielvet.cpen321milestone1.databinding.ActivityFaveCityBinding;

import java.util.Objects;

public class FaveCity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFaveCityBinding binding = ActivityFaveCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Purpose: Function triggers when the map is ready to use. It Drops a pin on my favourite city
     * @param googleMap : a Google Maps Object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng zurich = new LatLng(47.3769, 8.5417);
        Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(zurich)
                .title("My Favourite City: Zurich"))).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zurich, 10));
    }
}