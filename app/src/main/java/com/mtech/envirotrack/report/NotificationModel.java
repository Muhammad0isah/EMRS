package com.mtech.envirotrack.report;

public class NotificationModel {

    private String title;
    private String message;

    // No-argument constructor
    public NotificationModel() {}

    public NotificationModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}