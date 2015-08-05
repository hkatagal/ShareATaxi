package com.example.harishkumar.uimaterialtest;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

/**
 * Created by Harishkumar on 4/18/2015.
 */
public interface DBDetails {
     MobileServiceClient mClient = null;
    MobileServiceClient fbClient = null;
    String UserId = null;
    String urName = null;
    String eMail = null;
    int phoneNo =0;
    // MobileServiceTable<RequestData> mTableDb = null;
     public void setDBDetails();
     public MobileServiceClient getDBDetails();
    public MobileServiceClient getFbClient();
    public String getUrName();
    public void seteMail(String eMail);
    public void setPhoneNo(int phoneNo);
    public String geteMail();
    public int getPhoneNo();
    public String getUserId();
    }




