package com.rovaind.academy.manager.response;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("success")
    private Integer success;

    @SerializedName("message")
    private String message;


    public ImageResponse(Integer success, String message) {
        this.success = success;
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
