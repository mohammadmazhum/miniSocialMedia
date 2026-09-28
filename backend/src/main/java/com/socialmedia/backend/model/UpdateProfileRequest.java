package com.socialmedia.backend.model;

public class UpdateProfileRequest {

    private String bio;

    public UpdateProfileRequest() {
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}