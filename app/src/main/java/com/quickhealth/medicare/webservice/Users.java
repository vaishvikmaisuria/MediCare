package com.quickhealth.medicare.webservice;


import com.quickhealth.medicare.model.User;
import com.quickhealth.medicare.model.userResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Users {

    @GET("users/all")
    Call<List<User>> getAllUsers();
    @FormUrlEncoded
    @POST("users/login")
    Call <userResponse> loginUser(@Field("emailAddress") String emailAddress, @Field("password") String password);
    @FormUrlEncoded
    @POST("users/register")
    Call <User> registerUser(@Field("name") String name, @Field("userName") String userName, @Field("emailAddress") String emailAddress, @Field("password") String password);
    @FormUrlEncoded
    @GET("users/forgetPassword")
    Call<String> forgetPassword(@Field("emailAddress") String emailAddress);
}