package com.example.mlseriesdemonstrator.tests;

import com.google.type.LatLng;

public class Location {

  private String locationAddress;
  private LatLng latLng;
  private float geofenceRadius;

  public Location () {

  }

  public Location (String locationAddress, LatLng latLng, float geofenceRadius) {
    this.locationAddress = locationAddress;
    this.latLng = latLng;
    this.geofenceRadius = geofenceRadius;
  }

  public String getLocationAddress() {
    return locationAddress;
  }

  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
  }

  public float getGeofenceRadius() {
    return geofenceRadius;
  }

  public void setGeofenceRadius(float geofenceRadius) {
    this.geofenceRadius = geofenceRadius;
  }
}
