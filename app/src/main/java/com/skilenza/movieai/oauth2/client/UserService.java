package com.skilenza.movieai.oauth2.client;

import com.skilenza.movieai.oauth2.constant.OauthConstant;
import com.skilenza.movieai.oauth2.service.IUserService;

import retrofit.RestAdapter;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class    UserService {

    private IUserService _userService;


    public IUserService getUser() {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(OauthConstant.AUTHENTICATION_SERVER_URL).
                        setRequestInterceptor(new RequestInterceptorService().requestInterceptor)
                .build();
        _userService = restAdapter.create(IUserService.class);


        return _userService;
    }
}
