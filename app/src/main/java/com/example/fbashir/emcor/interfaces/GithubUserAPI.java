package com.example.fbashir.emcor.interfaces;

import com.example.fbashir.emcor.models.GithubUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fbashir on 8/2/2016.
 */

public interface GithubUserAPI {
    String ENDPOINT = "https://api.github.com";

    @GET("/users/{user}")
    Call<GithubUser> getUser(@Path("user") String user);
}
