package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.EventAdapter;
import com.example.mlseriesdemonstrator.adapter.OptionAdapter;
import com.example.mlseriesdemonstrator.fragments.student.AccountFragment;
import com.example.mlseriesdemonstrator.model.Options;
import com.example.mlseriesdemonstrator.utilities.EventManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

  private static final String TAG = "HomeFragment";
  Context context;
  RecyclerView eventRecyclerView;
  LinearLayout noEventsCard;
  TextView upcomingEventsTxt;
  RecyclerView homeOptions;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
          LayoutInflater inflater,
          ViewGroup container,
          Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_home2, container, false);
    eventRecyclerView = view.findViewById(R.id.EVENTS_RECYCLER);
    noEventsCard = view.findViewById(R.id.NO_EVENT_LAYOUT);
    upcomingEventsTxt = view.findViewById(R.id.UPCOMING_EVENT_TXT);
    homeOptions = view.findViewById(R.id.HOME_OPTIONS);

    context = getActivity();

    EventManager.getNearestUpcomingEvents(context, events -> {
      // Handle the retrieved events here
      if (!events.isEmpty()) {
        // Set the adapter after you have data in eventArrayList
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecyclerView.setAdapter(new EventAdapter(getContext(), events));
      } else {
        Log.d(TAG, "onCreateView: meow");
        noEventsCard.setVisibility(View.VISIBLE);
        upcomingEventsTxt.setVisibility(View.GONE);
      }
    });

    ArrayList<Options> options = new ArrayList<>();

    options.add(new Options("Check your profile!", new HomeFragment()));
    options.add(new Options("Check your profile!", new HomeFragment()));

    homeOptions.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
    );

    homeOptions.setAdapter(
            new OptionAdapter(getContext(), requireActivity().getSupportFragmentManager(), options)
    );

    return view;
  }
}
