package com.example.mlseriesdemonstrator.view_holder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;

public class OptionViewHolder extends RecyclerView.ViewHolder {

  public TextView optionMsg;
  public Button optionBtn;

  public OptionViewHolder(@NonNull View itemView) {
    super(itemView);

    optionMsg = itemView.findViewById(R.id.OPTION_MSG);
    optionBtn = itemView.findViewById(R.id.OPTION_BTN);
  }
}
