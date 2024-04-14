package com.example.capsules.models;

import java.util.Date;

public class PostModel {
    private String capsule_id;
    private String title;
    private String content;
    private Date creation_date;
    private String user_id;

    public String getCapsule_id() {
        return capsule_id;
    }

    public void setCapsule_id(String capsule_id) {
        this.capsule_id = capsule_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    // Constructor
    public PostModel(String capsule_id, String title, String content, Date creation_date, String user_id) {
        this.capsule_id = capsule_id;
        this.title = title;
        this.content = content;
        this.creation_date = creation_date;
        this.user_id = user_id;
    }

    // Getters and setters
    // Add getters and setters here
}
