package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class LoadingActivity extends AppCompatActivity implements Utility.LoadingCompleteListener {

  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);

    // Wait for the data that the firestore will send and set the user details.
    Utility.setUserDetails(this);
    // Set the content of this screen.
    context = LoadingActivity.this;
  }

  @Override
  public void onLoadingComplete(User user) {
    // When the user has been fetched from the firestore database, go to the main screen.
    startActivity(new Intent(context, MainActivity.class));
    finish();
  }
}