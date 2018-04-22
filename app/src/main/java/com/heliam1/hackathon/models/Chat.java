package com.heliam1.hackathon.models;

public class Chat {
    private String text;
    private String name;

    public Chat() {}

    public Chat(String groupMessageString, String groupMessageUserName) {
        this.text = groupMessageString;
        this.name = groupMessageUserName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
