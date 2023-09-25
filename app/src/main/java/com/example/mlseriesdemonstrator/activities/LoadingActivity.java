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
        Utility.setUserDetails(this);
        context = LoadingActivity.this;
    }

    @Override
    public void onLoadingComplete(User user) {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }
}