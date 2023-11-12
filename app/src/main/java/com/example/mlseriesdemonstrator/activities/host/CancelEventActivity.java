package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;

import java.util.List;

public class CancelEventActivity extends AppCompatActivity {

  private static final String TAG = "CancelEventActivity";
  Context context;
  RecyclerView eventsRecyclerView;
  Button cancelBtn;
  EditText searchTxt;
  EventAdapter eventAdapter;
  List<Event> allEvents;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cancel_event);

    context = CancelEventActivity.this;
    cancelBtn = findViewById(R.id.BACK_BTN);
    searchTxt = findViewById(R.id.SEARCH_EVENT);
    eventsRecyclerView = findViewById(R.id.CHOOSE_EVENTS_TO_CANCEL);

    EventManager.getUpcomingStartedEvents(context, events -> {
      allEvents = events; // Storing all events for filtering
      if (!events.isEmpty()) {
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, events);
        eventsRecyclerView.setAdapter(eventAdapter);
      } else {
        Log.d(TAG, "No events found");
      }
    });

    cancelBtn.setOnClickListener(v -> finish());

    searchTxt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

      @Override
      public void afterTextChanged(Editable editable) {
        if (eventAdapter != null) {
          eventAdapter.filterEvents(editable.toString());
        }
      }
    });
  }
}
