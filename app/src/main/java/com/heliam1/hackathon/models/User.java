package com.heliam1.hackathon.models;

import android.location.Location;

public class User {
    private Long studentId;
    private String pseudoname;
    private Location location;

    public User(long studentId, String pseudoname, Location location) {
        this.studentId = studentId;
        this.pseudoname = pseudoname;
        this.location = location;
    }

    public boolean hasId() {
        return (this.studentId != null);
    }

    public long getStudentId() {
        return studentId;
    }

    public String getPseudoname() {
        return pseudoname;
    }

    public Location getLocation() {
        return location;
    }
}
