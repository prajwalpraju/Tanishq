package com.feet.tanishq.model;

import java.io.Serializable;

/**
 * Created by asif on 06-04-2016.
 */
public class Model_Category implements Serializable{
    String cat_id,cat_name,id,name;
    boolean isSelected;

    public Model_Category(String cat_id,String cat_name,String id,String name,boolean isSelected){

        this.cat_id=cat_id;
        this.cat_name=cat_name;
        this.id=id;
        this.name=name;
        this.isSelected=isSelected;

    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
