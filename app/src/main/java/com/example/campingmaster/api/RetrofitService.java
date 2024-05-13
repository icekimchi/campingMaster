package com.example.campingmaster.api;

import com.example.campingmaster.api.dto.LogInRequestDto;
import com.example.campingmaster.api.dto.LogInResponseDto;
import com.example.campingmaster.api.dto.SignUpRequestDto;
import com.example.campingmaster.api.dto.SignUpResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitService {
    // 회원가입
    @POST("/members/signup")
    Call<SignUpResponseDto> userSignUp(@Body SignUpRequestDto data);

    @POST("/members/login")
    Call<LogInResponseDto> userLogIn(@Body LogInRequestDto data);
}