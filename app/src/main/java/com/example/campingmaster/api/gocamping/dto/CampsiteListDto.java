package com.example.campingmaster.api.gocamping.dto;

public class CampsiteListDto {
    String citeName;
    String imgUrl;
    String lineIntro;
    String category;

    public CampsiteListDto(String citeName, String imgUrl, String lineIntro, String category) {
        this.citeName = citeName;
        this.imgUrl = imgUrl;
        this.lineIntro = lineIntro;
        this.category = category;
    }
}
