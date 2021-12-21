package com.hermes;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hermes.storage.ContactPOJO;
import com.hermes.storage.ServerStorage;
import com.hermes.storage.LocalStorage;
import com.hermes.storage.OrgPOJO;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HermesUtils {

    private static final String TAG = "mercury.hermesutils";

    public static View.OnFocusChangeListener getTextFocusListener(int editTextId) {

        View.OnFocusChangeListener ofcListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.getId() == editTextId && !b) {

                    InputMethodManager imm =  (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        };
        return ofcListener;
    }

    public static void createFakeOrg() {

        OrgPOJO orgPOJO = new OrgPOJO();
        orgPOJO.setDisplayName("Neighborhood Watch");
        try {
            orgPOJO.setHashedPassword(OrgPOJO.encodeSHA256("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        orgPOJO.setUsername("neighborhood_watch");
        ServerStorage.addOrganization(orgPOJO);
    }


    public static void createFakeContacts(View view) {
        ContactPOJO contact1 = new ContactPOJO("person1", "9991113333", "help pls");
        ContactPOJO contact2 = new ContactPOJO("person2", "1234448888", "not having a good time");
        ContactPOJO contact3 = new ContactPOJO("person3", "4418879292", "probably dying rn help");
        LocalStorage.saveContact(view, contact1);
        LocalStorage.saveContact(view, contact2);
        LocalStorage.saveContact(view, contact3);
    }

    public static void viewContacts(View view) {
        Map<String, ContactPOJO> contactMap = LocalStorage.getContactMap(view);
        Log.d(TAG, contactMap.toString());
    }
}
