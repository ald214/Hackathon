package com.heliam1.hackathon.repositories;

import com.heliam1.hackathon.models.GroupMessage;

import java.util.List;

import io.reactivex.Single;

public interface GroupMessageRepository {
    public Single<List<GroupMessage>> getGroupMessages();

    public Single<Long> saveGroupMessage(GroupMessage groupMessage);

    public Single<Long> deleteGroupMessage(GroupMessage groupMessage);
}
