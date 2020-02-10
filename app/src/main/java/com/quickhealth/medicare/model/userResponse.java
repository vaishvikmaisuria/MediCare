package com.quickhealth.medicare.model;

import com.google.gson.annotations.SerializedName;

public class userResponse {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("token")
    private String token;
    @SerializedName("id")
    private String id;
    @SerializedName("userName")
    private String userName;


    public userResponse(String id, String token, Boolean success, String userName) {
        this.id = id;
        this.token = token;
        this.success = success;
        this.userName = userName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
