package com.example.mlseriesdemonstrator.activities.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import java.util.List;

public class EmergencyAttendanceActivity extends AppCompatActivity {

  private static final String TAG = "EmergencyAttendanceActivity";
  private RecyclerView startedEvents;
  private EventAdapter eventAdapter;
  private EditText searchBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_emergency_attendance);

    startedEvents = findViewById(R.id.STARTED_EVENTS_ADMIN);
    searchBox = findViewById(R.id.SEARCH_EVENT);

    // Create the EventAdapter and set it to the RecyclerView
    EventManager.getStartedEvents(this, events -> {
      if (!events.isEmpty()) {
        eventAdapter = new EventAdapter(this, events, 1);
        startedEvents.setLayoutManager(new LinearLayoutManager(this));
        startedEvents.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
      } else {
        Log.d(TAG, "No events found");
      }
    });

    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

      @Override
      public void afterTextChanged(Editable editable) {
        if (eventAdapter != null) {
          eventAdapter.filterEvents(editable.toString());
          Log.d(TAG, "hi");
        }
      }
    });
  }
}
