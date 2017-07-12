package com.htnguyen.healthy.model;

public class Items {
    private String date;
    private String description;
    private int value;

    public Items(String date, String description, int value) {
        this.date = date;
        this.description = description;
        this.value = value;
    }

    public Items() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
