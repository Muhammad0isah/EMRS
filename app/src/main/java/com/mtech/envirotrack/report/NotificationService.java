package com.mtech.envirotrack.report;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    public static final String ACTION_NOTIFICATION_RECEIVED = "com.mtech.envirotrack.ACTION_NOTIFICATION_RECEIVED";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            String title = notification.getTitle();
            String message = notification.getBody();

            Intent intent = new Intent(ACTION_NOTIFICATION_RECEIVED);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_MESSAGE, message);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Trigger an in-app message
            FirebaseInAppMessaging.getInstance().triggerEvent("notification_received");
        }
    }
}