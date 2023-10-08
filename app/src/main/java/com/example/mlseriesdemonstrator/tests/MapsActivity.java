package com.example.mlseriesdemonstrator.tests;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.databinding.ActivityMapsBinding;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    Button increaseGeofence;
    Button reduceGeofence;
    TextView radiusTxt;
    ActivityMapsBinding binding;
    private GoogleMap mMap;
    private GeoFenceHelper geoFenceHelper;
    private static final String TAG = "MapsActivity";
    private GeofencingClient geofencingClient;
    private Context context;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private float GEO_FENCE_RADIUS;
    private final String GEO_FENCE_ID = "TEST_ID";
    private final int MAX_GEOFENCE_RADIUS = 400;
    private final int MIN_GEOFENCE_RADIUS = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = MapsActivity.this;
        increaseGeofence = findViewById(R.id.INCREASE_SIZE);
        reduceGeofence = findViewById(R.id.REDUCE_SIZE);
        radiusTxt = findViewById(R.id.RADIUS);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFenceHelper = new GeoFenceHelper(this);

        GEO_FENCE_RADIUS = 150;

        radiusTxt.setText(String.valueOf(GEO_FENCE_RADIUS));

        increaseGeofence.setOnClickListener(v -> {
            if (GEO_FENCE_RADIUS <= MAX_GEOFENCE_RADIUS) {
                GEO_FENCE_RADIUS+=10;
                Utility.showToast(context, "Size increased");
                radiusTxt.setText(String.valueOf(GEO_FENCE_RADIUS));
            } else
                Utility.showToast(context, "Reached maximum geofence radius");
        });

        reduceGeofence.setOnClickListener(v -> {
            if (GEO_FENCE_RADIUS >= MIN_GEOFENCE_RADIUS) {
                GEO_FENCE_RADIUS-=10;
                Utility.showToast(context, "Size reduced");
                radiusTxt.setText(String.valueOf(GEO_FENCE_RADIUS));
            } else
                Utility.showToast(context, "Reached minimum geofence radius");
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable the display of the user's location on the map
            mMap.setMyLocationEnabled(true);

            // Get the user's last known location
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    // Create a LatLng object with the user's location
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Move the camera to the user's current location
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                }
            });
        } else {
            enableUserLocation();
        }

        mMap.setOnMapLongClickListener(this);
    }


    private void enableUserLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                );
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {

            }
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEO_FENCE_RADIUS);
        addGeofence(latLng, GEO_FENCE_RADIUS);
    }

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geoFenceHelper.getGeofence(
                GEO_FENCE_ID,
                latLng,
                radius,
                Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL |
                        Geofence.GEOFENCE_TRANSITION_EXIT
        );
        GeofencingRequest geofencingRequest = geoFenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geoFenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Geofence has been added");
                }).addOnFailureListener(e -> {
                    String errMsg = geoFenceHelper.getErrStr(e);
                    Log.d(TAG, "onFailure: " + errMsg);
                });
    }

    private void addMarker(LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {

        CircleOptions circleOptions = new CircleOptions();

        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);

        mMap.addCircle(circleOptions);
    }
}