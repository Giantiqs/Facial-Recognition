package com.example.mlseriesdemonstrator.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.api.Usage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = "GeofenceBroadcastReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO: This method is called when the BroadcastReceiver is receiving
    // an Intent broadcast.

    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

    assert geofencingEvent != null;
    if (geofencingEvent.hasError()) {
      Log.d(TAG, "onReceive: Error geofence event");
      return;
    }

    List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();

    assert geofenceList != null;

    for (Geofence geofence : geofenceList) {
      Log.d(TAG, "onReceive: " + geofence.getRequestId());
    }

//    Location location = geofencingEvent.getTriggeringLocation();
    int transitionType = geofencingEvent.getGeofenceTransition();

    switch (transitionType) {
      case Geofence.GEOFENCE_TRANSITION_ENTER:
        Utility.showToast(context, "You entered the geofence");
        break;
      case Geofence.GEOFENCE_TRANSITION_DWELL:
        Utility.showToast(context, "You are inside the geofence");
        break;
      case Geofence.GEOFENCE_TRANSITION_EXIT:
        Utility.showToast(context, "You are outside the geofence");
        break;
    }

  }

}