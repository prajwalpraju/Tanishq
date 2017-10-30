package com.feet.tanishq.utils;

/**
 * Created by Administrator on 4/19/2017.
 */

public class Filterlist_array {
    private String selectiontype;

    public String getSelectiontype() {
        return selectiontype;
    }

    public void setSelectiontype(String selectiontype) {
        this.selectiontype = selectiontype;
    }

    private String value;
    private boolean selected;

    public Filterlist_array(String value, boolean selected, String selectiontype) {
        this.value = value;
        this.selected = selected;
        this.selectiontype = selectiontype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
