package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Post;

public class PostImageResponse {
    private boolean error;
    private String message;
    @SerializedName("postgallery")
    private Post.Gallery postgallery;
    public PostImageResponse() {

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

    public Post.Gallery getPostgallery() {
        return postgallery;
    }

    public void setPostgallery(Post.Gallery postgallery) {
        this.postgallery = postgallery;
    }
}