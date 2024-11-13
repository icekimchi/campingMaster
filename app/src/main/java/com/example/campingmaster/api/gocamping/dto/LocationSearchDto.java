package com.example.campingmaster.api.gocamping.dto;

import com.google.gson.annotations.SerializedName;


public class LocationSearchDto {

    @SerializedName("mapX")
    private String mapX;
    @SerializedName("mapY")
    private String mapY;
    @SerializedName("radius")
    private String radius;

    public LocationSearchDto(Double mapX, Double mapY, int radius) {
        this.mapX = Double.toString(mapX);
        this.mapY = Double.toString(mapY);
        this.radius = Integer.toString(radius);
    }

    public LocationSearchDto(String mapX, String mapY, String radius) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.radius = radius;
    }

    public String getMapX() {
        return mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public String getRadius() {
        return radius;
    }

}
