package com.hermes;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hermes.storage.HermesStorage;
import com.hermes.storage.OrgPOJO;

import java.security.NoSuchAlgorithmException;

public class HermesUtils {

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
        HermesStorage hermesStorage = new HermesStorage();
        hermesStorage.addOrganization(orgPOJO);
    }
}
