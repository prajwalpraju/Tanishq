package com.feet.tanishq.model;

/**
 * Created by asif on 06-04-2016.
 */
public class Model_Category {
    String id,name;
    boolean isSelected;

    public Model_Category(String id,String name,boolean isSelected){
        this.id=id;
        this.name=name;
        this.isSelected=isSelected;

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
