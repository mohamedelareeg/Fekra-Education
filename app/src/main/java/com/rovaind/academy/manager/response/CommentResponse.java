package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Comment;

public class CommentResponse {
    private boolean error;
    private String message;

    @SerializedName("comment")
    private Comment comment;
    public CommentResponse() {

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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}