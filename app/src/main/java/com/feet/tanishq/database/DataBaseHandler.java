package com.feet.tanishq.database;

/**
 * Created by asif on 07-04-2016.
 */
public class DataBaseHandler {

    public static String DATABASE_NAME="Tanishq.db";
    public static String TABLE_CATEGORY="category";

    public static String CREATE_CATEGORY_TABLE="create table if not exists "+TABLE_CATEGORY+" (id integer primary key," +
            "cat_id varchar," +
            "cat_name varchar," +
            "item_id varchar," +
            "item_name varchar)";

}
