package com.example.mlseriesdemonstrator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.CancelEventActivity;
import com.example.mlseriesdemonstrator.activities.host.EndEventActivity;
import com.example.mlseriesdemonstrator.activities.host.HostHistoryActivity;
import com.example.mlseriesdemonstrator.activities.host.StartEventActivity;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.view_holder.EventViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

  private static final String TAG = "EventAdapter";
  Context context;
  List<Event> events;
  List<Event> filteredEvents;
  int isAdmin;
  String fragment;

  @SuppressLint("NotifyDataSetChanged")
  public void setData(List<Event> events) {
    this.events = events;
    notifyDataSetChanged();
  }

  String contextClassName;
  boolean isFromAttendanceFragment;

  public EventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
    this.filteredEvents = new ArrayList<>(events);
  }

  public EventAdapter(Context context, List<Event> events, boolean isFromAttendanceFragment) {
    this.context = context;
    this.events = events;
    this.isFromAttendanceFragment = isFromAttendanceFragment;
  }

  public EventAdapter(Context context, List<Event> events, int isAdmin) {
    this.context = context;
    this.events = events;
    this.isAdmin = isAdmin;
  }

  public EventAdapter(Context context, List<Event> events, String fragment) {
    this.context = context;
    this.events = events;
    this.fragment = fragment;
  }

  @NonNull
  @Override
  public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Get the name of the context's class
    contextClassName = parent.getContext().getClass().getName();
    Log.d(TAG, "hallo");

    if (contextClassName.equals(HostHistoryActivity.class.getName())) {
      return new EventViewHolder(
              LayoutInflater.from(context).inflate(R.layout.event_view, parent, false)
      );
    }

    if (fragment != null) {
      if (fragment.equals("user_home")) {
        return new EventViewHolder(
                LayoutInflater.from(context).inflate(R.layout.small_event_view, parent, false)
        );
      }
    }

    return new EventViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_view, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
    Event event;

    if (fragment == null) {
      fragment = "";
    }

    if (fragment.equals("user_home") || isAdmin == 1 || isFromAttendanceFragment) {
      event = events.get(position);
    } else {
      event = filteredEvents.get(position);
    }

    // Set the text data for the view holder
    holder.eventTitleTxt.setText(event.getTitle());
    holder.eventLocationTxt.setText(event.getLocation().getLocationAddress());
    holder.eventDateTxt.setText(event.getDate());
    holder.eventTimeTxt.setText(event.getStartTime());

    // Create a copy of the card_bg drawable
    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_bg2, null);

    // Set it as the background for the item view
    if (!contextClassName.equals(HostHistoryActivity.class.getName()))
      holder.itemView.setBackground(drawable);

    if (fragment != null) {
      if (fragment.equals("user_home"))
        holder.itemView.setBackground(drawable);
    }

    if (contextClassName.equals(StartEventActivity.class.getName())) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view); // Set the content view here
        Objects.requireNonNull(
                dialog.getWindow())
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(false);

        // Now you can find the buttons
        Button yesBtn = dialog.findViewById(R.id.YES_BTN);
        Button noBtn = dialog.findViewById(R.id.NO_BTN);
        TextView eventTitleTxt = dialog.findViewById(R.id.EVENT_TITLE);

        setDialogTitleText(eventTitleTxt, event, null);

        yesBtn.setOnClickListener(v1 -> {
          EventManager.startEvent(event, context);
          dialog.dismiss();

          ((Activity) context).finish();
        });

        noBtn.setOnClickListener(v1 -> dialog.dismiss());

        dialog.show(); // Show the dialog
      });
    }

    if (contextClassName.equals(EndEventActivity.class.getName())) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view);
        Objects.requireNonNull(
                        dialog.getWindow())
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
        dialog.setCancelable(false);

        Button yesBtn = dialog.findViewById(R.id.YES_BTN);
        Button noBtn = dialog.findViewById(R.id.NO_BTN);
        TextView eventTitleTxt = dialog.findViewById(R.id.EVENT_TITLE);
        TextView promptTxt = dialog.findViewById(R.id.PROMPT_TXT);

        setDialogTitleText(eventTitleTxt, event, promptTxt);

        yesBtn.setOnClickListener(v1 -> {
          EventManager.endEvent(event, context);
          dialog.dismiss();

          ((Activity) context).finish();
        });

        noBtn.setOnClickListener(v1 -> dialog.dismiss());

        dialog.show(); // Show the dialog
      });
    }

    if (contextClassName.equals(CancelEventActivity.class.getName())) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view);
        Objects.requireNonNull(
                dialog.getWindow())
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(false);

        Button yesBtn = dialog.findViewById(R.id.YES_BTN);
        Button noBtn = dialog.findViewById(R.id.NO_BTN);
        TextView eventTitleTxt = dialog.findViewById(R.id.EVENT_TITLE);
        TextView promptTxt = dialog.findViewById(R.id.PROMPT_TXT);

        setDialogTitleText(eventTitleTxt, event, promptTxt);

        yesBtn.setOnClickListener(v1 -> {
          EventManager.cancelEvent(event, context);
          dialog.dismiss();

          ((Activity) context).finish();
        });

        noBtn.setOnClickListener(v1 -> dialog.dismiss());

        dialog.show(); // Show the dialog
      });
    }

    if (isFromAttendanceFragment || isAdmin == 1) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view);
        Objects.requireNonNull(
                dialog.getWindow())
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(false);

        Button yesBtn = dialog.findViewById(R.id.YES_BTN);
        Button noBtn = dialog.findViewById(R.id.NO_BTN);
        TextView eventTitleTxt = dialog.findViewById(R.id.EVENT_TITLE);
        TextView promptTxt = dialog.findViewById(R.id.PROMPT_TXT);

        setDialogTitleText(eventTitleTxt, event, promptTxt);

        yesBtn.setOnClickListener(v1 -> {
          Intent intent = new Intent(context, FaceRecognitionActivity.class);
          String attendance = "attendance";

          intent.putExtra("mode", attendance);
          intent.putExtra("event_id", event.getEventId());

          context.startActivity(intent);
          dialog.dismiss();
        });

        noBtn.setOnClickListener(v1 -> dialog.dismiss());

        dialog.show(); // Show the dialog
      });
    }

  }

  private void setDialogTitleText(TextView eventTitleTxt, Event event, TextView promptTxt) {
    if (contextClassName.equals(EndEventActivity.class.getName())) {
      String endStr = "Are you sure you want to end the event?";
      promptTxt.setText(endStr);
    } else if (promptTxt != null) {
      String cancelStr = "Are you sure you want to cancel the event?";
      promptTxt.setText(cancelStr);
    }

    if ((isFromAttendanceFragment || isAdmin == 1) && promptTxt != null) {
      String attendStr = "Do you want to attend this event?";
      promptTxt.setText(attendStr);
    }
    eventTitleTxt.setText(event.getTitle());
  }

  @Override
  public int getItemCount() {
    if (fragment == null) {
      fragment = "";
    }

    if (fragment.equals("user_home") || isAdmin == 1 || isFromAttendanceFragment) {
      return events.size();
    }

    return filteredEvents.size();
  }

  public void filterEvents(String searchText) {
    filteredEvents.clear();
    if (searchText.isEmpty()) {
      filteredEvents.addAll(events); // If the search text is empty, show all events
    } else {
      for (Event event : events) {
        // Add events that contain the search text in their title
        if (event.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
          filteredEvents.add(event);
        }
      }
    }
    notifyDataSetChanged(); // Notify the adapter of the filtered data
  }
}
