package com.example.mlseriesdemonstrator.activities.host;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class SchedulerActivity extends AppCompatActivity {

    Context context;
    EditText eventTitleTxt;
    EditText eventDateTxt;
    EditText eventStartTime;
    EditText locationTxt;
    Button scheduleEventBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        context = SchedulerActivity.this;
        eventTitleTxt = findViewById(R.id.EVENT_TITLE);
        eventDateTxt = findViewById(R.id.EVENT_DATE);
        eventStartTime = findViewById(R.id.EVENT_TIME);
        locationTxt = findViewById(R.id.EVENT_LOCATION);
        scheduleEventBtn = findViewById(R.id.SET_EVENT);
        user = Utility.getUser();

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
        String hostId = user.getInstitutionalID();

        if (eventTitleStr.isEmpty()) {
            eventTitleTxt.setError("This field is required");
            return;
        }

        if (eventDateStr.isEmpty()) {
            eventDateTxt.setError("This field is required");
            return;
        }

        if (eventStartTimeStr.isEmpty()) {
            eventStartTime.setError("This field is required");
            return;
        }

        if (locationStr.isEmpty()) {
            locationTxt.setError("This field is required");
            return;
        }

        Event event = new Event(
                eventTitleStr,
                eventDateStr,
                eventStartTimeStr,
                locationStr,
                hostId,
                null
        );

        EventManager.scheduleEvent(event, context);

        EventManager.getEventsByHostId(hostId, context, events -> {
            Log.d("events of host id: " + hostId, events.get(0).getTitle());
        });
    }
}