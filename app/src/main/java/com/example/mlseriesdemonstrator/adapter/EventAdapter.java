package com.example.mlseriesdemonstrator.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.CancelEventActivity;
import com.example.mlseriesdemonstrator.activities.host.HostHistoryActivity;
import com.example.mlseriesdemonstrator.activities.host.StartEventActivity;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.view_holder.EventViewHolder;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

  private static final String TAG = "EventAdapter";
  Context context;
  List<Event> events;
  Random random;
  String contextClassName;
  boolean isFromAttendanceFragment;

  public EventAdapter(Context context, List<Event> events) {
    this.context = context;
    this.events = events;
    random = new Random(); // Initialize the random generator
  }

  public EventAdapter(Context context, List<Event> events, boolean isFromAttendanceFragment) {
    this.context = context;
    this.events = events;
    random = new Random(); // Initialize the random generator
    this.isFromAttendanceFragment = isFromAttendanceFragment;
  }

  @NonNull
  @Override
  public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Get the name of the context's class
    contextClassName = parent.getContext().getClass().getName();

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
    holder.eventLocationTxt.setText(event.getLocation().getLocationAddress());
    holder.eventDateTxt.setText(event.getDate());
    holder.eventTimeTxt.setText(event.getStartTime());

    // Create a copy of the card_bg drawable
    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.card_bg, null);

    // Change the background color (e.g., to blue)
    if (drawable instanceof GradientDrawable) {
      GradientDrawable gradientDrawable = (GradientDrawable) drawable;
      gradientDrawable.setColor(generatePastelColor()); // Change the color to your desired color
    }

    // Set it as the background for the item view
    if (!contextClassName.equals(HostHistoryActivity.class.getName()))
      holder.itemView.setBackground(drawable);

    if (contextClassName.equals(StartEventActivity.class.getName())) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view); // Set the content view here
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    if (contextClassName.equals(CancelEventActivity.class.getName())) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    if (isFromAttendanceFragment) {
      holder.itemView.setOnClickListener(v -> {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.yes_no_dialog_view);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
    if (promptTxt != null) {
      String cancelStr = "Are you sure you want to cancel the event?";
      promptTxt.setText(cancelStr);
    }

    if (isFromAttendanceFragment && promptTxt != null) {
      String attendStr = "Do you want to attend this event?";
      promptTxt.setText(attendStr);
    }
    eventTitleTxt.setText(event.getTitle());
  }

  @Override
  public int getItemCount() {
    return events.size();
  }

  private int generatePastelColor() {
    // Generate random values within a pastel color range
    int red = 100 + random.nextInt(156);   // R: 100-255
    int green = 100 + random.nextInt(156); // G: 100-255
    int blue = 100 + random.nextInt(156);  // B: 100-255

    return Color.argb(255, red, green, blue);
  }

}
