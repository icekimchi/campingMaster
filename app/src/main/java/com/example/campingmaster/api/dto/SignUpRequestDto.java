package com.example.campingmaster.api.dto;

import com.google.gson.annotations.SerializedName;

public class SignUpRequestDto {
    @SerializedName("memberId")
    private String memberId;
    @SerializedName("memberPw")
    private String memberPw;
    @SerializedName("email")
    private String email;

    public SignUpRequestDto(String memberId, String memberPw, String email) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.email = email;
    }
}
