package com.feet.tanishq.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_Product implements Serializable{
    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    String device_image,product_image,product_title,product_price,discount_price,discount_percent,product_url,description,community,occasion,collection,material,category,onlineexclusive,gold_karatage, weight,catalogue_display_image,disclaimer;
    boolean inWish,inCompare;
    ArrayList<String> stringArrayUrlList;

    public ArrayList<String> getStringArrayUrlList() {
        return stringArrayUrlList;
    }

    public void setStringArrayUrlList(ArrayList<String> stringArrayUrlList) {
        this.stringArrayUrlList = stringArrayUrlList;
    }

    public String getOnlineexclusive() {
        return onlineexclusive;
    }

    public void setOnlineexclusive(String onlineexclusive) {
        this.onlineexclusive = onlineexclusive;
    }

    public String getCatalogue_display_image() {
        return catalogue_display_image;
    }

    public void setCatalogue_display_image(String catalogue_display_image) {
        this.catalogue_display_image = catalogue_display_image;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Model_Product(String device_image, String product_image, String product_title, String product_price, String discount_price, String discount_percent,
                         String description, String collection, String material, String category, String product_url, boolean inWish, boolean inCompare, String gold_karatage, String weight, String onlineexclusive, String catalogue_display_image, String community, String occasion, ArrayList<String> stringArrayUrlList, String disclaimer){

        this.device_image=device_image;
        this.product_image=product_image;
        this.product_title=product_title;
        this.product_price=product_price;
        this.discount_price=discount_price;
        this.discount_percent=discount_percent;

        this.description=description;
        this.collection=collection;
        this.material=material;
        this.category=category;
        this.product_url=product_url;

        this.gold_karatage=gold_karatage;
        this.weight=weight;
        this.occasion=occasion;
        this.community=community;

        this.inWish=inWish;
        this.inCompare=inCompare;
        this.onlineexclusive=onlineexclusive;
        this.catalogue_display_image=catalogue_display_image;
        this.stringArrayUrlList = stringArrayUrlList;
        this.disclaimer= disclaimer;

    }


    public String getGold_karatage() {
        return gold_karatage;
    }

    public void setGold_karatage(String gold_karatage) {
        this.gold_karatage = gold_karatage;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDevice_image() {
        return device_image;
    }

    public void setDevice_image(String device_image) {
        this.device_image = device_image;
    }

    public boolean isInWish() {
        return inWish;
    }

    public void setInWish(boolean inWish) {
        this.inWish = inWish;
    }

    public boolean isInCompare() {
        return inCompare;
    }

    public void setInCompare(boolean inCompare) {
        this.inCompare = inCompare;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }
}
