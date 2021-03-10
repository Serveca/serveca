package com.example.seerveca;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "CCAC1406-F21E-6AE5-FF31-EBD5D60D2600";
    public static final String API_KEY = "BF35EF02-7F95-4A49-B2DB-8C43E760E3FA";
    public static final String SERVER_URL = "https://api.backendless.com";
    public static BackendlessUser user;
    public  static List<provider>provider ;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);
    }
}
