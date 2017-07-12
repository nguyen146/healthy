package com.htnguyen.healthy.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Timer extends RealmObject implements Parcelable {
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

    protected Timer(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        pendingId = in.readInt();
        wakeUpTime = new Date(in.readLong());
    }

    public static final Creator<Timer> CREATOR = new Creator<Timer>() {
        @Override
        public Timer createFromParcel(Parcel in) {
            return new Timer(in);
        }

        @Override
        public Timer[] newArray(int size) {
            return new Timer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(pendingId);
        dest.writeLong(wakeUpTime.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
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
