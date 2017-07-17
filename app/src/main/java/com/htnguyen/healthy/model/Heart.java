package com.htnguyen.healthy.model;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Heart extends RealmObject{
    @PrimaryKey
    private long id;
    private int heart;
    private int status;
    private Date timeStamp;

    public Heart() {

    }

    public Heart(int heart, int status, Date timeStamp) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.heart = heart;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
