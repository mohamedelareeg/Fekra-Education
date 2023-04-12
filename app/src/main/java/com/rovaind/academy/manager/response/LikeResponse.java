package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Like;

public class LikeResponse {
    private boolean error;
    private String message;

    @SerializedName("like")
    private Like like;
    public LikeResponse() {

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

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }
}