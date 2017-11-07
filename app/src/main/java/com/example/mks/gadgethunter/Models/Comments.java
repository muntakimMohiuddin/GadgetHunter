package com.example.mks.gadgethunter.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by HP on 08-Oct-17.
 */
@IgnoreExtraProperties
public class Comments {
    public String commenterName, content,commenterUid;


    public Comments() {
    }

    public Comments(String commenterName, String content, String commenterUid) {
        this.commenterName = commenterName;
        this.content = content;
        this.commenterUid = commenterUid;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "commenterName='" + commenterName + '\'' +
                ", content='" + content + '\'' +
                ", commenterUid='" + commenterUid + '\'' +
                '}';
    }

    public String getCommenterUid() {

        return commenterUid;
    }
}
