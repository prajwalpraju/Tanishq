package com.feet.tanishq.model;

import java.io.Serializable;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_TopFilter implements Serializable {
        String cat_id,id,name;
    public Model_TopFilter(String cat_id,String id, String name){
        this.cat_id=cat_id;
        this.id=id;
        this.name=name;

    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

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
