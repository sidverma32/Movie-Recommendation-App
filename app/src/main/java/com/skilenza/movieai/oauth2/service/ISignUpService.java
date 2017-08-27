package com.skilenza.movieai.oauth2.service;

import com.skilenza.movieai.oauth2.request.SignUpRequest;
import com.skilenza.movieai.oauth2.response.SignUpResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public interface ISignUpService {
    @POST("/user/signup")
    void signUp(@Body SignUpRequest signUpRequest,
                Callback<SignUpResponse> signUpResponseCallback);
}
