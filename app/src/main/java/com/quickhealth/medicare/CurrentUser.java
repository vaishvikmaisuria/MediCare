package com.quickhealth.medicare;

import org.json.JSONObject;

public class CurrentUser {

    static public String user_name = null;
    static public String user_id = null;
    static public String user_token = "";
    static public String user_ProfilePic = null;
    static public String lattitude = "";
    static public String longitude = "";

    public static void  setUserName(String name){
        user_name = name;
    }
    public static void  setUserID(String id){
        user_id = id;
    }
    public static void  setUserToken(String token){
        user_token = token;
    }
    public static void  setUserProfilePic(String profilePic){
        user_ProfilePic = profilePic;
    }
    public static void  setUserLattitude(String lattitude){
        lattitude = lattitude;
    }
    public static void  setUserLongitude(String longitude){
        longitude = longitude;
    }

    public static String  getUserName(){
        return user_name;
    }
    public static String  getUserID(){
        return user_id;
    }
    public static String  getUserToken(){
        return user_token;
    }
    public static String  getUserProfilePic(){ return user_ProfilePic; }
    public static String  getUserLattitude(){ return lattitude; }
    public static String  getUserLongitude(){ return longitude; }
}

