package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;

import com.example.mlseriesdemonstrator.model.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EventManager {

  private static final String EVENT_COLLECTION = "events";

  public interface NearestEventsCallback {
    void onEventsRetrieved(List<Event> events);
  }

  public interface EventCallback {
    void onEventsRetrieved(List<Event> events);
  }

  public static void scheduleEvent(Event event, Context context) {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    // Get the current date and time
    Date currentDate = new Date(System.currentTimeMillis());

    // Check if the event's dateTime is in the past
    if (event.getDateTime() != null && event.getDateTime().before(currentDate)) {
      Utility.showToast(context, "Bro, you cannot go back to the past smh.");
      return;
    }

    // Query to check if an event with the same dateTime and location exists
    fireStore.collection(EVENT_COLLECTION)
            .whereEqualTo("dateTime", event.getDateTime())
            .whereEqualTo("location", event.getLocation())
            .get()
            .addOnCompleteListener(queryTask -> {
              if (queryTask.isSuccessful()) {
                // Check if any matching events were found
                if (!queryTask.getResult().isEmpty()) {
                  // Event with the same dateTime and location exists
                  Utility.showToast(
                          context,
                          "An event at the same dateTime and location already exists."
                  );
                } else {
                  // No matching events found, proceed with scheduling
                  fireStore.collection(EVENT_COLLECTION)
                          .add(event)
                          .addOnCompleteListener(addTask -> {
                            if (addTask.isSuccessful()) {
                              Utility.showToast(context, "Event scheduled");
                            } else {
                              Utility.showToast(
                                      context,
                                      "Failed to schedule event: " + Objects
                                              .requireNonNull(
                                                      addTask.getException()
                                              ).getLocalizedMessage()
                              );
                            }
                          });
                }
              } else {
                Utility.showToast(
                        context, "Query failed: " + Objects
                                .requireNonNull(queryTask
                                        .getException()).getLocalizedMessage()
                );
              }
            });
  }


  public static void getEventsByHostId(String hostId, Context context, EventCallback callback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = fireStore.collection(EVENT_COLLECTION);

    // Create a query to filter events by hostId
    Query query = eventsRef.whereEqualTo("hostId", hostId);

    query.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        // List to store the matching events
        List<Event> events = new ArrayList<>();

        for (QueryDocumentSnapshot document : task.getResult()) {
          // Convert Firestore document to Event object
          Event event = document.toObject(Event.class);
          events.add(event);
        }

        // Pass the list of events to the callback
        callback.onEventsRetrieved(events);
      } else {
        Utility.showToast(
                context,
                "Error fetching events: " + Objects.requireNonNull(
                        task.getException()).getMessage()
        );

        callback.onEventsRetrieved(null);
      }
    });
  }

  public static void getNearestEvents(Context context, NearestEventsCallback eventsCallback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    // Get the current date and time
    Date currentDate = new Date(System.currentTimeMillis());

    // Query for events scheduled after the current date and time
    eventsCollection.whereGreaterThanOrEqualTo("dateTime", currentDate)
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .limit(10) // Fetch more results
            .get()
            .addOnCompleteListener(queryTask -> {
              if (queryTask.isSuccessful()) {
                QuerySnapshot querySnapshot = queryTask.getResult();
                List<Event> nearestEvents = new ArrayList<>();

                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                  Event event = document.toObject(Event.class);
                  if (event != null) {
                    nearestEvents.add(event);
                  }
                }

                // Filter and return the 5 nearest events
                List<Event> nearest5Events = filterNearestEvents(nearestEvents);
                eventsCallback.onEventsRetrieved(nearest5Events);
              } else {
                // Query failed
                Utility.showToast(context, "Query failed");
                eventsCallback.onEventsRetrieved(Collections.emptyList());
              }
            });
  }

  private static List<Event> filterNearestEvents(List<Event> events) {
    List<Event> nearestEvents = new ArrayList<>();
    for (int i = 0; i < Math.min(5, events.size()); i++) {
      nearestEvents.add(events.get(i));
    }
    return nearestEvents;
  }
}
