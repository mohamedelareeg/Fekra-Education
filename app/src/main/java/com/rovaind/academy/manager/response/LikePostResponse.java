package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.LikePost;

public class LikePostResponse {
    private boolean error;
    private String message;

    @SerializedName("likepost")
    private LikePost likePost;
    public LikePostResponse() {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LikePost getLikePost() {
        return likePost;
    }

    public void setLikePost(LikePost likePost) {
        this.likePost = likePost;
    }
}