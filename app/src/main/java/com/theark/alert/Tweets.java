package com.theark.alert;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jeffrywicaksana on 11/12/16.
 */

public class Tweets {
    private float lat;
    private float lng;
    @SerializedName("TweetBody")
    private String info;

    public Tweets(float lat, float lng, String info) {
        this.lat = lat;
        this.lng = lng;
        this.info = info;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getInfo() {
        return info;
    }
}
