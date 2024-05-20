package com.example.campingmaster.api.gocamping.dto;
import com.google.gson.annotations.SerializedName;

public class CampingSiteDto {
    @SerializedName("facltNm")
    private String name;
    @SerializedName("intro")
    private String description;
    @SerializedName("addr1")
    private String address;
    @SerializedName("tel")
    private String phoneNumber;
    @SerializedName("homepage")
    private String homepageUrl;
    @SerializedName("induty")
    private String category;
    @SerializedName("resveUrl")
    private String reserveUrl;
    @SerializedName("firstImageUrl")
    private String imgUrl;
    @SerializedName("animalCmgCl")
    private String isAnimal;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getReserveUrl() {
        return reserveUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getIsAnimal() {
        return isAnimal;
    }
}
