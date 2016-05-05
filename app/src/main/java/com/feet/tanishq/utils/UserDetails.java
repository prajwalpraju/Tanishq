package com.feet.tanishq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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



}
