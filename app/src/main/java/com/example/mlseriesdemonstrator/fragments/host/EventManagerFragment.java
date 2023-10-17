package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.CancelEventActivity;
import com.example.mlseriesdemonstrator.activities.host.SchedulerActivity;
import com.example.mlseriesdemonstrator.activities.host.StartEventActivity;

public class EventManagerFragment extends Fragment {

  private static final String TAG = "EventManagerFragment";
  // Add other buttons here later
  Button scheduleEventBtn;
  Button startEventBtn;
  Button cancelEventBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    Log.d(TAG, "sup");

    View view = inflater.inflate(
            R.layout.fragment_event_manager,
            container,
            false
    );

    scheduleEventBtn = view.findViewById(R.id.SCHEDULE_EVENT);
    startEventBtn = view.findViewById(R.id.START_EVENT);
    cancelEventBtn = view.findViewById(R.id.CANCEL_EVENT);

    scheduleEventBtn.setOnClickListener(v -> startActivity(
            new Intent(getActivity(), SchedulerActivity.class))
    );

    startEventBtn.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), StartEventActivity.class))
    );

    cancelEventBtn.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), CancelEventActivity.class)));

    return view;
  }
}