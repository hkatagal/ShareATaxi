package com.example.harishkumar.uimaterialtest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harishkumar on 4/27/2015.
 */
public class fbdetails {
    @SerializedName("id")
    private String mId;
    @SerializedName("userId")
    private String userId;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("email")
    private String email;
    @SerializedName("phonenumber")
    private int phonenumber;

    public fbdetails(){

    }
    public fbdetails(String id, String name){
        setId(id);
        setUserName(name);
    }
    public void setEmail(String email){this.email=email;}
    public void setPhonenumber(int phonenumber){this.phonenumber=phonenumber;}
    public void setUserName(String name){this.UserName=name;}
    public void setUserId(String userId){this.userId=userId;}
    public String getUserName(){return this.UserName;}
    public String getUserId(){return this.userId;}
    public String getEmail(){return this.email;}
    public int getPhonenumber(){return phonenumber;}
    public void setId(String id){this.mId = id;}
    public String getId(){
        return mId;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof fbdetails && ((fbdetails) o).mId == mId;
    }
}
