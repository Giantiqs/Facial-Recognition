package com.example.mlseriesdemonstrator.tests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = "GeofenceBroadcastReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO: This method is called when the BroadcastReceiver is receiving
    // an Intent broadcast.

//    Utility.showToast(context, "Geofence triggered");

    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

    if (geofencingEvent.hasError()) {
      Log.d(TAG, "onReceive: Error geofence event");
      return;
    }

    List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();

    for (Geofence geofence : geofenceList) {
      Log.d(TAG, "onReceive: " + geofence.getRequestId());
    }

//    Location location = geofencingEvent.getTriggeringLocation();
    int transitionType = geofencingEvent.getGeofenceTransition();

    switch (transitionType) {
      case Geofence.GEOFENCE_TRANSITION_ENTER:
        Utility.showToast(context, "GEOFENCE_TRANSITION_ENTER");
        break;
      case Geofence.GEOFENCE_TRANSITION_DWELL:
        Utility.showToast(context, "GEOFENCE_TRANSITION_DWELL");
        break;
      case Geofence.GEOFENCE_TRANSITION_EXIT:
        Utility.showToast(context, "GEOFENCE_TRANSITION_EXIT");
        break;
    }
  }
}