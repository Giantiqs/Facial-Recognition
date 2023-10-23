package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.AdminEventAdapter;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class EventControlPanelFragment extends Fragment {

  private static final String TAG = "EventControlPanelFragme";
  RecyclerView allEvents;
  AdminEventAdapter adminEventAdapter;

  public EventControlPanelFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_event_control_panel, container, false);

    allEvents = view.findViewById(R.id.ALL_EVENTS_RECYCLER);
    allEvents.setLayoutManager(new LinearLayoutManager(getActivity()));

    // Initialize the adapter with an empty list
    adminEventAdapter = new AdminEventAdapter(getActivity(), new ArrayList<>());

    allEvents.setAdapter(adminEventAdapter);

    // Call the method to retrieve events from EventManager
    EventManager.getAllEvents(getActivity(), events -> {
      // Update the adapter with the retrieved events
      adminEventAdapter.setData(events);
    });

    return view;
  }
}
