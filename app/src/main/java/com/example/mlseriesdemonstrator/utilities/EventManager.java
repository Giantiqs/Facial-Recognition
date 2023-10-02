package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;

import com.example.mlseriesdemonstrator.model.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventManager {

    private static final String EVENT_COLLECTION = "events";

    public interface NearestEventCallback {
        void onEventRetrieved(Event event);
    }

    public static void scheduleEvent(Event event, Context context) {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        // Query to check if an event with the same date, time, and location exists
        fireStore.collection(EVENT_COLLECTION)
                .whereEqualTo("date", event.getDate())
                .whereEqualTo("startTime", event.getStartTime())
                .whereEqualTo("location", event.getLocation())
                .get()
                .addOnCompleteListener(queryTask -> {
                    if (queryTask.isSuccessful()) {
                        // Check if any matching events were found
                        if (!queryTask.getResult().isEmpty()) {
                            // Event with the same date, time, and location exists
                            Utility.showToast(
                                    context,
                                    "An event at the same date, time, and location already exists."
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
                    // Convert Fire Store document to Event object
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

    public interface EventCallback {
        void onEventsRetrieved(List<Event> events);
    }

    public static void getNearestEvent(Context context, NearestEventCallback eventCallback) {

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference eventsCollection = fireStore.collection(EVENT_COLLECTION);

        // Get the current date and time in your format
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        Date currentDate = new Date(System.currentTimeMillis());
        String currentDateTime = dateFormat.format(currentDate);

        // Query for events scheduled after the current date and time
        eventsCollection.whereGreaterThanOrEqualTo("date", currentDateTime)
                .orderBy("date", Query.Direction.ASCENDING)
                .orderBy("startTime", Query.Direction.ASCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(queryTask -> {
                    if (queryTask.isSuccessful()) {
                        QuerySnapshot querySnapshot = queryTask.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // Get the nearest event (the first one in the sorted list)
                            DocumentSnapshot nearestEventDoc = querySnapshot.getDocuments().get(0);
                            Event nearestEvent = nearestEventDoc.toObject(Event.class);
                            eventCallback.onEventRetrieved(nearestEvent);
                        } else {
                            // No events found after the current date and time
                            eventCallback.onEventRetrieved(null);
                        }
                    } else {
                        // Query failed
                        Utility.showToast(context, "Query failed");
//                        Log.d("LINK THANKS", Objects.requireNonNull(queryTask.getException().getLocalizedMessage()));
                        eventCallback.onEventRetrieved(null);
                    }
                });
    }

}
