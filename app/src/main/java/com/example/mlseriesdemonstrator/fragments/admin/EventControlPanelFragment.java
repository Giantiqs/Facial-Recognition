package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.AdminEventAdapter;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;

import java.util.ArrayList;
import java.util.List;

public class EventControlPanelFragment extends Fragment {

  RecyclerView allEvents;
  AdminEventAdapter adminEventAdapter;
  EditText searchEventTxt;
  Spinner eventDateSpinner;

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
    searchEventTxt = view.findViewById(R.id.SEARCH_EVENT);
    eventDateSpinner = view.findViewById(R.id.EVENTS_SPINNER);

    // Initialize the adapter with an empty list
    adminEventAdapter = new AdminEventAdapter(getActivity(), new ArrayList<>());

    allEvents.setAdapter(adminEventAdapter);

    // Call the method to retrieve events from EventManager
    EventManager.getAllEvents(getActivity(), events -> {
      // Update the adapter with the retrieved events

      adminEventAdapter.setData(events);
    });

    searchEventTxt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterEvents(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    eventDateSpinner = view.findViewById(R.id.EVENTS_SPINNER);
    ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.sort_options,
            R.layout.spinner_item
    );
    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
    eventDateSpinner.setAdapter(spinnerAdapter);

    eventDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // Handle sorting here based on the selected option
        String selectedOption = eventDateSpinner.getSelectedItem().toString();
        if (selectedOption.equals("Ascending")) {
          // Sort events in ascending order
          adminEventAdapter.sortEventsAscending();
        } else if (selectedOption.equals("Descending")) {
          // Sort events in descending order
          adminEventAdapter.sortEventsDescending();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
        // Do nothing
      }
    });

    return view;
  }

  private void filterEvents(String searchText) {
    if (adminEventAdapter != null) {
      List<Event> filteredEvents = new ArrayList<>();
      for (Event event : adminEventAdapter.getOriginalData()) {
        if (
                event.getTitle().toLowerCase().contains(searchText.toLowerCase())
                || event.getLocation().getLocationAddress().toLowerCase().contains(searchText.toLowerCase())
                || event.getDate().toLowerCase().contains(searchText.toLowerCase())
                || event.getStartTime().toLowerCase().contains(searchText.toLowerCase())
                || event.getStatus().toLowerCase().contains(searchText.toLowerCase())
                || event.getDateTime().toString().toLowerCase().contains(searchText.toLowerCase())
        ) {
          filteredEvents.add(event);
        }
      }
      adminEventAdapter.filterEvents(filteredEvents);
    }
  }


}
