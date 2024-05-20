package com.example.campingmaster.api.gocamping.dto;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchKeywordResponseDto {
    @SerializedName("campingSites")
    private List<CampingSiteDto> campingSites;

    public List<CampingSiteDto> getCampingSites() {
        return campingSites;
    }

    public void setCampingSites(List<CampingSiteDto> campingSites) {
        this.campingSites = campingSites;
    }
}