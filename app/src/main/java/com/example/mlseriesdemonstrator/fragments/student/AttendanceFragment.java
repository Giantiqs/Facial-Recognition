package com.example.mlseriesdemonstrator.fragments.student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {

  private static final String TAG = "AttendanceFragment";
  RecyclerView startedEventsRecyclerView;
  User user;
  EventAdapter eventAdapter;
  LinearLayout noEventCard;
  LinearLayout onGoingEventsCard;
  LinearLayout noFaceRegisteredCard;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    user = Utility.getUser();
    eventAdapter = new EventAdapter(context, new ArrayList<>(), true);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @SuppressLint("NotifyDataSetChanged")
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_attendance, container, false);

    startedEventsRecyclerView = view.findViewById(R.id.STARTED_EVENTS_RECYCLER);
    noEventCard = view.findViewById(R.id.NO_EVENT_LAYOUT);
    onGoingEventsCard = view.findViewById(R.id.ONGOING_EVENTS_CARD);
    noFaceRegisteredCard = view.findViewById(R.id.NO_FACE_LAYOUT);

    startedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    startedEventsRecyclerView.setAdapter(eventAdapter);

    if (user.getFaceVector() == null) {
      noFaceRegisteredCard.setVisibility(View.VISIBLE);
      noEventCard.setVisibility(View.GONE);
    } else {
      EventManager.getStartedEvents(requireContext(), user, events -> {
        if (!events.isEmpty()) {
          eventAdapter.setData(events);
          eventAdapter.notifyDataSetChanged();
          onGoingEventsCard.setVisibility(View.VISIBLE);
          noEventCard.setVisibility(View.GONE);
        } else {
          onGoingEventsCard.setVisibility(View.GONE);
          noEventCard.setVisibility(View.VISIBLE);
        }
      });
    }

    return view;
  }
}

