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
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class EndEventActivity extends AppCompatActivity {

  private static final String TAG = "EndEventActivity";
  Context context;
  RecyclerView eventsRecyclerView;
  Button cancelBtn;
  User user;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_end_event);

    user = Utility.getUser();
    context = EndEventActivity.this;
    eventsRecyclerView = findViewById(R.id.CHOOSE_EVENTS_TO_END);
    cancelBtn = findViewById(R.id.BACK_BTN);

    EventManager.getStartedEventsByHost(context, user, events -> {
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