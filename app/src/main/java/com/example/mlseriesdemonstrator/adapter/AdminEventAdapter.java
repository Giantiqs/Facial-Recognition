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

  private Context context;
  private List<Event> events;
  private Random random;

  public AdminEventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
    this.random = new Random();
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

    // Set the background color of the card's layout
    int pastelColor = generatePastelColor();

    // Create a copy of the card_bg drawable
    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_bg, null);

    // Change the background color
    if (drawable instanceof GradientDrawable) {
      GradientDrawable gradientDrawable = (GradientDrawable) drawable;
      gradientDrawable.setColor(pastelColor);
    }

    // Set it as the background for the item view
    holder.itemView.setBackground(drawable);

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

  private int generatePastelColor() {
    // Generate random values within a pastel color range
    int red = 100 + random.nextInt(156);   // R: 100-255
    int green = 100 + random.nextInt(156); // G: 100-255
    int blue = 100 + random.nextInt(156);  // B: 100-255

    return Color.argb(255, red, green, blue);
  }

}
