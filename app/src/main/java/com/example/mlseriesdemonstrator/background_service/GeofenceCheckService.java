package com.example.mlseriesdemonstrator.background_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class GeofenceCheckService extends Service {

  private static final String TAG = "GeofenceCheckService";
  private Thread thread;
  private volatile boolean stopThread = false;
  private static final int NOTIFICATION_ID = 1;
  private static final String CHANNEL_ID = "geofence_check_channel";
  private final static long CHECK_INTERVAL = 600000;

  @SuppressLint("ForegroundServiceType")
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startForeground(NOTIFICATION_ID, createNotification());
    startThread();
    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void startThread() {

    Event event = Utility.getCurrentEvent();
    thread = new Thread(() -> {
      while (!stopThread) {
        checkEventStatus();
        if (!event.getLocation().getLocationAddress().equals("Online Event")) {
          checkGeofence();
        }
        isLoggedIn();
        try {
          Thread.sleep(CHECK_INTERVAL);
        } catch (InterruptedException e) {
          Log.e(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
        }
      }
    });

    thread.start();
  }

  private void isLoggedIn() {

    Event event = Utility.getCurrentEvent();
    User user = Utility.getUser();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    if (firebaseUser == null) {
      // Student is outside the geofence
      Log.d(TAG, "Student logged out");
      stopThread(); // Stop the thread when the student logged out
      showNotification("Logout Alert", "You have logged out of your account");
      EventManager.deleteAttendance(user.getInstitutionalID(), event.getEventId());

      Intent serviceIntent = new Intent(this, GeofenceCheckService.class);
      stopService(serviceIntent);
    }
  }

  private void checkEventStatus() {
    Event event = Utility.getCurrentEvent();
    EventManager.getEventByEventId(event.getEventId(), this, events -> {
      Event event1 = events.get(0);

      if (!event1.getStatus().equals("started")) {
        stopThread();
        showNotification("Event has ended", "Thank you for attending the event!");

        Intent serviceIntent = new Intent(this, GeofenceCheckService.class);
        stopService(serviceIntent);
      }
    });
  }


  private void stopThread() {
    stopThread = true;

  }

  private Notification createNotification() {
    createNotificationChannel();

    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
    );

    return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Geofence Check")
            .setContentText("Checking if you are inside the geofence...")
            .setSmallIcon(R.drawable.apptextlogoonly2)
            .setContentIntent(pendingIntent)
            .build();
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
              CHANNEL_ID,
              "Geofence Check Channel",
              NotificationManager.IMPORTANCE_DEFAULT
      );

      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  private void checkGeofence() {
    Event event = Utility.getCurrentEvent();
    User user = Utility.getUser();

    if (event != null && user != null) {
      getUserLocation(userLocation -> {
        LatLng geofenceLocation = new LatLng(
                event.getLocation().getCustomLatLng().getLatitude(),
                event.getLocation().getCustomLatLng().getLongitude()
        );

        float[] results = new float[1];
        Location.distanceBetween(
                userLocation.latitude, userLocation.longitude,
                geofenceLocation.latitude, geofenceLocation.longitude,
                results
        );

        float geofenceRadius = event.getLocation().getGeofenceRadius();

        boolean insideGeofence = results[0] <= geofenceRadius;

        if (!insideGeofence) {
          // Student is outside the geofence
          Log.d(TAG, "Student is outside the geofence");
          stopThread(); // Stop the thread when the student is outside the geofence
          showNotification("Geofence Alert", "You have exited the geofence!");
          EventManager.deleteAttendance(user.getInstitutionalID(), event.getEventId());

          Intent serviceIntent = new Intent(this, GeofenceCheckService.class);
          stopService(serviceIntent);
        }
      });
    } else {
      Log.d(TAG, "NO EVENT or USER");
    }
  }

  private void getUserLocation(LocationCallback callback) {
    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
      if (location != null) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        callback.onLocationReceived(userLocation);
      } else {
        Log.e(TAG, "User location is null");
        callback.onLocationReceived(null);
      }
    }).addOnFailureListener(e -> {
      Log.e(TAG, "Error getting location: " + e.getLocalizedMessage());
      callback.onLocationReceived(null);
    });
  }

  private void showNotification(String title, String message) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.apptextlogoonly2)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    notificationManager.notify(NOTIFICATION_ID, builder.build());
  }

  private interface LocationCallback {
    void onLocationReceived(LatLng userLocation);
  }

  @Override
  public void onDestroy() {
    stopThread();
    super.onDestroy();
  }
}
