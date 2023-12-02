package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.SearchedAddress;
import com.example.mlseriesdemonstrator.view_holder.AddressViewHolder;

import org.w3c.dom.Text;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {

  private static final String TAG = "AddressAdapter";
  Context context;
  List<SearchedAddress> addresses;

  public interface TextListener {
    public void onTextClicked(String address);
  }

  public static TextListener listener;

  public AddressAdapter(Context context, List<SearchedAddress> addresses) {
    this.context = context;
    this.addresses = addresses;
  }

  @NonNull
  @Override
  public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.searched_address_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
    holder.addressName.setText(addresses.get(position).getAddress());

    holder.addressName.setOnClickListener(v -> {
      if (listener != null) {
        listener.onTextClicked(addresses.get(position).getAddress());
      }
    });
  }

  @Override
  public int getItemCount() {
    return addresses.size();
  }

  public void updateData(List<SearchedAddress> updatedAddresses) {
    addresses.clear();
    addresses.addAll(updatedAddresses);
    notifyDataSetChanged();
  }

  public void setTextListener(TextListener listener) {
    this.listener = listener;
  }
}
