package com.heliam1.hackathon.ui;

import com.heliam1.hackathon.models.Group;

import java.util.List;

public interface MainView {
    public void displayGroups(List<Group> groups, List<Double> distancesAway);

    public void displayNoGroups();
}
