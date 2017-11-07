package com.example.mks.gadgethunter.Items;

/**
 * Created by HP on 09-Oct-17.
 */

public class ThreadItems {
    String content, title, username;

    public ThreadItems() {
    }

    public String getContent() {

        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public ThreadItems(String content, String title, String username) {

        this.content = content;
        this.title = title;
        this.username = username;
    }
}
