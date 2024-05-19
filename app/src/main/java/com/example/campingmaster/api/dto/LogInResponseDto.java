package com.example.campingmaster.api.dto;

import com.google.gson.annotations.SerializedName;

public class LogInResponseDto {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
