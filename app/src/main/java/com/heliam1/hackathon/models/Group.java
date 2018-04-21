package com.heliam1.hackathon.models;

import android.location.Location;

public class Group {
    private long id;
    private Location location;
    private String name;
    private int subjectCode;
    private int userCount;
    private int rating;

    public Group(long id, Location location, String name, int subjectCode, int userCount, int rating) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.subjectCode = subjectCode;
        this.userCount = userCount;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public int getSubjectCode() {
        return subjectCode;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getRating() {
        return rating;
    }
}
