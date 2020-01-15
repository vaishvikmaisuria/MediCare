package com.quickhealth.medicare;

import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    static public JSONObject User1;
    static public String acceptedUser = null;
    static public ArrayList<String> items = new ArrayList<>();
    static public String acceptedDocter = null;
    static public boolean onS = false;


    public User(){
        // all users have potential to become a Mangers
    }

    public static JSONObject getUser(){
        return User1;
    }
    public static void  setUser(JSONObject x){
        User1 = x;
    }
    public static void  setacceptedUser(String x ){acceptedUser = x;}
    public static void  setacceptedDoctor(String x ){acceptedDocter = x;}


}
