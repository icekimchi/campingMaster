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

    public String getCiteName() {
        return citeName;
    }

    public void setCiteName(String citeName) {
        this.citeName = citeName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLineIntro() {
        return lineIntro;
    }

    public void setLineIntro(String lineIntro) {
        this.lineIntro = lineIntro;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
