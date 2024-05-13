package com.example.campingmaster.api.dto;

import com.google.gson.annotations.SerializedName;
public class LogInRequestDto {
    @SerializedName("memberId")
    private String memberId;
    @SerializedName("memberPw")
    private String memberPw;

    public LogInRequestDto(String memberId, String memberPw) {
        this.memberId = memberId;
        this.memberPw = memberPw;
    }
}
