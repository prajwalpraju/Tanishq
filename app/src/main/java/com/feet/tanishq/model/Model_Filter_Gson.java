package com.feet.tanishq.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.s;

/**
 * Created by Administrator on 4/18/2017.
 */

public class Model_Filter_Gson implements Serializable {


    @SerializedName("Category")
    List<List_Arr> category_arr;

    @SerializedName("Collection")
    List<List_Arr> collection_arr;

    @SerializedName("Grammage")
    List<List_Arr> grammage_arr;

    @SerializedName("Karatage")
    List<List_Arr> karatage_arr;

    @SerializedName("Material")
    List<List_Arr> material_arr;

    @SerializedName("Occasion")
    List<List_Arr> occasion_arr;

    @SerializedName("Pricebar")
    List<List_Arr> pricebar_arr;


    public List<List_Arr> getCategory_arr() {
        return category_arr;
    }

    public void setCategory_arr(List<List_Arr> category_arr) {
        this.category_arr = category_arr;
    }

    public List<List_Arr> getCollection_arr() {
        return collection_arr;
    }

    public void setCollection_arr(List<List_Arr> collection_arr) {
        this.collection_arr = collection_arr;
    }

    public List<List_Arr> getGrammage_arr() {
        return grammage_arr;
    }

    public void setGrammage_arr(List<List_Arr> grammage_arr) {
        this.grammage_arr = grammage_arr;
    }

    public List<List_Arr> getKaratage_arr() {
        return karatage_arr;
    }

    public void setKaratage_arr(List<List_Arr> karatage_arr) {
        this.karatage_arr = karatage_arr;
    }

    public List<List_Arr> getMaterial_arr() {
        return material_arr;
    }

    public void setMaterial_arr(List<List_Arr> material_arr) {
        this.material_arr = material_arr;
    }

    public List<List_Arr> getOccasion_arr() {
        return occasion_arr;
    }

    public void setOccasion_arr(List<List_Arr> occasion_arr) {
        this.occasion_arr = occasion_arr;
    }

    public List<List_Arr> getPricebar_arr() {
        return pricebar_arr;
    }

    public void setPricebar_arr(List<List_Arr> pricebar_arr) {
        this.pricebar_arr = pricebar_arr;
    }

    public class List_Arr{


        @SerializedName("id")
        String id;

        @SerializedName("name")
        String name;

        @SerializedName("isSelected")
        boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
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

}
