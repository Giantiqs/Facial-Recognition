package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.HostHistoryActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.view_holder.EventViewHolder;

import java.util.List;
import java.util.Random;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

  Context context;
  List<Event> events;
  Random random;

  public EventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
    random = new Random(); // Initialize the random generator
  }

  @NonNull
  @Override
  public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Get the name of the context's class
    String contextClassName = parent.getContext().getClass().getName();

    if (contextClassName.equals(HostHistoryActivity.class.getName())) {
      return new EventViewHolder(
              LayoutInflater.from(context).inflate(R.layout.history_event_view, parent, false)
      );
    }

    return new EventViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_view, parent, false)
    );
  }


  @Override
  public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
    Event event = events.get(position);

    // Set the text data for the view holder
    holder.eventTitleTxt.setText(event.getTitle());
    holder.eventLocationTxt.setText(event.getLocation());
    holder.eventDateTxt.setText(event.getDate());
    holder.eventTimeTxt.setText(event.getStartTime());

    // Create a copy of the card_bg drawable
    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_bg, null);

    // Change the background color (e.g., to blue)
    if (drawable instanceof GradientDrawable) {
      GradientDrawable gradientDrawable = (GradientDrawable) drawable;
      gradientDrawable.setColor(generateRandomColor()); // Change the color to your desired color
    }

    // Set it as the background for the item view
    holder.itemView.setBackground(drawable);
  }

  @Override
  public int getItemCount() {
    return events.size();
  }

  private int generateRandomColor() {
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
  }
}
