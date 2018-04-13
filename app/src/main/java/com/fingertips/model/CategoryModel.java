package com.fingertips.model;

import java.io.Serializable;

/**
 * Created by deepanshurustagi on 4/5/18.
 */

public class CategoryModel implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
