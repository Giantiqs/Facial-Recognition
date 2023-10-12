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

public class CancelEventActivity extends AppCompatActivity {

  //meow
  private static final String TAG = "CancelEventActivity";
  Context context;
  RecyclerView eventsRecyclerView;
  Button cancelBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cancel_event);

    context = CancelEventActivity.this;
    cancelBtn = findViewById(R.id.BACK_BTN);

    eventsRecyclerView = findViewById(R.id.CHOOSE_EVENTS_TO_CANCEL);

    EventManager.getNearestUpcomingEvents(context, events -> {
      if (!events.isEmpty()) {
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventsRecyclerView.setAdapter(new EventAdapter(context, events));
      } else {
        Log.d(TAG, "No events found");
      }
    });

    cancelBtn.setOnClickListener(v -> finish());
  }
}