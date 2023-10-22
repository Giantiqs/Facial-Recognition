package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mlseriesdemonstrator.R;

public class EventControlPanelFragment extends Fragment {

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

    return view;
  }
}