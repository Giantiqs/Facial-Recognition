package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.example.mlseriesdemonstrator.view_holder.AdminEventViewHolder;

import java.util.List;
import java.util.Random;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventViewHolder> {

  private final Context context;
  private List<Event> events;

  public AdminEventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
  }

  @NonNull
  @Override
  public AdminEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.admin_event_view, parent, false);
    return new AdminEventViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AdminEventViewHolder holder, int position) {
    Event event = events.get(position);
    holder.eventTitleTxt.setText(event.getTitle());
    holder.eventLocationTxt.setText(event.getLocation().getLocationAddress());
    holder.eventDateTxt.setText(event.getDate());
    holder.eventTimeTxt.setText(event.getStartTime());
    holder.statusTxt.setText(event.getStatus());

    holder.itemView.setOnLongClickListener(v -> {
      Utility.showToast(context, "ze event");
      return true;
    });
  }

  @Override
  public int getItemCount() {
    return events.size();
  }

  // Add a method to update the event data
  public void setData(List<Event> events) {
    this.events = events;
    notifyDataSetChanged();
  }

}
