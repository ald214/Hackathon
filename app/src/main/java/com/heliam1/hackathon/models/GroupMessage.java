package com.heliam1.hackathon.models;

public class GroupMessage {
    Long id;
    long groupId;
    long userId;
    String userName;
    String zonedDateTime;
    String messageText;

    public GroupMessage(long id, long groupId, long userId, String userName, String zonedDateTime, String messageText) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.userName = userName;
        this.zonedDateTime = zonedDateTime;
        this.messageText = messageText;
    }

    public boolean hasId() {
        return (this.id != null);
    }

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getZonedDateTime() {
        return zonedDateTime;
    }

    public String getMessageText() {
        return messageText;
    }
}
