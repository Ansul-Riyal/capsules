package com.example.capsules.models;

import java.util.Date;

public class MessageModel {
    private String capsule_id;
    private String content;
    private String receiver_user_id;
    private String sender_user_id;

    public String getCapsule_id() {
        return capsule_id;
    }

    public void setCapsule_id(String capsule_id) {
        this.capsule_id = capsule_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver_user_id() {
        return receiver_user_id;
    }

    public void setReceiver_user_id(String receiver_user_id) {
        this.receiver_user_id = receiver_user_id;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private Date timestamp;

    // Constructor
    public MessageModel(String capsule_id, String content, String receiver_user_id, String sender_user_id, Date timestamp) {
        this.capsule_id = capsule_id;
        this.content = content;
        this.receiver_user_id = receiver_user_id;
        this.sender_user_id = sender_user_id;
        this.timestamp = timestamp;
    }

    // Getters and setters
    // Add getters and setters here
}