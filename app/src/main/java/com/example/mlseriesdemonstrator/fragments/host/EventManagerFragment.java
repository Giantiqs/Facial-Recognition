package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.CancelEventActivity;
import com.example.mlseriesdemonstrator.activities.host.EndEventActivity;
import com.example.mlseriesdemonstrator.activities.host.SchedulerActivity;
import com.example.mlseriesdemonstrator.activities.host.StartEventActivity;

public class EventManagerFragment extends Fragment {

  Button scheduleEventBtn;
  Button startEventBtn;
  Button endEventActivity;
  Button cancelEventBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_event_manager, container, false);

    scheduleEventBtn = view.findViewById(R.id.SCHEDULE_EVENT);
    startEventBtn = view.findViewById(R.id.START_EVENT);
    endEventActivity = view.findViewById(R.id.END_EVENT);
    cancelEventBtn = view.findViewById(R.id.CANCEL_EVENT);

    scheduleEventBtn.setOnClickListener(v -> startActivity(
            new Intent(getActivity(), SchedulerActivity.class))
    );

    startEventBtn.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), StartEventActivity.class))
    );

    endEventActivity.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), EndEventActivity.class))
    );

    cancelEventBtn.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), CancelEventActivity.class)));

    return view;
  }
}