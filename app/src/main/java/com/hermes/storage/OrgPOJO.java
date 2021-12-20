package com.hermes.storage;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class OrgPOJO {

    private String displayName;
    private String username;
    private String hashedPassword;

    public boolean checkPassword(String unhashedPassword) {
        try {
            String encoded = encodeSHA256(unhashedPassword);
            return (hashedPassword.equals(encoded));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public static String encodeSHA256(String unhashed) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(unhashed.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
