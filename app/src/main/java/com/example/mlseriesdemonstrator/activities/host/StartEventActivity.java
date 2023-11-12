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
import com.example.mlseriesdemonstrator.utilities.EventManager;

public class StartEventActivity extends AppCompatActivity {

  //meow
  private static final String TAG = "StartEventActivity";
  RecyclerView eventsRecyclerView;
  Button backBtn;
  Context context;
  EditText searchTxt;
  EventAdapter eventAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_event);

    eventsRecyclerView = findViewById(R.id.CHOOSE_EVENTS_TO_START);
    backBtn = findViewById(R.id.BACK_BTN);
    context = StartEventActivity.this;
    searchTxt = findViewById(R.id.SEARCH_EVENT);


    EventManager.getNearestUpcomingEvents(context, events -> {
      if (!events.isEmpty()) {
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, events);
        eventsRecyclerView.setAdapter(eventAdapter);
      } else {
        Log.d(TAG, "No events found");
      }
    });

    backBtn.setOnClickListener(v -> finish());

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