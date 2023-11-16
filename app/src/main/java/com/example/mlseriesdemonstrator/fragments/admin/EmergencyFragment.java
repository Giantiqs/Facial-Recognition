package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;

public class EmergencyFragment extends Fragment {

  private static final String TAG = "EmergencyFragment";
  RecyclerView startedEvents;
  private EventAdapter eventAdapter; // Declare the EventAdapter
  EditText searchBox;

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
    searchBox = view.findViewById(R.id.SEARCH_EVENT);

    // Create the EventAdapter and set it to the RecyclerView

    EventManager.getStartedEvents(requireContext(), events -> {
      if (!events.isEmpty()) {
        eventAdapter = new EventAdapter(requireContext(), events, 1);
        startedEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        startedEvents.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
      } else {
        Log.d(TAG, "No events found");
      }
    });

    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if (eventAdapter != null) {
          eventAdapter.filterEvents(editable.toString());
          Log.d(TAG, "hi");
        }
      }
    });

    return view;
  }
}
