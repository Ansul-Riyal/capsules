package com.example.capsules.models;

import java.util.Date;

public class UserModel {
    private String id;
    private String name;
    private String email;
    private Date joined;
    private String location;
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Constructor
    public UserModel(String id, String name, String email, Date joined, String location, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joined = joined;
        this.location = location;
        this.avatar = avatar;
    }
}
