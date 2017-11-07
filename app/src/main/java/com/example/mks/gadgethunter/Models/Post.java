package com.example.mks.gadgethunter.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by HP on 08-Oct-17.
 */
@IgnoreExtraProperties
public class Post {
    public String uid, username;
    public String title;
    public String content;
    public String imageUrl;
    public String numberOfComments;
    public String postId;

    public Post() {

    }

    public Post(String uid, String username, String title, String content, String imageUrl, String numberOfComments, String postId) {
        this.uid = uid;
        this.username = username;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.numberOfComments = numberOfComments;
        this.postId = postId;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNumberOfComments() {
        return numberOfComments;
    }

    public String getPostId() {
        return postId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", numberOfComments='" + numberOfComments + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
