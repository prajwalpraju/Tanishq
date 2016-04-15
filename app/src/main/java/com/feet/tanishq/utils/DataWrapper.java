package com.feet.tanishq.utils;

import com.feet.tanishq.model.Model_Filter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class DataWrapper implements Serializable {

    private ArrayList<Model_Filter> model;

    public DataWrapper(ArrayList<Model_Filter> data) {
        this.model = data;
    }

    public ArrayList<Model_Filter> getParliaments() {
        return this.model;
    }

}
