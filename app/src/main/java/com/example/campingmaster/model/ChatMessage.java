package com.example.campingmaster.model;

public class ChatMessage {
    private String message;
    private String sender;
    private String timestamp;

    public ChatMessage(String message, String sender, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
