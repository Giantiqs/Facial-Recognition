package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.adapter.OptionAdapter;
import com.example.mlseriesdemonstrator.model.Options;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

  private static final String TAG = "HomeFragment";
  Context context;
  RecyclerView eventsRecyclerView;
  User user;
  LinearLayout noEventsLayout;
  TextView upcomingEventTxt;
  RecyclerView homeOptions;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_home, container, false);
    eventsRecyclerView = view.findViewById(R.id.EVENTS_RECYCLER);
    noEventsLayout = view.findViewById(R.id.NO_EVENT_LAYOUT);
    upcomingEventTxt = view.findViewById(R.id.UPCOMING_EVENT_TXT);
    homeOptions = view.findViewById(R.id.HOME_OPTIONS);

    // Initialize the context
    context = getActivity();
    user = Utility.getUser();

    EventManager.getNearestEventsByUserCourse(context, user, events -> {
      // Handle the retrieved events here
      if (!events.isEmpty()) {
        // Set the adapter after you have data in eventArrayList
        noEventsLayout.setVisibility(View.GONE);
        upcomingEventTxt.setVisibility(View.VISIBLE);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(new EventAdapter(getContext(), events));

        ArrayList<Options> options = new ArrayList<>();

        options.add(new Options("Check your profile!", new AccountFragment()));
        options.add(new Options("Check your profile! 2", new AccountFragment()));

        homeOptions.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        homeOptions.setAdapter(
                new OptionAdapter(getContext(), requireActivity().getSupportFragmentManager(), options)
        );

      } else {
        upcomingEventTxt.setVisibility(View.GONE);
        eventsRecyclerView.setVisibility(View.GONE);
        noEventsLayout.setVisibility(View.VISIBLE);

        Log.d(TAG, "no events rn fr");
      }
    });

    return view;
  }

}
