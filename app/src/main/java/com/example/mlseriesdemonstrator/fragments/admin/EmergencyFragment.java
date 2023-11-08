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
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;

public class EmergencyFragment extends Fragment {

  private static final String TAG = "EmergencyFragment";
  private RecyclerView startedEvents;
  private EventAdapter eventAdapter; // Declare the EventAdapter

  public EmergencyFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_admin_dash_board, container, false);

    startedEvents = view.findViewById(R.id.STARTED_EVENTS_ADMIN);

    // Create the EventAdapter and set it to the RecyclerView
    eventAdapter = new EventAdapter(requireContext(), new ArrayList<>(), 1); // Adjust parameters as needed
    startedEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
    startedEvents.setAdapter(eventAdapter);

    User user = Utility.getUser();

    EventManager.getStartedEvents(requireContext(), events -> {
      if (!events.isEmpty()) {
        eventAdapter.setData(events);
        eventAdapter.notifyDataSetChanged();
      } else {
        Log.d(TAG, "No events found");
      }
    });

    return view;
  }
}
