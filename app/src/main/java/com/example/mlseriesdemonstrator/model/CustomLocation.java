package com.example.mlseriesdemonstrator.model;

public class CustomLocation {
  private double latitude;
  private double longitude;

  public CustomLocation() {

  }

  public CustomLocation(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
