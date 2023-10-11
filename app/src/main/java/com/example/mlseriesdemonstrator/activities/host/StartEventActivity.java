package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.utilities.EventManager;

public class StartEventActivity extends AppCompatActivity {

  private static final String TAG = "StartEventActivity";
  RecyclerView eventsRecyclerView;
  Button backBtn;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_event);

    eventsRecyclerView = findViewById(R.id.CHOOSE_EVENTS_TO_START);
    backBtn = findViewById(R.id.BACK_BTN);
    context = StartEventActivity.this;

    EventManager.getNearestEvents(context, events -> {
      if (!events.isEmpty()) {
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventsRecyclerView.setAdapter(new EventAdapter(context, events));
      } else {
        Log.d(TAG, "No events found");
      }
    });

    backBtn.setOnClickListener(v -> finish());
  }
}