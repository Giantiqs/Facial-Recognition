package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.api.Usage;

import okhttp3.internal.Util;

public class AttendanceFragment extends Fragment {

  private static final String TAG = "AttendanceFragment";
  Button getStartedBtn;
  RecyclerView startedEventsRecyclerView;
  Context context;
  User user;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_attendance, container, false);

    user = Utility.getUser();
    context = getActivity();
    getStartedBtn = view.findViewById(R.id.GET_STARTED);
    startedEventsRecyclerView = view.findViewById(R.id.STARTED_EVENTS_RECYCLER);

    EventManager.getStartedEvents(context, user, events -> {
      if (!events.isEmpty()) {
        Log.d(TAG, getContext().getClass().getName());

        /*
        * Create a new activity that will display started events and move some code from here
        * to that activity.
         */
        startedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        startedEventsRecyclerView.setAdapter(new EventAdapter(getContext(), events));
      } else {
        Log.d(TAG, "onCreateView: meow");
      }
    });

    getStartedBtn.setOnClickListener(v -> {

      Intent intent = new Intent(context, FaceRecognitionActivity.class);
      String attendance = "attendance";

      intent.putExtra("mode", attendance);

      startActivity(intent);
    });

    return view;
  }
}