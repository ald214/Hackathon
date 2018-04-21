package com.heliam1.hackathon.models;

public class UserMessage {
    long id;
    long fromUserId;
    long toUserId;
    long fromUserName;
    long toUserName;
    String zonedDateTime;
    String messageText;

    public UserMessage(long id, long fromUserId, long toUserId, long fromUserName, long toUserName, String zonedDateTime, String messageText) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.zonedDateTime = zonedDateTime;
        this.messageText = messageText;
    }
}
