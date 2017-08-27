package com.skilenza.movieai.oauth2.client;

import com.skilenza.movieai.oauth2.constant.OauthConstant;
import com.skilenza.movieai.oauth2.service.ISignUpService;

import retrofit.RestAdapter;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class SignUpService {

    private ISignUpService _signUpService;


    public ISignUpService signUpService() {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(OauthConstant.AUTHENTICATION_SERVER_URL).
                        setRequestInterceptor(new RequestInterceptorService().requestInterceptor)
                .build();
        _signUpService = restAdapter.create(ISignUpService.class);


        return _signUpService;
    }
}
