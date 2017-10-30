package com.feet.tanishq.model;

//import android.icu.text.SymbolTable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asif on 22-04-2017.
 */

public class Model_Filter implements Serializable {

    @SerializedName("response")
    List<Response> responseList;

    //object

//    @SerializedName("response")
//    Response response;


    public List<Response> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<Response> responseList) {
        this.responseList = responseList;
    }

    public class Response{

        @SerializedName("title")
        String title;

        @SerializedName("selectiontype")
        String selectiontype;

        public String getSelectiontype() {
            return selectiontype;
        }

        public void setSelectiontype(String selectiontype) {
            this.selectiontype = selectiontype;
        }

        @SerializedName("filterinfo")
        List<FilterInfo> filterInfoList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<FilterInfo> getFilterInfoList() {
            return filterInfoList;
        }

        public void setFilterInfoList(List<FilterInfo> filterInfoList) {
            this.filterInfoList = filterInfoList;
        }
    }

    public static class FilterInfo implements Serializable{

        @SerializedName("filterid")
        String filterid;

        @SerializedName("filtername")
        String filtername;

        @SerializedName("activestatus")
        String activestatus;

        @SerializedName("checked")
        String checked;

        @SerializedName("mainfilter")
        String mainfilter;

        @SerializedName("prodcutsCount")
        String prodcutsCount;

        public FilterInfo(String filterid, String filtername, String activestatus, String checked, String mainfilter, String prodcutsCount, boolean select) {
            this.filterid = filterid;
            this.filtername = filtername;
            this.activestatus = activestatus;
            this.checked = checked;
            this.mainfilter = mainfilter;
            this.prodcutsCount = prodcutsCount;
            this.select = select;
        }

        public String getProdcutsCount() {
            return prodcutsCount;
        }

        public void setProdcutsCount(String prodcutsCount) {
            this.prodcutsCount = prodcutsCount;
        }

        public String getActivestatus() {
            return activestatus;
        }

        public void setActivestatus(String activestatus) {
            this.activestatus = activestatus;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
        }

        public String getMainfilter() {
            return mainfilter;
        }

        public void setMainfilter(String mainfilter) {
            this.mainfilter = mainfilter;
        }

        boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getFilterid() {
            return filterid;
        }

        public void setFilterid(String filterid) {
            this.filterid = filterid;
        }

        public String getFiltername() {
            return filtername;
        }

        public void setFiltername(String filtername) {
            this.filtername = filtername;
        }
    }

}
