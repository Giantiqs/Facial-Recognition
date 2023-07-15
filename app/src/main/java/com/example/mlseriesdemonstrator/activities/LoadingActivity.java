package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.classes.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class LoadingActivity extends AppCompatActivity implements Utility.LoadingCompleteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Utility.setUserDetails(this);
    }

    @Override
    public void onLoadingComplete(User user) {
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        finish();
    }
}