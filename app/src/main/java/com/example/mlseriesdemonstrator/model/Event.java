package com.example.mlseriesdemonstrator.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {

  private static final String TAG = "Event";
  private String eventId;
  private String title;
  private String date;
  private String startTime;
  private Location location;
  private String hostId;
  private String status;
  private String targetDepartment;
  private String targetCourse;
  private Date dateTime;

  public Event() {
    // ignore
  }

  public Event(
          String title,
          String date,
          String startTime,
          Location location,
          String hostId,
          String status,
          String eventId,
          String targetDepartment,
          String targetCourse
  ) {
    this.title = title;
    this.date = date;
    this.startTime = startTime;
    this.location = location;
    this.hostId = hostId;
    this.status = status;
    this.eventId = eventId;
    this.targetDepartment = targetDepartment;
    this.targetCourse = targetCourse;
    this.dateTime = parseDateTime(date, startTime);
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

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  private java.util.Date parseDateTime(String dateStr, String timeStr) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
      return dateFormat.parse(dateStr + " " + timeStr);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }
}
