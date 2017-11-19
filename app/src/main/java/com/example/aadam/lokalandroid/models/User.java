package com.example.aadam.lokalandroid.models;

/**
 * Created by Aadam on 2017-11-18.
 */

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Ryan_Schmidt and Aadam on 2017-11-18.
 */

import static java.lang.Math.cos;

public class User {

    private String email;
    private String password;
    private DetailedEvent[] eventsHosted;
    private DetailedEvent[] eventsAvailable;
    private FirebaseDatabase query;
    private GoogleMap map;


    public User(){

        email = "";
        password = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocal(double ln, double lt){


    }


    public Query calculateList(double lng, double lat){

        double latRange = lat += 0.027131152;
        double lngRange = lng += 3/(cos(lat)*111.320);


       return FirebaseDatabase.getInstance().getReference().child("detailed.events")
                .orderByChild("lat").startAt(map.getMyLocation().getLatitude() - 0.027131152).endAt(map.getMyLocation().getLatitude() + 0.027131152)
                .orderByChild("long").startAt(map.getMyLocation().getLongitude() - 3/(cos(lat)*111.320)).endAt(map.getMyLocation().getLongitude() + 3/(cos(lat)*111.320));







    }

}
