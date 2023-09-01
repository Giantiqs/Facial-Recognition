package com.example.mlseriesdemonstrator.model;

import android.location.Location;

import com.google.type.Date;

import java.sql.Time;

public class Event {

    /*

        THESE VARIABLES ARE SUBJECT TO CHANGE LATER
        SO PUT THE GETTER AND SETTER LATER OKE TY

     */

    private int ID;
    private String title;
    private String date;
    private String startTime;
    private Time endTime;
    private String location;
    private boolean hasEnded;

    public Event() {

    }

    public Event(String title, String date, String startTime, String location) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
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
}
