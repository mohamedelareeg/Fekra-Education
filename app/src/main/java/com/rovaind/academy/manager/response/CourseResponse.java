package com.rovaind.academy.manager.response;



public class CourseResponse {
    private boolean error;
    private String message;

    public CourseResponse() {

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
}