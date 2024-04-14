package com.example.capsules.models;

import java.util.Date;

public class CapsuleModel {
    private String id;
    private String title;
    private String description;
    private String location;
    private Date creation_date;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Constructor
    public CapsuleModel(String id, String title, String description, String location, Date creation_date, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.creation_date = creation_date;
        this.userId = userId;
    }
}
