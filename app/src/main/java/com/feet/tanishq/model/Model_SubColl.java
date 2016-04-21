package com.feet.tanishq.model;

/**
 * Created by asif on 14-04-2016.
 */
public class Model_SubColl {
    String cat_id,cat_name,item_id,item_name,item_image;

    public Model_SubColl(String cat_id,String cat_name,String item_id,String item_name,String item_image){
        this.cat_id=cat_id;
        this.cat_name=cat_name;
        this.item_id=item_id;
        this.item_name=item_name;
        this.item_image=item_image;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
