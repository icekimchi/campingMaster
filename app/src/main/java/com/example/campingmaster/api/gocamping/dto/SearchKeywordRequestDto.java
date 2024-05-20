package com.example.campingmaster.api.gocamping.dto;

import com.google.gson.annotations.SerializedName;

public class SearchKeywordRequestDto {
    @SerializedName("keyword")
    private String keyword;

    public SearchKeywordRequestDto(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
