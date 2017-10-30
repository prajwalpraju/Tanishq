package com.feet.tanishq.utils;

import com.feet.tanishq.model.Model_FilterOld;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class DataWrapper implements Serializable {

    private ArrayList<Model_FilterOld> model;

    public DataWrapper(ArrayList<Model_FilterOld> data) {
        this.model = data;
    }

    public ArrayList<Model_FilterOld> getParliaments() {
        return this.model;
    }

}
