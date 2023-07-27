package com.example.sonnetapp.models;

public class Followers {

    String userId,followerId;
    boolean isFollowing;

    public Followers(String userId, String followerId, boolean isFollowing) {
        this.userId = userId;
        this.followerId = followerId;
        this.isFollowing = isFollowing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
