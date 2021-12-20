package com.hermes.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static void saveContact(View view, ContactPOJO contactPOJO) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(contactPOJO);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LocalLocations.CONTACTS.path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(contactPOJO.getName(), jsonString);
        editor.apply();
    }

    public static Map<String, ContactPOJO> getContactMap(View view) {
        Map<String, ContactPOJO> contactMao = new HashMap<>();
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LocalLocations.CONTACTS.path, Context.MODE_PRIVATE);
        Map<String, String> jsonList = (Map<String, String>) sharedPreferences.getAll();

        for (String key : jsonList.keySet()) {
            Gson gson = new Gson();
            ContactPOJO contactPOJO = gson.fromJson(jsonList.get(key), ContactPOJO.class);
            contactMao.put(key, contactPOJO);
        }
        return contactMao;
    }

    public static List<ContactPOJO> getContactList(View view) {
        Map<String, ContactPOJO> contactMap = getContactMap(view);
        return (List<ContactPOJO>) contactMap.values();
    }

    public static ContactPOJO findContact(View view, String name) {
        Map<String, ContactPOJO> contactMap = getContactMap(view);
        return contactMap.get(name);
    }

    public static void deleteContact(View view, String name) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LocalLocations.CONTACTS.path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
    }


    public enum LocalLocations {
        ORGANIZATION("user_file", "current_user"),
        CONTACTS("user_contacts", null);

        public final String path;
        public final String key;

        LocalLocations(String path, String key) {
            this.path = path;
            this.key = key;
        }
    }
}
