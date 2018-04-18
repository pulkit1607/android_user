package com.fingertips.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by deepanshurustagi on 4/13/18.
 */

public class HotelModel implements Serializable {


    /**
     * id : 1
     * name : SnapDeal
     * contact_number : 7011925220
     * address : Guegaon
     * city : Gurgaon
     * state : Haryana
     * lat : 28.5017234
     * long : 77.0852146
     */

    private int id;
    private String name;
    private String contact_number;
    private String address;
    private String city;
    private String state;
    private double lat;
    @SerializedName("long")
    private double longX;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongX() {
        return longX;
    }

    public void setLongX(double longX) {
        this.longX = longX;
    }
}
