package com.hermes;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;

public class LocalStorage {


    public static OrgPOJO getCurrentUser(View view) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LocalLocations.ORGANIZATION.path, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(LocalLocations.ORGANIZATION.key, null);
        if (jsonString != null) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(jsonString, OrgPOJO.class);
            } catch (JsonSyntaxException e) {
                return null;
            }
        }
        return null;
    }

    public static void storeCurrentUser(View view, OrgPOJO orgPOJO) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(orgPOJO);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LocalLocations.ORGANIZATION.path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LocalLocations.ORGANIZATION.key, jsonString);
        editor.apply();
    }

    public static void signUserOut(View view) {
        storeCurrentUser(view, null);
    }


    public enum LocalLocations {
        ORGANIZATION("user_file", "current_user");

        public final String path;
        public final String key;

        LocalLocations(String path, String key) {
            this.path = path;
            this.key = key;
        }
    }
}
