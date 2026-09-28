package com.socialmedia.backend.model;

public class ProfileResponse {

    private String username;
    private String bio;
    private int followersCount;
    private int followingCount;

    public ProfileResponse() {
    }

    public ProfileResponse(
            String username,
            String bio,
            int followersCount,
            int followingCount) {

        this.username = username;
        this.bio = bio;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
}