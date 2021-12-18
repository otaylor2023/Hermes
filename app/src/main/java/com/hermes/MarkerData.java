package com.hermes;

import android.content.res.Resources;
import android.graphics.Color;

import java.lang.reflect.Field;
import java.util.Date;

public class MarkerData {

    private Date date;
    private String description;
    private int idField;
    private String crimeString;
    private int crimeColor;

    public MarkerData(int idField, Date date, String description) {
        this.idField = idField;
        this.date = date;
        this.description = description;
        this.crimeString = Resources.getSystem().getString(idField);
        this.crimeColor = Resources.getSystem().getColor(idField);
    }
//    private Organization organization;

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

}
