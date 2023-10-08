package com.example.mlseriesdemonstrator.model;

import com.example.mlseriesdemonstrator.tests.Location;
import com.google.type.Date;

import java.sql.Time;

public class Event {

    private String eventId;
    private String title;
    private String date;
    private String startTime;
    private String location;
    private String hostId;
    private Location mLocation; // asikaso dis later

    public Event() {

    }

    public Event(
            String title,
            String date,
            String startTime,
            String location,
            String hostId,
            String eventId
    ) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.hostId = hostId;
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
