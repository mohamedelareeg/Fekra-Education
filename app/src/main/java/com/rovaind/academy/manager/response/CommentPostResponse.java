package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.CommentPost;

public class CommentPostResponse {
    private boolean error;
    private String message;

    @SerializedName("commentposts")
    private CommentPost commentposts;
    public CommentPostResponse() {

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

    public CommentPost getCommentposts() {
        return commentposts;
    }

    public void setCommentposts(CommentPost commentposts) {
        this.commentposts = commentposts;
    }
}