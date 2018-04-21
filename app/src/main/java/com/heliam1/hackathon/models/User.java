package com.heliam1.hackathon.models;

import android.location.Location;

public class User {
    private long studentId;
    private String pseudoname;
    private Location location;

    public User(long studentId, String pseudoname, Location location) {
        this.studentId = studentId;
        this.pseudoname = pseudoname;
        this.location = location;
    }
}
