package com.example.mlseriesdemonstrator.activities.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;

public class EditEventActivity extends AppCompatActivity {

  private static final String TAG = "EditEventActivity";
  RecyclerView eventsRecyclerView;
  Button backBtn;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_event);

    // reuse the dialog and add a restart event btn if the event is cancelled
  }
}