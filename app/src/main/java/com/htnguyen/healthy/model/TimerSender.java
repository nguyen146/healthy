package com.htnguyen.healthy.model;


public class TimerSender {

    private String title;
    private String description;
    private long wakeUpTime;

    public TimerSender() {
    }

    public TimerSender(String title, String description, long wakeUpTime) {
        this.title = title;
        this.description = description;
        this.wakeUpTime = wakeUpTime;
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

    public long getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(long wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }
}
