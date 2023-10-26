package com.example.mlseriesdemonstrator.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.admin.EditEventActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.view_holder.AdminEventViewHolder;

import java.util.Collections;
import java.util.List;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventViewHolder> {

  private final Context context;
  private List<Event> originalEvents;  // Store the original list of events
  private List<Event> filteredEvents;  // Store the currently displayed (filtered) events

  public AdminEventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.originalEvents = events;  // Initialize originalEvents
    this.filteredEvents = events;  // Initialize filteredEvents
  }

  @NonNull
  @Override
  public AdminEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.admin_event_view, parent, false);
    return new AdminEventViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AdminEventViewHolder holder, int position) {
    Event event = filteredEvents.get(position);
    holder.eventTitleTxt.setText(event.getTitle());
    holder.eventLocationTxt.setText(event.getLocation().getLocationAddress());
    holder.eventDateTxt.setText(event.getDate());
    holder.eventTimeTxt.setText(event.getStartTime());
    holder.statusTxt.setText(event.getStatus());

    holder.itemView.setOnLongClickListener(v -> {
      Intent intent = new Intent(context, EditEventActivity.class);
      intent.putExtra("event_id", event.getEventId());
      ((Activity) context).startActivity(intent);
      return true;
    });
  }

  @Override
  public int getItemCount() {
    return filteredEvents.size();
  }

  // Add a method to update the event data
  public void setData(List<Event> events) {
    this.originalEvents = events;  // Update the original list of events
    this.filteredEvents = events;  // Update the filtered list of events
    notifyDataSetChanged();
  }

  // Add a method to retrieve the original list of events
  public List<Event> getOriginalData() {
    return originalEvents;
  }

  // Add a method to filter events
  public void filterEvents(List<Event> filteredEvents) {
    this.filteredEvents = filteredEvents;
    notifyDataSetChanged();
  }

  public void sortEventsAscending() {
    Collections.sort(filteredEvents, (event1, event2) -> {
      // Implement your comparison logic for ascending order here
      return event1.getDateTime().compareTo(event2.getDateTime());
    });
    notifyDataSetChanged();
  }

  public void sortEventsDescending() {
    Collections.sort(filteredEvents, (event1, event2) -> {
      // Implement your comparison logic for descending order here
      return event2.getDateTime().compareTo(event1.getDateTime());
    });
    notifyDataSetChanged();
  }

}
