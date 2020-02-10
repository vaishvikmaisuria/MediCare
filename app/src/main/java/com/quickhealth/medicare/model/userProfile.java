package com.quickhealth.medicare.model;

import com.google.gson.annotations.SerializedName;

public class userProfile {

    @SerializedName("id")
    private String id;

    @SerializedName("path")
    private String path;

    public userProfile(String id, String path) {
        this.id = id;
        this.path = path;

    }
}
