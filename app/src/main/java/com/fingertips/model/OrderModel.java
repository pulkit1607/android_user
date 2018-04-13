package com.fingertips.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by deepanshurustagi on 4/7/18.
 */

public class OrderModel implements Serializable {


    /**
     * order_id : 1231231231
     * total_amount : 390
     * hotel : {"id":1,"name":"SnapDeal","contact_number":"7011925220","contact_person_name":"Pulkit","address":"Guegaon","city":"Gurgaon","state":"Haryana","lat":28.5017234,"long":77.0852146,"location":"SRID=4326;POINT (77.0852146 28.5017234)","user":1}
     */

    private String order_id;
    private String total_amount;
    private Hotel hotel;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public static class Hotel {
        /**
         * id : 1
         * name : SnapDeal
         * contact_number : 7011925220
         * contact_person_name : Pulkit
         * address : Guegaon
         * city : Gurgaon
         * state : Haryana
         * lat : 28.5017234
         * long : 77.0852146
         * location : SRID=4326;POINT (77.0852146 28.5017234)
         * user : 1
         */

        private int id;
        private String name;
        private String contact_number;
        private String contact_person_name;
        private String address;
        private String city;
        private String state;
        private double lat;
        @SerializedName("long")
        private double longX;
        private String location;
        private int user;

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

        public String getContact_person_name() {
            return contact_person_name;
        }

        public void setContact_person_name(String contact_person_name) {
            this.contact_person_name = contact_person_name;
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }
    }
}
