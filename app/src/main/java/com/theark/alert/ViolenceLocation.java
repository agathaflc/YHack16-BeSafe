package com.theark.alert;

/**
 * Created by jeffrywicaksana on 11/12/16.
 */

public class ViolenceLocation {
    private double lat;
    private double lng;
    private String info;

    public ViolenceLocation(double lat, double lng, String info) {
        this.lat = lat;
        this.lng = lng;
        this.info = info;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getInfo() {
        return info;
    }
}
