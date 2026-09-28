package com.socialmedia.backend.model;

import java.time.LocalDateTime;

public class Comment {


    private String authorUsername;
    private String text;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(String username, String text, LocalDateTime createdAt) {
        this.authorUsername = username;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}