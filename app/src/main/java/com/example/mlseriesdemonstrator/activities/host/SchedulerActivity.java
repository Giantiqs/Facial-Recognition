package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;

public class SchedulerActivity extends AppCompatActivity {

    EditText eventTitleTxt;
    EditText eventDateTxt;
    EditText eventStartTime;
    EditText locationTxt;
    Button scheduleEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        eventTitleTxt = findViewById(R.id.EVENT_TITLE);
        eventDateTxt = findViewById(R.id.EVENT_DATE);
        eventStartTime = findViewById(R.id.EVENT_TIME);
        locationTxt = findViewById(R.id.EVENT_LOCATION);
        scheduleEventBtn = findViewById(R.id.SCHEDULE_EVENT);

        locationTxt.setOnClickListener(v -> {
            // Add geofence functions
        });

        scheduleEventBtn.setOnClickListener(v -> confirmSchedule());
    }

    private void confirmSchedule() {

        String eventTitleStr = eventTitleTxt.getText().toString();
        String eventDateStr = eventDateTxt.getText().toString();
        String eventStartTimeStr = eventStartTime.getText().toString();
        String locationStr = locationTxt.getText().toString();

        Event event = new Event(
                eventTitleStr,
                eventDateStr,
                eventStartTimeStr,
                locationStr
        );

        Log.d("test", String.format(
                "%s %s %s %s",
                event.getTitle(),
                event.getDate(),
                event.getStartTime(),
                event.getLocation()
                )
        );
    }
}