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
  private boolean started;
  private boolean ended;
  private boolean ongoing;
  private String targetDepartment;
  private String targetCourse;
  private Location mLocation; // asikaso dis later

  public Event() {
    // ignore
  }

  public Event(
          String title,
          String date,
          String startTime,
          String location,
          String hostId,
          String eventId,
          String targetDepartment,
          String targetCourse
  ) {
    this.title = title;
    this.date = date;
    this.startTime = startTime;
    this.location = location;
    this.hostId = hostId;
    this.eventId = eventId;
    this.targetDepartment = targetDepartment;
    this.targetCourse = targetCourse;
    this.started = false;
    this.ended = false;
    this.ongoing = false;
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

  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isEnded() {
    return ended;
  }

  public void setEnded(boolean ended) {
    this.ended = ended;
  }

  public boolean isOngoing() {
    return ongoing;
  }

  public void setOngoing(boolean ongoing) {
    this.ongoing = ongoing;
  }

  public String getTargetDepartment() {
    return targetDepartment;
  }

  public void setTargetDepartment(String targetDepartment) {
    this.targetDepartment = targetDepartment;
  }

  public String getTargetCourse() {
    return targetCourse;
  }

  public void setTargetCourse(String targetCourse) {
    this.targetCourse = targetCourse;
  }
}
