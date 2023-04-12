package com.rovaind.academy.manager.response;

import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Code;

public class CodeResponse {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("codes")
    private Code codes;

    public CodeResponse(Boolean error, String message, Code codes) {
        this.error = error;
        this.message = message;
        this.codes = codes;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Code getCode() {
        return codes;
    }

}
