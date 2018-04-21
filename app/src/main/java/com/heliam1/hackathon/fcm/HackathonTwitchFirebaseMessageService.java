package com.heliam1.hackathon.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class HackathonTwitchFirebaseMessageService extends FirebaseMessagingService {
    /*
    private static final String JSON_KEY_AUTHOR = HackathonContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = HackathonContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = HackathonContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = HackathonContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static String LOG_TAG = HackathonTwitchFirebaseMessageService.class.getSimpleName();
    */

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with FCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options\

        // The Squawk server always sends just *data* messages, meaning that onMessageReceived when
        // the app is both in the foreground AND the background

        /*
        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            // Send a notification that you got a new message
            sendNotification(data);
            insertSquawk(data);

        } */
    }
}
