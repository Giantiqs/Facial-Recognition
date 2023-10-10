package com.example.mlseriesdemonstrator.view_holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;

public class EventViewHolder extends RecyclerView.ViewHolder {

  public TextView eventTitleTxt;
  public TextView eventLocationTxt;
  public TextView eventDateTxt;
  public TextView eventTimeTxt;

  public EventViewHolder(@NonNull View itemView) {
    super(itemView);

    eventTitleTxt = itemView.findViewById(R.id.EVENT_TITLE);
    eventLocationTxt = itemView.findViewById(R.id.EVENT_LOCATION);
    eventDateTxt = itemView.findViewById(R.id.EVENT_DATE);
    eventTimeTxt = itemView.findViewById(R.id.EVENT_TIME);
  }
}
