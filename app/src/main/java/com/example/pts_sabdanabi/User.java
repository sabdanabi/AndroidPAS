package com.example.pts_sabdanabi;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    public String getFullName(){
        return fullName;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }
}