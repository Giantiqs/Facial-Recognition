package com.example.mlseriesdemonstrator.activities.host;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.tests.MapsActivity;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.Calendar;
import java.util.Objects;

public class SchedulerActivity extends AppCompatActivity {

    Context context;
    EditText eventTitleTxt;
    EditText eventDateTxt;
    EditText eventStartTime;
    EditText locationTxt;
    Button scheduleEventBtn;
    private User user;
    private int hour;
    private int minute;

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

        eventDateTxt.setOnClickListener(v -> selectDate());

        eventStartTime.setOnClickListener(v -> selectTime());

        locationTxt.setOnClickListener(v -> {
            startActivity(new Intent(
                    context,
                    MapsActivity.class
            ));
        });

        scheduleEventBtn.setOnClickListener(v -> confirmSchedule());
    }

    private void selectTime() {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute1) -> {
            hour = hourOfDay;
            minute = minute1;
            String hourStr = String.valueOf(hour);
            String minuteStr = (minute < 10) ? "0" + minute : String.valueOf(minute);
            String startTimeStr = hourStr + ":" + minuteStr;

            eventStartTime.setText(startTimeStr);
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog,
                onTimeSetListener,
                hour,
                minute,
                true
        );

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void selectDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, month1, dayOfMonth) -> {
            String date = year1 + "/" + (month1 + 1) + "/" + dayOfMonth;
            eventDateTxt.setText(date);
        };

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog,
                dateSetListener,
                year,
                month,
                day
        );

        Objects.requireNonNull(
                dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)
        );

        dialog.setTitle("Select date");

        dialog.show();
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
//            return;
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
    }
}