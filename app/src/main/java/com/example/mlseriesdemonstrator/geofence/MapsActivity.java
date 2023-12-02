package com.example.mlseriesdemonstrator.geofence;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.AddressAdapter;
import com.example.mlseriesdemonstrator.databinding.ActivityMapsBinding;
import com.example.mlseriesdemonstrator.model.Location;
import com.example.mlseriesdemonstrator.model.SearchedAddress;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

  Button increaseGeofence;
  Button reduceGeofence;
  Button addGeofence;
  TextView radiusTxt;
  ActivityMapsBinding binding;
  EditText searchLocationTxt;
  ImageButton searchLocationBtn;
  private GoogleMap mMap;
  private GeoFenceHelper geoFenceHelper;
  private static final String TAG = "MapsActivity";
  private GeofencingClient geofencingClient;
  private Context context;
  private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
  private float GEO_FENCE_RADIUS;
  private final int MAX_GEOFENCE_RADIUS = 400;
  private final int MIN_GEOFENCE_RADIUS = 50;
  final String GEO_FENCE_ID = "TEST_ID";
  private LatLng selectedLatLng;
  private Location selectedLocation;
  private Dialog dialog;
  private long geofenceExpirationDuration = Geofence.NEVER_EXPIRE;
  RecyclerView searchedAddress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMapsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    context = MapsActivity.this;
    increaseGeofence = findViewById(R.id.INCREASE_SIZE);
    reduceGeofence = findViewById(R.id.REDUCE_SIZE);
    radiusTxt = findViewById(R.id.RADIUS);
    addGeofence = findViewById(R.id.ADD_GEOFENCE);
    searchLocationBtn = findViewById(R.id.SEARCH_LOC_BTN);
    searchLocationTxt = findViewById(R.id.SEARCH_LOCATION_TXT);
    searchedAddress = findViewById(R.id.ADDRESS_RECV);

    AddressAdapter addressAdapter = new AddressAdapter(getApplicationContext(), new ArrayList<>());
    searchedAddress.setAdapter(addressAdapter);

    addressAdapter.setTextListener(selectedAddress -> {
      searchLocationTxt.setText(selectedAddress);
    });

    searchLocationTxt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        String searchText = editable.toString().trim();

        if (!searchText.isEmpty()) {
          // Perform address search and update the addresses list
          ArrayList<SearchedAddress> addresses = searchNearestAddresses(searchText);


          searchedAddress.setLayoutManager(new LinearLayoutManager(context));
          searchedAddress.setAdapter(new AddressAdapter(getApplicationContext(), addresses));
        } else {
          // If the search text is empty, clear the addresses list and RecyclerView
          ArrayList<SearchedAddress> emptyAddresses = new ArrayList<>();
          searchedAddress.setLayoutManager(new LinearLayoutManager(context));
          searchedAddress.setAdapter(new AddressAdapter(getApplicationContext(), emptyAddresses));
        }
      }
    });

    searchLocationBtn.setOnClickListener(v -> moveToLocation());

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
      if (GEO_FENCE_RADIUS < MAX_GEOFENCE_RADIUS) {
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

    dialog = new Dialog(context);
    dialog.setContentView(R.layout.yes_no_dialog_view);
    Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialog.setCancelable(false);

    Button yesBtn = dialog.findViewById(R.id.YES_BTN);
    Button noBtn = dialog.findViewById(R.id.NO_BTN);
    TextView eventTitleTxt = dialog.findViewById(R.id.EVENT_TITLE);
    TextView promptTxt = dialog.findViewById(R.id.PROMPT_TXT);

    modifyTexts(promptTxt, eventTitleTxt);

    yesBtn.setOnClickListener(v1 -> {
      String locationName = getLocationName(selectedLatLng);

      selectedLocation = new Location(locationName, selectedLatLng, GEO_FENCE_RADIUS);

      // Set the geofence expiration duration, e.g., 24 hours (86400000 milliseconds)
      geofenceExpirationDuration = 10000;

      // Create an intent to return the location data to the previous activity
      Intent resultIntent = new Intent();
      resultIntent.putExtra("location_name", selectedLocation.getLocationAddress());
      resultIntent.putExtra("longitude", selectedLocation.getCustomLatLng().getLongitude());
      resultIntent.putExtra("latitude", selectedLocation.getCustomLatLng().getLatitude());
      resultIntent.putExtra("location_geofence_radius", selectedLocation.getGeofenceRadius());
      // Set the result with the location data and finish the activity
      setResult(RESULT_OK, resultIntent);

      // Add the geofence with the dynamic expiration duration
      addGeofence(selectedLatLng, GEO_FENCE_RADIUS, geofenceExpirationDuration);

      finish();
      dialog.dismiss(); // Dismiss the dialog when finished
    });


    noBtn.setOnClickListener(v1 -> dialog.dismiss());

    // Handle the click event for the "Add Geofence" button
    addGeofence.setOnClickListener(v -> {
      if (!isFinishing()) { // Check if the activity is still active
        dialog.show();
      }
    });
  }

  // Update the method to return a list of SearchedAddress
  private ArrayList<SearchedAddress> searchNearestAddresses(String searchText) {

    Geocoder geocoder = new Geocoder(context);
    List<Address> addressList;

    try {
      addressList = geocoder.getFromLocationName(searchText, 5);
      ArrayList<SearchedAddress> searchedAddresses = new ArrayList<>();

      assert addressList != null;
      int maxResults = Math.min(5, addressList.size());

      for (int i = 0; i < maxResults; i++) {
        Address address = addressList.get(i);

        SearchedAddress searchedAddress = new SearchedAddress(address.getAddressLine(0));

        searchedAddresses.add(searchedAddress);
      }

      return searchedAddresses;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }


  private void moveToLocation() {
    Log.d(TAG, "moveToLocation");

    String locationStr = searchLocationTxt.getText().toString();

    if (locationStr.isEmpty()) {
      Log.d(TAG, "Location string is empty");
      return;
    }

    Geocoder geocoder = new Geocoder(context);
    List<Address> addresses;

    try {
      addresses = geocoder.getFromLocationName(locationStr, 1);

      assert addresses != null;
      if (!addresses.isEmpty()) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        mMap.clear(); // Clear existing markers
        mMap.addMarker(new MarkerOptions().position(latLng).title(locationStr));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

      } else {
        Log.d(TAG, "No addresses found for the location: " + locationStr);
      }

    } catch (IOException e) {
      Log.e(TAG, "Geocoding failed: " + e.getLocalizedMessage());
    }
  }

  private String getLocationName(LatLng latLng) {
    Geocoder geocoder = new Geocoder(this);
    List<Address> addresses;

    try {
      addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
      assert addresses != null;
      if (!addresses.isEmpty()) {
        return addresses.get(0).getAddressLine(0);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return ""; // Return an empty string if location name couldn't be determined.
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
      }
    }
  }

  @Override
  public void onMapLongClick(@NonNull LatLng latLng) {
    selectedLatLng = latLng;
    mMap.clear();
    addMarker(latLng);
    addCircle(latLng, GEO_FENCE_RADIUS);
    addGeofence(latLng, GEO_FENCE_RADIUS, geofenceExpirationDuration);
  }

  private void modifyTexts(TextView promptTxt, TextView eventTitleTxt) {

    String prompt = "Set this as Geofence?";

    promptTxt.setText(prompt);
    eventTitleTxt.setVisibility(View.GONE);
  }

  private void addGeofence(LatLng latLng, float radius, long expirationDuration) {

    Geofence geofence = geoFenceHelper.getGeofence(
            GEO_FENCE_ID,
            latLng,
            radius,
            Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_DWELL |
                    Geofence.GEOFENCE_TRANSITION_EXIT,
            expirationDuration  // Pass the expiration duration
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
            .addOnSuccessListener(unused -> Log.d(TAG, "Geofence has been added"))
            .addOnFailureListener(e -> {
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