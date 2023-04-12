package com.rovaind.academy.manager.response;

import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Instructor;


public class InstructorResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("instructor")
    private Instructor instructor;

    public InstructorResponse(Boolean error, String message, Instructor instructor) {
        this.error = error;
        this.message = message;
        this.instructor = instructor;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Instructor getInstructor() {
        return instructor;
    }
}