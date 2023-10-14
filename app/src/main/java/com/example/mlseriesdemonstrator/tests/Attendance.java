package com.example.mlseriesdemonstrator.tests;

import com.google.firebase.firestore.FieldValue;

public class Attendance {

  String institutionalId;
  String studentName;
  String eventId;
  Object timestamp;

  public Attendance() {

  }

  public Attendance(String institutionalId, String studentName, String eventId) {
    this.institutionalId = institutionalId;
    this.studentName = studentName;
    this.eventId = eventId;
    this.timestamp = FieldValue.serverTimestamp();
  }

  public String getInstitutionalId() {
    return institutionalId;
  }

  public void setInstitutionalId(String institutionalId) {
    this.institutionalId = institutionalId;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public Object getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Object timestamp) {
    this.timestamp = timestamp;
  }


}
