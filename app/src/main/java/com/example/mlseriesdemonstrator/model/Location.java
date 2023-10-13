package com.example.mlseriesdemonstrator.model;


import com.example.mlseriesdemonstrator.model.CustomLocation;
import com.google.android.gms.maps.model.LatLng;

public class Location {

  private String locationAddress;

  private CustomLocation customLatLng;
  private float geofenceRadius;

  public Location () {

  }

  public Location (String locationAddress, LatLng latLng, float geofenceRadius) {
    this.locationAddress = locationAddress;

    this.customLatLng = new CustomLocation();
    this.customLatLng.setLongitude(latLng.longitude);
    this.customLatLng.setLatitude(latLng.latitude);

    this.geofenceRadius = geofenceRadius;
  }

  public String getLocationAddress() {
    return locationAddress;
  }

  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  public float getGeofenceRadius() {
    return geofenceRadius;
  }

  public void setGeofenceRadius(float geofenceRadius) {
    this.geofenceRadius = geofenceRadius;
  }

  public CustomLocation getCustomLatLng() {
    return customLatLng;
  }

  public void setCustomLatLng(CustomLocation customLatLng) {
    this.customLatLng = customLatLng;
  }
}
