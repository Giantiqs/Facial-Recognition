package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.view_holder.EventViewHolder;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

  Context context;
  List<Event> events;

  public EventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
  }

  @NonNull
  @Override
  public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new EventViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_view, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
    holder.eventTitleTxt.setText(events.get(position).getTitle());
    holder.eventLocationTxt.setText(events.get(position).getLocation());
    holder.eventDateTxt.setText(events.get(position).getDate());
    holder.eventTimeTxt.setText(events.get(position).getStartTime());
  }

  @Override
  public int getItemCount() {
    return events.size();
  }
}
