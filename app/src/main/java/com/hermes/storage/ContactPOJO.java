package com.hermes.storage;

import androidx.annotation.NonNull;

public class ContactPOJO {

    public ContactPOJO() {}

    public ContactPOJO(String name, String number, String message) {
        this.name = name;
        this.number = number;
        this.message = message;
    }

    private String name;
    private String number;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    public String toString() {
        return String.format("[%s, %s, %s]", name, number, message);
    }
}
