package com.htnguyen.healthy.model;

public class Items {
    private String date;
    private String description;
    private float value;

    public Items(String date, String description, float value) {
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
