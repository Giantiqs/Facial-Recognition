package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;

public class SchedulerActivity extends AppCompatActivity {

    EditText eventTitleTxt;
    EditText eventDateTxt;
    EditText eventStartTime;
    EditText locationTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        eventTitleTxt = findViewById(R.id.EVENT_TITLE);
        eventDateTxt = findViewById(R.id.EVENT_DATE);
        eventStartTime = findViewById(R.id.EVENT_TIME);
        locationTxt = findViewById(R.id.EVENT_LOCATION);

        locationTxt.setOnClickListener(v -> {
            // Add geofence functions
        });

    }
}