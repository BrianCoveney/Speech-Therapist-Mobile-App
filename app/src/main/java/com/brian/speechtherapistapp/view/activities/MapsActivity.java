package com.brian.speechtherapistapp.view.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.view.activities.googlemaps.GetNearbyPlacesData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        //Data retrieved from LocationActivity
        SharedPreferences sharedPreferences =
                getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);

        //storing double values in SharedPreferences without losing precision
        latitude = Double.longBitsToDouble(sharedPreferences.getLong("latitude_key", 0));
        longitude = Double.longBitsToDouble(sharedPreferences.getLong("longitude_key", 0));


        // Add a marker in Current Location and move the camera
        LatLng mLocation = new LatLng(latitude, longitude);
        this.googleMap.addMarker(new MarkerOptions().position(mLocation).title("Marker in current location"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        // Zoom in, animating the camera.
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @OnClick({R.id.btn_hospitals})
    public void onButtonClick(Button button) {

        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch (button.getId()) {
            case R.id.btn_hospitals:
                googleMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital); // Pass our lat long to the url
                dataTransfer[0] = googleMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCLKM52wTT4hpszalb-yWP_7i8-6wKK5pw");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }
}
