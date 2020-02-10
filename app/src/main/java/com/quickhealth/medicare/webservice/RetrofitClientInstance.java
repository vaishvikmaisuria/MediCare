package com.quickhealth.medicare.webservice;

import android.content.Context;
import android.util.Log;

import com.quickhealth.medicare.CurrentUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://13745450.ngrok.io";
    private static final String BASE_Token = CurrentUser.getUserToken();

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

//            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request newRequest  = chain.request().newBuilder()
//                            .addHeader("Authorization", "Bearer " + BASE_Token)
//                            .build();
//                    return chain.proceed(newRequest);
//                }
//            }).build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
