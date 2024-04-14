package com.example.capsules.models;

public class FriendModel {
    private String user_id;
    private String friend_user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(String friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    // Constructor
    public FriendModel(String user_id, String friend_user_id) {
        this.user_id = user_id;
        this.friend_user_id = friend_user_id;
    }

    // Getters and setters
    // Add getters and setters here
}
