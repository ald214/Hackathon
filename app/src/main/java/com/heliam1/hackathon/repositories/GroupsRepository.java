package com.heliam1.hackathon.repositories;

import com.heliam1.hackathon.models.Group;

import java.util.List;

import io.reactivex.Single;

public interface GroupsRepository {
    public Single<List<Group>> getGroups();

    public Single<Long> saveGroup(Group group);

    public Single<Long> deleteGroup(Group group);

    // Single<List<Task>> searchTasks(String title);
}
