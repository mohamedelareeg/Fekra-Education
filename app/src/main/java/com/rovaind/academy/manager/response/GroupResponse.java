package com.rovaind.academy.manager.response;


import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.group;

public class GroupResponse {
    private boolean error;
    private String message;
    @SerializedName("groups")
    private group group;
    public GroupResponse() {

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

    public com.rovaind.academy.model.group getGroup() {
        return group;
    }

    public void setGroup(com.rovaind.academy.model.group group) {
        this.group = group;
    }
}