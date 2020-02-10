package com.quickhealth.medicare.webservice;


import android.se.omapi.Session;

import com.quickhealth.medicare.model.User;
import com.quickhealth.medicare.model.userResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private userResponse userResponse;

    public TokenInterceptor(userResponse session) {
        this.userResponse = session;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Response response = chain.proceed(chain.request());


        // if 'x-auth-token' is available into the response header
        // save the new token into session.The header key can be
        // different upon implementation of backend.
        String newToken = response.header("x-auth-token");
        if (newToken != null) {
            userResponse.setToken(newToken);
        }

        return response;
    }
}