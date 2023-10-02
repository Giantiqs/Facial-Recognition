package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import com.example.mlseriesdemonstrator.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static final String EVENT_COLLECTION = "events";
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void scheduleEvent(Event event, Context context) {
        firestore.collection(EVENT_COLLECTION)
                .add(event)
                .addOnCompleteListener(task -> Utility.showToast(context, "Event scheduled"))
                .addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
    }

    public static void getEventsByHostId(String hostId, Context context, EventCallback callback) {
        CollectionReference eventsRef = firestore.collection(EVENT_COLLECTION);

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
                Utility.showToast(context, "Error fetching events: " + task.getException().getMessage());
                callback.onEventsRetrieved(null);
            }
        });
    }

    public interface EventCallback {
        void onEventsRetrieved(List<Event> events);
    }
}
