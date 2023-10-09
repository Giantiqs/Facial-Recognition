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
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.dialogs.CourseDepartmentDialog;
import com.example.mlseriesdemonstrator.tests.MapsActivity;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.Calendar;
import java.util.Objects;

public class SchedulerActivity extends AppCompatActivity implements CourseDepartmentDialog.CourseDepartmentListener {

  private static final String TAG = "SchedulerActivity";
  Context context;
  EditText eventTitleTxt;
  EditText eventDateTxt;
  EditText eventStartTimeTxt;
  EditText locationTxt;
  EditText courseAndDeptTxt;
  Button scheduleEventBtn;
  private User user;
  private int hour;
  private int minute;
  private String selectedDepartment;
  private String selectedCourse;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scheduler);

    context = SchedulerActivity.this;
    eventTitleTxt = findViewById(R.id.EVENT_TITLE);
    eventDateTxt = findViewById(R.id.EVENT_DATE);
    eventStartTimeTxt = findViewById(R.id.EVENT_TIME);
    locationTxt = findViewById(R.id.EVENT_LOCATION);
    courseAndDeptTxt = findViewById(R.id.DEPARTMENT_AND_COURSE);
    scheduleEventBtn = findViewById(R.id.SET_EVENT);

    user = Utility.getUser();

    eventDateTxt.setOnClickListener(v -> selectDate());

    eventStartTimeTxt.setOnClickListener(v -> selectTime());

    locationTxt.setOnClickListener(v -> startActivity(new Intent(
            context,
            MapsActivity.class
    )));

    courseAndDeptTxt.setOnClickListener(v -> openDeptAndCourseDialog());

    scheduleEventBtn.setOnClickListener(v -> confirmSchedule());
  }

  private void openDeptAndCourseDialog() {

    CourseDepartmentDialog dialog = new CourseDepartmentDialog();

    dialog.show(getSupportFragmentManager(), "Course and Department Dialog");
  }

  private void selectTime() {

    TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute1) -> {
      hour = hourOfDay;
      minute = minute1;
      String hourStr = (hour < 10) ? "0" + hour : String.valueOf(hour);
      String minuteStr = (minute < 10) ? "0" + minute : String.valueOf(minute);
      String startTimeStr = hourStr + ":" + minuteStr;

      eventStartTimeTxt.setText(startTimeStr);
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
    String eventStartTimeStr = eventStartTimeTxt.getText().toString();
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
      eventStartTimeTxt.setError("This field is required");
      return;
    }

    if (locationStr.isEmpty()) {
//      locationTxt.setError("This field is required");
//      return;
    }

    Event event = new Event(
            eventTitleStr,
            eventDateStr,
            eventStartTimeStr,
            "test location",
            hostId,
            "upcoming",
            "",
            selectedDepartment,
            selectedCourse
    );

    EventManager.scheduleEvent(event, context);
    finish();
  }

  @Override
  public void applyTexts(String department, String course) {

    String selectedTxt = department + " - " + course;
    selectedDepartment = department;
    selectedCourse = course;
    courseAndDeptTxt.setText(selectedTxt);
  }
}