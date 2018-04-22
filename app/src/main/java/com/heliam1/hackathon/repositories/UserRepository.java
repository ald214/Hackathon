package com.heliam1.hackathon.repositories;

import com.heliam1.hackathon.models.User;

import java.util.List;

import io.reactivex.Single;

public interface UserRepository {
    public Single<List<User>> getUser();

    public Single<Long> saveUser(User user);

    public Single<Long> deleteUser(User user);

    // Single<List<Task>> searchTasks(String title);
}
