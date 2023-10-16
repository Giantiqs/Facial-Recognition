package com.example.mlseriesdemonstrator.model;

import androidx.fragment.app.Fragment;

public class Options {

  String message;
  Fragment fragment;

  public Options(String message, Fragment fragment) {
    this.message = message;
    this.fragment = fragment;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Fragment getFragment() {
    return fragment;
  }

  public void setFragment(Fragment fragment) {
    this.fragment = fragment;
  }
}
