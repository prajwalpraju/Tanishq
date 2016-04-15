package com.feet.tanishq.model;

import java.io.Serializable;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_Params implements Serializable {
    String collection_id,jewellery_id,occassion_id,material_id,collection_name,jewellery_name,occassion_name,material_name;
    public Model_Params(String collection_id,String jewellery_id,String occassion_id,String material_id,
                        String collection_name,String jewellery_name,String occassion_name,String material_name){

        this.collection_id=collection_id;
        this.jewellery_id=jewellery_id;
        this.occassion_id=occassion_id;
        this.material_id=material_id;
        this.collection_name=collection_name;
        this.jewellery_name=jewellery_name;
        this.occassion_name=occassion_name;
        this.material_name=material_name;

    }

    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getOccassion_name() {
        return occassion_name;
    }

    public void setOccassion_name(String occassion_name) {
        this.occassion_name = occassion_name;
    }

    public String getJewellery_name() {
        return jewellery_name;
    }

    public void setJewellery_name(String jewellery_name) {
        this.jewellery_name = jewellery_name;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getJewellery_id() {
        return jewellery_id;
    }

    public void setJewellery_id(String jewellery_id) {
        this.jewellery_id = jewellery_id;
    }

    public String getOccassion_id() {
        return occassion_id;
    }

    public void setOccassion_id(String occassion_id) {
        this.occassion_id = occassion_id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }
}
