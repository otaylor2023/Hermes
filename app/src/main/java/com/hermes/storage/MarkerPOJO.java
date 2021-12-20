package com.hermes.storage;

import com.google.android.gms.maps.model.LatLng;
import com.hermes.MarkerData;

import java.util.Date;

public class MarkerPOJO {
    private Date date;
    private double latitude;
    private double longitude;
    private String description;
    private int stringRes;
    private int colorRes;

    public MarkerPOJO() {}

    public MarkerPOJO(MarkerData markerData) {
        this.date = markerData.getDate();
        this.latitude = markerData.getLocation().latitude;
        this.longitude = markerData.getLocation().longitude;
        this.description = markerData.getDescription();
        this.stringRes = markerData.getCrimeType().getNameRes();
        this.colorRes = markerData.getCrimeType().getHueRes();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStringRes() {
        return stringRes;
    }

    public void setStringRes(int stringRes) {
        this.stringRes = stringRes;
    }

    public int getColorRes() {
        return colorRes;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
