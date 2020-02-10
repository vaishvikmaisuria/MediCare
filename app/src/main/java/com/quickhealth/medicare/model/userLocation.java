package com.quickhealth.medicare.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

public class userLocation {

    @SerializedName("id")
    private String id;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public userLocation(String id, String latitude, String longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
