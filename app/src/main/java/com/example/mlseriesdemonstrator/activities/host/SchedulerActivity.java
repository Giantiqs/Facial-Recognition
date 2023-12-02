package com.example.mlseriesdemonstrator.activities.host;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.dialogs.CourseDepartmentDialog;
import com.example.mlseriesdemonstrator.geofence.MapsActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.Location;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Objects;

public class SchedulerActivity extends AppCompatActivity implements CourseDepartmentDialog.CourseDepartmentListener {

  Context context;
  EditText eventTitleTxt;
  EditText eventDateTxt;
  EditText eventStartTimeTxt;
  EditText eventModeTxt;
  EditText locationTxt;
  EditText courseAndDeptTxt;
  Button scheduleEventBtn;
  private User user;
  private int hour;
  private int minute;
  private String selectedDepartment;
  private String selectedCourse;
  private Location selectedLocation;
  private String selectedMode;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scheduler);

    context = SchedulerActivity.this;
    eventTitleTxt = findViewById(R.id.EVENT_TITLE);
    eventDateTxt = findViewById(R.id.EVENT_DATE);
    eventStartTimeTxt = findViewById(R.id.EVENT_TIME);
    eventModeTxt = findViewById(R.id.EVENT_MODE);
    locationTxt = findViewById(R.id.EVENT_LOCATION);
    courseAndDeptTxt = findViewById(R.id.DEPARTMENT_AND_COURSE);
    scheduleEventBtn = findViewById(R.id.SET_EVENT);

    user = Utility.getUser();

    eventDateTxt.setOnClickListener(v -> selectDate());

    eventStartTimeTxt.setOnClickListener(v -> selectTime());

    eventModeTxt.setOnClickListener(v -> selectMode());

    locationTxt.setOnClickListener(v -> {
      Intent intent = new Intent(context, MapsActivity.class);
      mapsActivityLauncher.launch(intent);
    });

    courseAndDeptTxt.setOnClickListener(v -> openDeptAndCourseDialog());

    scheduleEventBtn.setOnClickListener(v -> confirmSchedule());
  }

  private final ActivityResultLauncher<Intent> mapsActivityLauncher = registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
              Intent data = result.getData();
              if (data != null) {
                String locationName = data.getStringExtra("location_name");
                double longitude = data.getDoubleExtra("longitude", 0);
                double latitude = data.getDoubleExtra("latitude", 0);
                float locationRadius = data.getFloatExtra("location_geofence_radius", 150);

                LatLng latLng = new LatLng(latitude, longitude);

                // Check if the location data is not null
                if (locationName != null) {
                  // Update the locationTxt with the selected location name
                  locationTxt.setText(locationName);

                  // You can also store the selected location data in selectedLocation
                  selectedLocation = new Location(locationName, latLng, locationRadius);
                }
              }
            }
          }
  );

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
            android.R.style.Theme_Holo_Dialog,
            onTimeSetListener,
            hour,
            minute,
            true
    );

    Objects.requireNonNull(
            timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)
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
            android.R.style.Theme_Holo_Dialog,
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
      locationTxt.setError("This field is required");
      return;
    }

    Event event = new Event(
            eventTitleStr,
            eventDateStr,
            eventStartTimeStr,
            selectedLocation,
            hostId,
            "upcoming",
            "",
            selectedDepartment,
            selectedCourse
    );

    AlertDialog.Builder builder = new AlertDialog.Builder(context);

    builder.setTitle(Html.fromHtml("<font color='#8ab4f8'>Schedule this event?</font>"));
    builder.setMessage(Html.fromHtml("<font color='#8ab4f8'>Are you sure you want to schedule this event?</font>"));


    builder.setPositiveButton(Html.fromHtml("<font color='#8ab4f8'>Yes</font>"), (dialog1, which) -> {
      EventManager.scheduleEvent(event, context);
      finish();
      dialog1.dismiss();
    });
    builder.setNegativeButton(Html.fromHtml("<font color='#8ab4f8'>No</font>"), (dialog12, which) -> dialog12.dismiss());

    AlertDialog dialog = builder.create();

    Objects.requireNonNull(
            dialog.getWindow())
            .setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#162c46")
                    )
    );

    dialog.show();
  }

  @Override
  public void applyTexts(String department, String course) {

    String selectedTxt = department + " - " + course;
    selectedDepartment = department;
    selectedCourse = course;
    courseAndDeptTxt.setText(selectedTxt);
  }

  private void selectMode() {
    String[] modes = { "Online", "On-site" };

    AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
    builder.setTitle("Select mode (Online or On-site)");
    builder.setItems(modes, (dialog, which) -> {
      selectedMode = modes[which];
      eventModeTxt.setText(selectedMode);

      if ( selectedMode.equals("Online") ) {
        final String ONLINE_EVENT = "Online Event";
        LatLng latLng = new LatLng(0, 0);
        locationTxt.setText(ONLINE_EVENT);


        selectedLocation = new Location(ONLINE_EVENT, latLng, 0);
      } else {
        locationTxt.setText("");
      }
    });

    AlertDialog dialog = builder.create();
    dialog.show();
  }
}