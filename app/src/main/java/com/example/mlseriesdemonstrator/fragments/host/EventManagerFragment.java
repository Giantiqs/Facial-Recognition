package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.SchedulerActivity;

public class EventManagerFragment extends Fragment {

  private static final String TAG = "EventManagerFragment";
  // Add other buttons here later
  Button eventBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {


    View view = inflater.inflate(
            R.layout.fragment_event_manager,
            container,
            false
    );

    eventBtn = view.findViewById(R.id.SCHEDULE_EVENT);

    eventBtn.setOnClickListener(v -> startActivity(
            new Intent(getActivity(), SchedulerActivity.class))
    );

    return view;
  }
}