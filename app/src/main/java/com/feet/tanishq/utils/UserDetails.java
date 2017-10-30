package com.feet.tanishq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.feet.tanishq.model.Model_Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by asif on 04-04-2016.
 */
public class UserDetails {

    Context context;
    SharedPreferences userPref;
    SharedPreferences.Editor editor;

    public UserDetails(Context context){
        this.context=context;
        userPref=this.context.getSharedPreferences("user",Context.MODE_PRIVATE);
        this.editor=userPref.edit();
    }

    public void clearUserPreference(){
        userPref.edit().clear().commit();
    }

    public void setUserTitle(String userTitle){
        editor.putString("title",userTitle);
        editor.commit();
    }


    public void setAppVertion(int appvertion){
        editor.putInt("appvertion",appvertion);
        editor.commit();
    }

    public int getAppVertion(){
        return userPref.getInt("appvertion",7);
    }

    public String getUserTitle(){
        return userPref.getString("title","");
    }

    public void setUserName(String username){
        editor.putString("username",username);
        editor.commit();
    };
    public String getUserName(){
        return userPref.getString("username","");
    };
    public void setMobileNumber(String mobile){
        editor.putString("mobile",mobile);
        editor.commit();
    };
    public String getMobileNumber(){
        return  userPref.getString("mobile","");
    };

    public void setUserId(String id){
        editor.putString("id",id);
        editor.commit();
    };

    public String getMainDashboadResponse(){
        return  userPref.getString("response","");
    };

    public void setMainDashboadResponse(String MainDashboadResponse){
        editor.putString("response",MainDashboadResponse);
        editor.commit();
    };

    public String getUserId(){
        return  userPref.getString("id","");
    };

    public void setUserDevice(String device){
        editor.putString("device",device);
        editor.commit();
    };

    public String getUserDevice(){
        return  userPref.getString("device","");
    };

    public void setDemoUrl(String url){
        editor.putString("url",url);
        editor.commit();
    };

    public String getDemoUrl(){
        return  userPref.getString("url","");
    };

    public void setVideo_idUrl(String video_id){
        editor.putString("video_id",video_id);
        editor.commit();
    };

    public boolean getPush(){
        return  userPref.getBoolean("push",false);
    };


    public void setPush(boolean push){
        editor.putBoolean("push",push);
        editor.commit();
    };

    public String getVideo_idUrl(){
        return  userPref.getString("video_id","");
    };



    public void savePagePosition( int nextpage,int totalpages) {
        editor.putInt("next_page",nextpage);
        editor.putInt("total_page",totalpages);
        editor.commit();
    }


    public ArrayList<Model_Product> getPreservedArray(){

        Gson gson = new Gson();
        String json = userPref.getString("preserve_Array","");
        Type type = new TypeToken<ArrayList<Model_Product>>() {}.getType();
        ArrayList<Model_Product> arrayList = gson.fromJson(json, type);
        return arrayList;
    }


    public void ClearPreservedArrayAndPosition(){

        userPref.edit().remove("preserve_Array").apply();
        userPref.edit().remove("next_page").apply();
        userPref.edit().remove("total_page").apply();

    }

    public int getNextPosition(){
        return userPref.getInt("next_page",1);
    }
    public int getTotalPosition(){
        return userPref.getInt("total_page",2);
    }

    public void setAndPreservePosition(String arr_list) {
        editor.putString("preserve_Array",arr_list);
        editor.commit();
    }

}
