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
    private Date date;
    private Time startTime;
    private Time endTime;
    private Location location;
    private boolean hasEnded;

    public Event() {

    }

}
