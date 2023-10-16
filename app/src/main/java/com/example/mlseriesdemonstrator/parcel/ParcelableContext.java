package com.example.mlseriesdemonstrator.parcel;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableContext implements Parcelable {
  private Context context;

  public ParcelableContext(Context context) {
    this.context = context;
  }

  public Context getContext() {
    return context;
  }

  protected ParcelableContext(Parcel in) {
    context = (Context) in.readValue(Context.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ParcelableContext> CREATOR = new Creator<ParcelableContext>() {
    @Override
    public ParcelableContext createFromParcel(Parcel in) {
      return new ParcelableContext(in);
    }

    @Override
    public ParcelableContext[] newArray(int size) {
      return new ParcelableContext[size];
    }
  };
}
