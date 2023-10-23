package com.example.mlseriesdemonstrator.view_holder;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;

public class AdminEventViewHolder extends RecyclerView.ViewHolder {

  private static final String TAG = "AdminEventViewHolder";
  public TextView eventTitleTxt;
  public TextView eventLocationTxt;
  public TextView eventDateTxt;
  public TextView eventTimeTxt;
  public TextView statusTxt;

  public AdminEventViewHolder(@NonNull View itemView) {
    super(itemView);

    eventTitleTxt = itemView.findViewById(R.id.EVENT_TITLE);
    eventLocationTxt = itemView.findViewById(R.id.EVENT_LOCATION);
    eventDateTxt = itemView.findViewById(R.id.EVENT_DATE);
    eventTimeTxt = itemView.findViewById(R.id.EVENT_TIME);
    statusTxt = itemView.findViewById(R.id.EVENT_STATUS);

    Log.d(TAG, "Hello");
  }
}
