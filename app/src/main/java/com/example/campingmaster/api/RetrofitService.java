package com.example.campingmaster.api;

import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.api.gocamping.dto.LocationSearchDto;
import com.example.campingmaster.api.gocamping.dto.SearchKeywordRequestDto;
import com.example.campingmaster.api.gocamping.dto.SearchKeywordResponseDto;
import com.example.campingmaster.api.member.dto.LogInRequestDto;
import com.example.campingmaster.api.member.dto.LogInResponseDto;
import com.example.campingmaster.api.member.dto.SignUpRequestDto;
import com.example.campingmaster.api.member.dto.SignUpResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface RetrofitService {
    // 회원가입
    @POST("/members/signup")
    Call<SignUpResponseDto> userSignUp(@Body SignUpRequestDto data);

    @POST("/members/login")
    Call<LogInResponseDto> userLogIn(@Body LogInRequestDto data);

    @GET("/basedList")
    Call<List<CampingSiteDto>> searchBasedList();

    @GET("/detail/{siteName}")
    Call<CampingSiteDto> getSiteDetail(@Path("siteName") String siteName);

    @POST("/executeQuery")
    Call<List<CampingSiteDto>> searchQuery(@Body Map<String, Object> request);

    @POST("/filter")
    Call<List<CampingSiteDto>> searchWithFilters(@Body Map<String, List<String>> filters);

    @GET("/get-location")
    Call<List<CampingSiteDto>> searchByLocation(@Query("mapX") String latitude,
                                                @Query("mapY") String longitude,
                                                @Query("radius") String radius);
}