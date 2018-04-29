package com.brian.speechtherapistapp.view.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.brian.speechtherapistapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Date retreived from LocationActivity
        SharedPreferences sharedPreferences = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);

        //storing double values in SharedPreferences without losing precision
        double myLat = Double.longBitsToDouble(sharedPreferences.getLong("latitude_key", 0));
        double myLong = Double.longBitsToDouble(sharedPreferences.getLong("longitude_key", 0));


        // Add a marker in Current Location and move the camera
        LatLng mLocation = new LatLng(myLat, myLong);
        mMap.addMarker(new MarkerOptions().position(mLocation).title("Marker in current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        
    }
}
