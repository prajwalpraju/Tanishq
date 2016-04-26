package com.feet.tanishq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asif on 07-04-2016.
 */
public class DataBaseHandler {

//    private static final int DATABASE_VERSION=1;
    public static String DATABASE_NAME="Tanishq.db";
    public static String TABLE_CATEGORY="category";
    public static String TABLE_WISHLIST="wishlist";


    public static String CREATE_CATEGORY_TABLE="create table if not exists "+TABLE_CATEGORY+" (id integer primary key," +
            "cat_id varchar," +
            "cat_name varchar," +
            "item_id varchar," +
            "item_name varchar," +
            "selected varchar)";

    public static  String CREATE_WISHLIST_TABLE="create table if not exists "+TABLE_WISHLIST+" (id integer primary key," +
            "product_image varchar," +
            "product_title varchar," +
            "product_price varchar," +
            "discount_price varchar," +
            "discount_percent varchar," +
            "product_url varchar)";

}
