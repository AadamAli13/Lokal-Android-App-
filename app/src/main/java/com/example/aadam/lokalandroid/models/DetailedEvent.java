package com.example.aadam.lokalandroid.models;

import android.location.Location;

import com.example.aadam.lokalandroid.models.User;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ryan_Schmidt on 2017-11-18.
 */

public class DetailedEvent {

    private Location location;
    private String eventType;
    private User eventHost;
    private Calendar when;
    private double cost;;
    private String additionalNotes;

    public DetailedEvent(Location location, String eventType, User eventHost, int year, int month, int dayOfMonth,
                         int hourOfDay, int minute, double cost, String additionalNotes){

        this.location = location;
        this.eventType = eventType;
        this.eventHost = eventHost;
        when = new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minute);
        this.cost = cost;
        this.additionalNotes = additionalNotes;
    }

    public double distanceFromLocation(Location otherLocation){

        double lat1 = location.getLatitude();
        double lat2 = otherLocation.getLatitude();
        double lng1 = location.getLongitude();
        double lng2 = otherLocation.getLongitude();
        double resultLat = Math.abs(lat1-lat2);
        double resultLng = Math.abs(lng1-lng2);

        double x = resultLat*(110.574);
        double y = resultLng * (Math.cos(Math.toRadians(resultLat))*111.320);

        return Math.sqrt((Math.pow(x,2)+Math.pow(y,2)));
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public User getEventHost() {
        return eventHost;
    }

    public void setEventHost(User eventHost) {
        this.eventHost = eventHost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Calendar getWhen() {
        return when;
    }

    public void setWhen(Calendar when) {
        this.when = when;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
}
