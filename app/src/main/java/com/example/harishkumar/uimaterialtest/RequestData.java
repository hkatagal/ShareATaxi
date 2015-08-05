package com.example.harishkumar.uimaterialtest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harishkumar on 4/16/2015.
 */
public class RequestData {


    @SerializedName("id")
    private String mId;

    @SerializedName("date")
    private String mDate;

    @SerializedName("time")
    private String mTime;

    @SerializedName("fromaddress")
    private String mFromAddress;

    @SerializedName("fromcity")
    private String mFromCity;

    @SerializedName("fromstate")
    private String mFromState;

    @SerializedName("fromzip")
    private int mFromZip;

    @SerializedName("destaddress")
    private String mDestAddress;

    @SerializedName("destcity")
    private String mDestCity;

    @SerializedName("deststate")
    private String mDestState;

    @SerializedName("destzip")
    private int mDestZip;


    @SerializedName("userId")
    private String fbId;

    @SerializedName("sharefbid")
    private String sharefbId;

    @SerializedName("sharename")
    private String shareName;

    @SerializedName("shareemail")
    private String shareEmail;

    @SerializedName("sharephoneno")
    private int sharePhoneno;

    @SerializedName("isactive")
    private int isactive;

    @SerializedName("srclong")
    private double srcLong;

    @SerializedName("srclat")
    private double srcLat;

    public RequestData() {

    }


    public RequestData(String id, String date, String time, String fromaddress, String fromcity, String fromstate, int fromzip, String destaddress, String destcity, String deststate, int destzip, String FbId) {
        setId(id);
        setDate(date);
        setTime(time);
        setFromAddress(fromaddress);
        setFromCity(fromcity);
        setFromState(fromstate);
        setFromZip(fromzip);
        setDestAddress(destaddress);
        setDestCity(destcity);
        setDestState(deststate);
        setDestZip(destzip);
        setFbId(FbId);
    }

    public void setFbId(String Id){this.fbId = Id;}
    public void setDate(String date){this.mDate = date;}
    public void setTime(String time){this.mTime = time;}
    public void setId(String id){this.mId = id;}
    public void setFromAddress(String address){this.mFromAddress = address;}
    public void setFromCity(String city){this.mFromCity = city;}
    public void setFromState(String state){this.mFromState = state;}
    public void setFromZip(int zip){this.mFromZip = zip;}
    public void setIsactive(int ind){this.isactive=ind;}
    public void setDestAddress(String address){this.mDestAddress = address;}
    public void setDestCity(String city){this.mDestCity = city;}
    public void setDestState(String state){this.mDestState = state;}
    public void setDestZip(int zip){this.mDestZip = zip;}
    public void setSharefbId(String sharefbId){this.sharefbId = sharefbId;}
    public void setShareName(String shareName){this.shareName=shareName;}
    public void setShareEmail(String shareEmail){this.shareEmail = shareEmail;}
    public void setSharePhoneno(int sharePhoneno){this.sharePhoneno = sharePhoneno;}
    public void setSrcLong(double long1){this.srcLong=long1;}
    public void setSrcLat(double lat){this.srcLat=lat;}


    public String getId(){
        return mId;
    }


    public String getFbId(){return fbId;}
    public  String getDate(){return mDate;}
    public  String getTime(){return mTime;}

    public String getFromAddress(){
        return mFromAddress;
    }

    public String getFromCity(){
        return mFromCity;
    }

    public String getFromState(){
        return mFromState;
    }

    public int getFromZip(){
        return mFromZip;
    }
    public int getIsactive(){return isactive;}


    public String getDestAddress(){
        return mDestAddress;
    }

    public String getDestCity(){
        return mDestCity;
    }

    public String getDestState(){
        return mDestState;
    }

    public int getDestZip(){
        return mDestZip;
    }

    public String getSharefbId(){return sharefbId;}

    public String getShareName(){return shareName;}
    public String getShareEmail(){return shareEmail;}
    public int getSharePhoneno(){return sharePhoneno;}
    public double getSrcLong(){return srcLong;}
    public double getSrcLat(){return srcLat;}




    @Override
    public boolean equals(Object o) {
        return o instanceof RequestData && ((RequestData) o).mId == mId;
    }







}
