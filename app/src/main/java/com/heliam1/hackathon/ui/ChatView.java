package com.heliam1.hackathon.ui;

import com.heliam1.hackathon.models.GroupMessage;

import java.util.List;

public interface ChatView {
    public void displayGroupMessages(List<GroupMessage> groupMessages);

    public void displayNoGroupMessages();

    public void displayToast(String message);
}
