package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

  private static final String TAG = "HomeFragment";
  Context context;
  RecyclerView courseEventsRV;
  RecyclerView allEventsRV;
  RecyclerView startedEventsRV;
  User user;
  LinearLayout noEventsLayout;
  LinearLayout eventsForYouCard;
  LinearLayout allEventsCard;
  LinearLayout ongoingEventsCard;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_home, container, false);
    int[] eventSize = new int[1];

    courseEventsRV = view.findViewById(R.id.EVENTS_RECYCLER);
    noEventsLayout = view.findViewById(R.id.NO_EVENT_LAYOUT);
    allEventsRV = view.findViewById(R.id.ALL_EVENTS_HOME);
    startedEventsRV = view.findViewById(R.id.COURSE_STARTED_EVENTS);
    eventsForYouCard = view.findViewById(R.id.EVENTS_COURSE);
    allEventsCard = view.findViewById(R.id.ALL_EVENTS_USER);
    ongoingEventsCard = view.findViewById(R.id.ONGOING_EVENTS_USER);

    // Initialize the context
    context = getActivity();
    user = Utility.getUser();

    EventManager.getNearestEventsByUserCourse(context, user, events -> {
      // Handle the retrieved events here
      if (!events.isEmpty()) {
        // Set the adapter after you have data in eventArrayList
        noEventsLayout.setVisibility(View.GONE);
        eventsForYouCard.setVisibility(View.VISIBLE);
        courseEventsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        courseEventsRV.setAdapter(new EventAdapter(getContext(), events, "user_home"));

        eventSize[0]+=events.size();
      } else {
        eventsForYouCard.setVisibility(View.GONE);
      }
    });

    EventManager.getAllEvents(context, events -> {
      if (!events.isEmpty()) {
        EventAdapter eventAdapter = new EventAdapter(context, new ArrayList<>(), "user_home");
        // Set the adapter after you have data in eventArrayList
        eventAdapter.setData(events);
        eventAdapter.notifyDataSetChanged();
        noEventsLayout.setVisibility(View.GONE);
        allEventsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allEventsRV.setAdapter(eventAdapter);
        allEventsCard.setVisibility(View.VISIBLE);

        eventSize[0]+=events.size();
      } else {
        allEventsCard.setVisibility(View.GONE);
      }
    });

    EventManager.getStartedEvents(context, user, events -> {

      if (!events.isEmpty()) {
        EventAdapter eventAdapter = new EventAdapter(context, new ArrayList<>(), "user_home");
        // Set the adapter after you have data in eventArrayList
        eventAdapter.setData(events);
        eventAdapter.notifyDataSetChanged();
        noEventsLayout.setVisibility(View.GONE);
        ongoingEventsCard.setVisibility(View.VISIBLE);
        startedEventsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        startedEventsRV.setAdapter(eventAdapter);

        eventSize[0]+=events.size();
      } else {
        ongoingEventsCard.setVisibility(View.GONE);
      }
    });

    if (eventSize[0] == 0) {
      noEventsLayout.setVisibility(View.VISIBLE);
    }

    return view;
  }

}
