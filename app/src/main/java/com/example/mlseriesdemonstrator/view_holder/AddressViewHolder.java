package com.example.mlseriesdemonstrator.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;

public class AddressViewHolder extends RecyclerView.ViewHolder {

  public TextView addressName;
  public AddressViewHolder(@NonNull View itemView) {
    super(itemView);

    addressName = itemView.findViewById(R.id.ADDRESS_NAME);
  }
}
