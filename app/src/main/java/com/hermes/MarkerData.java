package com.hermes;

import android.util.Log;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class MarkerData {

    private Date date;
    private String description;
//    private long idField;
    private CrimeType crimeType;
    private LatLng location;
    private static final String TAG = "mercury.markerdata";
    private static HashMap<String, CrimeType> crimeTypeHashMap;

    public MarkerData() {}

    public MarkerData(MarkerPOJO markerPOJO) {
        this.date = markerPOJO.getDate();
        this.description = markerPOJO.getDescription();
        this.crimeType = getCrimeType(markerPOJO.getStringRes());
        this.location = new LatLng(markerPOJO.getLatitude(), markerPOJO.getLongitude());
    }


//    private Organization organization;

//    public static int getId(String resourceName, Class<?> c) {
//        try {
//            Field idField = c.getDeclaredField(resourceName);
//            return idField.getInt(idField);
//        } catch (Exception e) {
//            throw new RuntimeException("No resource ID found for: "
//                    + resourceName + " / " + c, e);
//        }
//    }
//
//    public void setResources(String selectedString) {
//
//        String resourceName = selectedString.toLowerCase(Locale.ROOT);
//        resourceName = String.join("_", Arrays.asList(resourceName.split(" ")));
//        Log.d(TAG, resourceName);
//
//        this.crimeString = Resources.getSystem().getString(getId(resourceName, String.class));
//        this.crimeColor = Resources.getSystem().getColor(getId(resourceName, Integer.class));
//    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public float getCrimeColor(View view) {
        float[] hslValues = new float[3];
        int colorLong = view.getResources().getColor(crimeType.getHueRes());
        ColorUtils.colorToHSL(colorLong, hslValues);
        Log.d(TAG, "hsl values: " + Arrays.toString(hslValues));
        return hslValues[0];
    }

    public String getCrimeString(View view) {
        return view.getResources().getString(crimeType.getNameRes());
    }

    public void setCrimeType(CrimeType crimeType) {
        this.crimeType = crimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CrimeType getCrimeType() {
        return crimeType;
    }

    public static CrimeType getCrimeType(String crimeName, View view) {

        if (crimeTypeHashMap == null) {
            crimeTypeHashMap = new HashMap<>();
            for (CrimeType crimeType : CrimeType.values()) {
                crimeTypeHashMap.put(view.getResources().getString(crimeType.getNameRes()), crimeType);
            }
        }

        return crimeTypeHashMap.get(crimeName);

    }

    public static CrimeType getCrimeType(int crimeNameRes) {
        for (CrimeType crimeType : CrimeType.values()) {
            if (crimeType.getNameRes() == crimeNameRes) {
                return crimeType;
            }
        }
        return null;
    }


}
