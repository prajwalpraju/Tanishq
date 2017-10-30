package com.feet.tanishq.model;

/**
 * Created by asif on 11-04-2016.
 */
public class CollectionList_Model {

    String id;
    String name;
    String image;
    String contenttype;
    String filterparameter;
    String onlineexclusive;

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

    public String getOnlineexclusive() {
        return onlineexclusive;
    }

    public void setOnlineexclusive(String onlineexclusive) {
        this.onlineexclusive = onlineexclusive;
    }

    public String getFilterparameter() {
        return filterparameter;
    }

    public void setFilterparameter(String filterparameter) {
        this.filterparameter = filterparameter;
    }

    public CollectionList_Model(String id, String name, String image, String contenttype, String hasfilter, String filterparameter, String onlineexclusive) {

        this.id = id;
        this.name = name;
        this.image = image;
        this.contenttype = contenttype;
        this.hasfilter = hasfilter;
        this.filterparameter = filterparameter;
        this.onlineexclusive = onlineexclusive;

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

