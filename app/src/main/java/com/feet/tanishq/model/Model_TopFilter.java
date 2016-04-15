package com.feet.tanishq.model;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_TopFilter {
        String id,name;
    public Model_TopFilter(String id, String name){
        this.id=id;
        this.name=name;

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
