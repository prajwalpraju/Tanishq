package com.feet.tanishq.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by asif on 06-04-2016.
 */
public class Model_FilterOld implements Serializable{

    String cat_id,item_id,item_name;

    public Model_FilterOld(String cat_id, String item_id, String item_name){
        this.cat_id=cat_id;
        this.item_id=item_id;
        this.item_name=item_name;

    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
