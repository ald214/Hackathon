package com.heliam1.hackathon.models;

public class GroupMessage {
    long id;
    long groupId;
    long userId;
    long userName;
    String zonedDateTime;
    String messageText;

    public GroupMessage(long id, long groupId, long userId, long userName, String zonedDateTime, String messageText) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.userName = userName;
        this.zonedDateTime = zonedDateTime;
        this.messageText = messageText;
    }
}
