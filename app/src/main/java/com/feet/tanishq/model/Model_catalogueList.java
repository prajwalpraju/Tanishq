package com.feet.tanishq.model;

/**
 * Created by asif on 11-04-2016.
 */
public class Model_catalogueList {

    String id;
    String name;
    String image;
    String contenttype;

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getHasfilter() {
        return hasfilter;
    }

    public void setHasfilter(String hasfilter) {
        this.hasfilter = hasfilter;
    }

    String hasfilter;

    public Model_catalogueList(String id, String name, String image, String contenttype, String hasfilter) {

        this.id = id;
        this.name = name;
        this.image = image;
        this.contenttype = contenttype;
        this.hasfilter = hasfilter;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

