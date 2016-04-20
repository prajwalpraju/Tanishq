package com.feet.tanishq.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_Params implements Serializable {
    HashMap<String,String> coll_map,jewel_map,occas_map,mat_map;

    public Model_Params(HashMap<String,String> coll_map,HashMap<String,String> jewel_map,HashMap<String,String> occas_map,HashMap<String,String> mat_map){

        this.coll_map=coll_map;
        this.jewel_map=jewel_map;
        this.occas_map=occas_map;
        this.mat_map=mat_map;

    }

    public HashMap<String, String> getColl_map() {
        return coll_map;
    }

    public void setColl_map(HashMap<String, String> coll_map) {
        this.coll_map = coll_map;
    }

    public HashMap<String, String> getJewel_map() {
        return jewel_map;
    }

    public void setJewel_map(HashMap<String, String> jewel_map) {
        this.jewel_map = jewel_map;
    }

    public HashMap<String, String> getOccas_map() {
        return occas_map;
    }

    public void setOccas_map(HashMap<String, String> occas_map) {
        this.occas_map = occas_map;
    }

    public HashMap<String, String> getMat_map() {
        return mat_map;
    }

    public void setMat_map(HashMap<String, String> mat_map) {
        this.mat_map = mat_map;
    }
}
