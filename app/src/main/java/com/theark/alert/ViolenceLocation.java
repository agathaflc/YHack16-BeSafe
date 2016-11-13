package com.theark.alert;

/**
 * Created by jeffrywicaksana on 11/12/16.
 */

public class ViolenceLocation {
    private float lat;
    private float lng;
    private String info;

    public ViolenceLocation(float lat, float lng, String info) {
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
