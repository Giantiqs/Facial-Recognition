package com.example.mlseriesdemonstrator.fragments.host;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;

public class HomeFragment extends Fragment {

    Context context;
    TextView upcomingEventTxt;
    TextView eventTitleTxt;
    TextView eventLocationTxt;
    TextView eventDateTxt;
    TextView eventTimeTxt;
    LinearLayout card;
    TextView at;
    Event nearestEvent;

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
        upcomingEventTxt = view.findViewById(R.id.UPCOMING_EVENT);
        eventTitleTxt = view.findViewById(R.id.EVENT_TITLE);
        eventLocationTxt = view.findViewById(R.id.EVENT_LOCATION);
        eventDateTxt = view.findViewById(R.id.EVENT_DATE);
        eventTimeTxt = view.findViewById(R.id.EVENT_TIME);
        card = view.findViewById(R.id.SCHEDULE);
        at = view.findViewById(R.id.AT);

        context = getActivity();

        // Retrieve the nearest event and set it
        EventManager.getNearestEvent(context, event -> {
            nearestEvent = event;
            setNearestEvent();
        });

        return view;
    }

    private void setNearestEvent() {
        // Check if the nearest event is not null
        if (nearestEvent != null) {
            // Set the TextViews with event details
            upcomingEventTxt.setText("Upcoming Event:");
            eventTitleTxt.setText(nearestEvent.getTitle());
            eventLocationTxt.setText(nearestEvent.getLocation());
            eventDateTxt.setText(nearestEvent.getDate());
            eventTimeTxt.setText(nearestEvent.getStartTime());

            // Make the TextViews visible
            upcomingEventTxt.setVisibility(View.VISIBLE);
            eventTitleTxt.setVisibility(View.VISIBLE);
            eventLocationTxt.setVisibility(View.VISIBLE);
            card.setVisibility(View.VISIBLE);
            at.setVisibility(View.VISIBLE);
        }
    }

}
