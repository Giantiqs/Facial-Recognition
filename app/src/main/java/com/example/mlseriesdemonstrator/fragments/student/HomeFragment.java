package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;

public class HomeFragment extends Fragment {

    Context context;
    TextView upcomingEventTxt;
    TextView eventTitleTxt;
    TextView eventLocationTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        upcomingEventTxt = view.findViewById(R.id.UPCOMING_EVENT);
        eventTitleTxt = view.findViewById(R.id.EVENT_TITLE);
        eventLocationTxt = view.findViewById(R.id.EVENT_LOCATION);

        context = getActivity();

        setNearestEvent();

        return view;
    }

    private void setNearestEvent() {

        /*

            SET THE UPCOMING EVENT IN THE TEXTS HERE. AND MAKE THE TEXT VIEWS VISIBLE

         */
    }
}