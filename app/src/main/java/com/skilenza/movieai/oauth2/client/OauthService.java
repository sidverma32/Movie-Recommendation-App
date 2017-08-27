package com.skilenza.movieai.oauth2.client;

import com.skilenza.movieai.oauth2.constant.OauthConstant;
import com.skilenza.movieai.oauth2.service.IOauthService;

import retrofit.RestAdapter;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class OauthService {

    private IOauthService _oauthService;


    public IOauthService getAccessToken() {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(OauthConstant.AUTHENTICATION_SERVER_URL)
                .build();
        _oauthService = restAdapter.create(IOauthService.class);


        return _oauthService;
    }
}