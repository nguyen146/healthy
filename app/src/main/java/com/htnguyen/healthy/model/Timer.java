package com.htnguyen.healthy.model;


import android.os.Parcel;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Timer extends RealmObject{
    @PrimaryKey
    private long id;
    private String title;
    private String description;
    private int pendingId;
    private Date wakeUpTime;

    public Timer() {
    }

    public Timer(String title, String description, int pendingId, Date wakeUpTime) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.title = title;
        this.description = description;
        this.pendingId = pendingId;
        this.wakeUpTime = wakeUpTime;
    }

    public Timer(String title, String description, Date wakeUpTime) {
        this.title = title;
        this.description = description;
        this.wakeUpTime = wakeUpTime;
    }

    protected Timer(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        pendingId = in.readInt();
        wakeUpTime = new Date(in.readLong());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPendingId() {
        return pendingId;
    }

    public void setPendingId(int pendingId) {
        this.pendingId = pendingId;
    }

    public Date getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(Date wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }
}
