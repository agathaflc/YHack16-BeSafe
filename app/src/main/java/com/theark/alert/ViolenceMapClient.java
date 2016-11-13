package com.theark.alert;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jeffrywicaksana on 11/12/16.
 */

public interface ViolenceMapClient {
    @GET("/locations")
    Call<List<ViolenceLocation>> getLocations();

    @POST("/locations")
    Call<List<ViolenceLocation>> reportLocation(@Body List<ViolenceLocation> location);

}
