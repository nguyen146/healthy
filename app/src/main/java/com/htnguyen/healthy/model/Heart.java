package com.htnguyen.healthy.model;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Heart extends RealmObject{
    @PrimaryKey
    private long id;
    private int heart;
    private String title;
    private String timeStamp;

    public Heart() {

    }

    public Heart(int heart, String title, String timeStamp) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.heart = heart;
        this.title = title;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
