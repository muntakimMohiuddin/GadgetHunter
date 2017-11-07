package com.example.mks.gadgethunter.Models;

/**
 * Created by kibria on 05/11/17.
 */

public class Notification {
    String notificationText;
    String postId;

    public Notification() {
    }

    public Notification(String notificationText, String postId) {
        this.notificationText = notificationText;
        this.postId = postId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public String getPostId() {
        return postId;
    }
}
