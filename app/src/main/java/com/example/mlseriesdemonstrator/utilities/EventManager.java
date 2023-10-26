package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.util.Log;

import com.example.mlseriesdemonstrator.model.Attendance;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

  private static final String TAG = "EventManager";
  private static final String EVENT_COLLECTION = "events";

  public interface NearestEventsCallback {
    void onEventsRetrieved(List<Event> events);
  }

  public interface EventCallback {
    void onEventsRetrieved(List<Event> events);
  }

  public interface StartedEventsCallback {
    void onStartedEventsRetrieved(List<Event> events);
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
                          .add(event) // Add the event to fire store
                          .addOnCompleteListener(addTask -> {
                            if (addTask.isSuccessful()) {
                              // Set the event ID in the Event object
                              event.setEventId(addTask.getResult().getId());

                              Utility.addEventIds(addTask.getResult().getId());

                              // Update the document with the event ID
                              fireStore.collection(EVENT_COLLECTION)
                                      .document(addTask.getResult().getId())
                                      .update("eventId", addTask.getResult().getId())
                                      .addOnSuccessListener(aVoid -> Utility.showToast(context, "Event scheduled"))
                                      .addOnFailureListener(e -> Utility.showToast(
                                              context,
                                              "Failed to update event ID: " + e.getLocalizedMessage()
                                      ));
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
          // Convert fire store document to Event object
          Event event = document.toObject(Event.class);
          events.add(event);
        }

        // Pass the list of events to the callback
        callback.onEventsRetrieved(events);
      } else {
        String errorMessage = task.getException() != null
                ? task.getException().getMessage()
                : "Unknown error";

        assert errorMessage != null;
        Log.d(TAG, errorMessage);

        callback.onEventsRetrieved(null);
        Utility.showToast(context, "You have no events");
      }
    });
  }

  public static void getAllEvents(Context context, EventCallback callback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventRef = fireStore.collection(EVENT_COLLECTION);

    eventRef.get()
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<Event> events = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                  Event event = documentSnapshot.toObject(Event.class);
                  if (event != null) {
                    events.add(event);
                  }
                }
                callback.onEventsRetrieved(events);
              }
            }).addOnFailureListener(e -> Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
  }

  public static void getNearestUpcomingEvents(Context context, NearestEventsCallback eventsCallback) {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    // Get the current date and time
    Date currentDate = new Date(System.currentTimeMillis());

    // Query for events scheduled after the current date and time with status "upcoming"
    eventsCollection.whereGreaterThanOrEqualTo("dateTime", currentDate)
            .whereEqualTo("status", "upcoming")  // Add this line to filter by status
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

  public static void getUpcomingStartedEvents(Context context, NearestEventsCallback eventsCallback) {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    // Get the current date and time
    Date currentDate = new Date(System.currentTimeMillis());
    ArrayList<String> eventStatus = new ArrayList<>();

    eventStatus.add("upcoming");
    eventStatus.add("started");

    // Query for events scheduled after the current date and time with status "upcoming"
    eventsCollection.whereGreaterThanOrEqualTo("dateTime", currentDate)
            .whereIn("status", eventStatus)  // Add this line to filter by status
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

  public static void getStartedEvents(Context context, User user, StartedEventsCallback callback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);
    String started = "started";

    // Create a list of target courses to filter by
    List<String> targetCourses = new ArrayList<>();
    targetCourses.add(user.getCourse());
    targetCourses.add("ALL"); // Include "ALL" as a possible target course

    List<String> targetDepartments = new ArrayList<>();
    targetDepartments.add(user.getDepartment());
    targetDepartments.add("ALL");

    eventsCollection
            .whereEqualTo("status", started)
            .whereIn("targetCourse", targetCourses)
            .whereIn("targetDepartment", targetDepartments)
            .get()
            .addOnCompleteListener(querySnapshotTask -> {
              if (querySnapshotTask.isSuccessful()) {
                QuerySnapshot snapshots = querySnapshotTask.getResult();
                List<Event> startedEvents = new ArrayList<>();

                for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
                  Event event = snapshot.toObject(Event.class);
                  if (event != null) {
                    startedEvents.add(event);
                  }
                }
                callback.onStartedEventsRetrieved(startedEvents);

              } else {
                String errorMessage = querySnapshotTask.getException() != null
                        ? querySnapshotTask.getException().getMessage()
                        : "Unknown error";
                Utility.showToast(context, "Query failed: " + errorMessage);
                callback.onStartedEventsRetrieved(Collections.emptyList());
                assert errorMessage != null;
                Log.d(TAG, errorMessage);
              }
            });
  }

  public static void getNearestEventsByUserCourse(Context context, User user, NearestEventsCallback eventsCallback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    // Get the current date and time
    Date currentDate = new Date(System.currentTimeMillis());

    // Create a list of target courses to filter by
    List<String> targetCourses = new ArrayList<>();
    targetCourses.add(user.getCourse());
    targetCourses.add("ALL"); // Include "ALL" as a possible target course

    List<String> targetDepartments = new ArrayList<>();
    targetDepartments.add(user.getDepartment());
    targetDepartments.add("ALL");

    // Query for events scheduled after the current date and time and with the matching department and target course
    eventsCollection.whereGreaterThanOrEqualTo("dateTime", currentDate)
            .whereIn("targetCourse", targetCourses)
            .whereIn("targetDepartment", targetDepartments) // Add department filter
            .whereEqualTo("status", "upcoming")
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .limit(10)
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
                String errorMessage = queryTask.getException() != null
                        ? queryTask.getException().getMessage()
                        : "Unknown error";
                Utility.showToast(context, "Query failed: " + errorMessage);
                eventsCallback.onEventsRetrieved(Collections.emptyList());
                assert errorMessage != null;
                Log.d(TAG, errorMessage);
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

  public static void startEvent(Event event, Context context) {

    String started = "started";
    String eventId = event.getEventId();

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    // Update the status field of the event to "started"
    eventsCollection.document(eventId)
            .update("status", started)
            .addOnSuccessListener(aVoid -> {
              // Status updated successfully
              Utility.showToast(context, "Event started");
            })
            .addOnFailureListener(e -> {
              // Failed to update the status
              Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
            });
  }

  public static void cancelEvent(Event event, Context context) {

    String cancelled = "cancelled";
    String eventId = event.getEventId();

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

    eventsCollection.document(eventId)
            .update("status", cancelled)
            .addOnSuccessListener(aVoid -> {
              // Status updated successfully
              Utility.showToast(context, "Event cancelled");
            })
            .addOnFailureListener(e -> {
              // Failed to update the status
              Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
            });
  }

  public static void getEventByEventId(String eventId, Context context, EventCallback callback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    DocumentReference eventRef = fireStore.collection(EVENT_COLLECTION).document(eventId);

    eventRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          // Convert the Firestore document to an Event object
          Event event = document.toObject(Event.class);
          if (event != null) {
            List<Event> events = new ArrayList<>();
            events.add(event);
            // Pass the event to the callback
            callback.onEventsRetrieved(events);
          }
        } else {
          // Event with the given eventId does not exist
          callback.onEventsRetrieved(Collections.emptyList());
          Utility.showToast(context, "Event not found");
        }
      } else {
        String errorMessage = task.getException() != null
                ? task.getException().getMessage()
                : "Unknown error";
        assert errorMessage != null;
        Log.d(TAG, errorMessage);
        callback.onEventsRetrieved(Collections.emptyList());
        Utility.showToast(context, "Failed to fetch event: " + errorMessage);
      }
    });
  }

}
