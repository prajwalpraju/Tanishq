package com.feet.tanishq.model;

import java.io.Serializable;

/**
 * Created by asif on 15-04-2016.
 */
public class Model_Product implements Serializable{
    String product_image,product_title,product_price,discount_price,discount_percent,product_url;

    public Model_Product(String product_image,String product_title,String product_price,String discount_price,String discount_percent,String product_url){

        this.product_image=product_image;
        this.product_title=product_title;
        this.product_price=product_price;
        this.discount_price=discount_price;
        this.discount_percent=discount_percent;
        this.product_url=product_url;

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
