package com.quickhealth.medicare.webservice;

import com.quickhealth.medicare.model.User;
import com.quickhealth.medicare.model.userLocation;
import com.quickhealth.medicare.model.userProfile;
import com.quickhealth.medicare.model.userResponse;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserProfile {

    @FormUrlEncoded
    @POST("profile/profilePicture")
    Call <String> storePath(@Field("id") String id, @Field("path") String path, @Header("Authorization") String authHeader);
    @GET("profile/getProfilePicture")
    Call<String> getPath(@Field("id") String id, @Header("Authorization") String authHeader);
    @GET("profile/getuserLocation")
    Call<userLocation> getuserLocation(@Field("id") String id, @Header("Authorization") String authHeader);
    @FormUrlEncoded
    @POST("profile/setuserLocation")
    Call<String> setuserLocation(@Field("id") String id, @Field("latitude") String latitude, @Field("latitude") String longitude, @Header("Authorization") String authHeader);

}