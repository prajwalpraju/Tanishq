package com.feet.tanishq.model;

import java.io.Serializable;

/**
 * Created by Administrator on 4/27/2017.
 */

public class ModelTopFilterNew implements Serializable {

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ModelTopFilterNew(String title) {

        this.title = title;
    }
}
