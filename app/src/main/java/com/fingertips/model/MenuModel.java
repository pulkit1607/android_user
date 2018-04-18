package com.fingertips.model;

import java.io.Serializable;

/**
 * Created by deepanshurustagi on 4/13/18.
 */

public class MenuModel implements Serializable {


    /**
     * id : 1
     * name : Dal Makhani
     * decscription : Something Great
     * price : 230
     */

    private int id;
    private String name;
    private String decscription;
    private int price;

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

    public String getDecscription() {
        return decscription;
    }

    public void setDecscription(String decscription) {
        this.decscription = decscription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
