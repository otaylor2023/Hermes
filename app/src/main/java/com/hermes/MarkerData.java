package com.hermes;

import android.util.Log;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class MarkerData {

    private Calendar date;
    private String description;
//    private long idField;
    private CrimeType crimeType;
    private LatLng location;
    private static final String TAG = "mercury.markerdata";
    private static HashMap<String, CrimeType> crimeTypeHashMap;


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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
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



    public enum CrimeType {

        ASSAULT(R.string.assault, R.color.assault),
        ROBBERY(R.string.robbery, R.color.robbery),
        AUTO_THEFT(R.string.auto_theft, R.color.auto_theft),
        MUGGING(R.string.mugging, R.color.mugging),
        MURDER(R.string.murder, R.color.murder),
        SEXUAL_ASSAULT(R.string.sexual_assault, R.color.sexual_assault);

        private int hueRes;
        private int nameRes;


        CrimeType(int nameRes, int hueRes) {
            this.nameRes = nameRes;
            this.hueRes = hueRes;
        }

        public int getHueRes() {
            return hueRes;
        }

        public int getNameRes() {
            return nameRes;
        }



    }
}
