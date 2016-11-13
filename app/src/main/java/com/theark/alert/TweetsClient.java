package com.theark.alert;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jeffrywicaksana on 11/12/16.
 */

public interface TweetsClient {
    @GET("/tweets")
    Call<List<Tweets>> getLocationTweet();
}
