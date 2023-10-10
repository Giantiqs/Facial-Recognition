package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class HomeFragment extends Fragment {

  private static final String TAG = "HomeFragment";
  Context context;
  RecyclerView eventsRecyclerView;
  User user;

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

    // Initialize the context
    context = getActivity();
    user = Utility.getUser();

    EventManager.getNearestEventsByUserCourse(context, user.getCourse(), events -> {
      // Handle the retrieved events here
      if (!events.isEmpty()) {
        // Set the adapter after you have data in eventArrayList
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(new EventAdapter(getContext(), events));
      } else {
        Log.d(TAG, "onCreateView: meow");
        Log.d(TAG, user.getCourse());
      }
    });

    return view;
  }

}
