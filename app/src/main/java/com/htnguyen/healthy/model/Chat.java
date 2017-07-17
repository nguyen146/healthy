package com.htnguyen.healthy.model;

import java.util.List;

public class Chat {
    private String key;
    private String sendId;
    private String sendMessage;
    private long timeStamp;
    private List<TimerSender> timer;

    public Chat() {
    }

    public Chat(String sendId, String sendMessage, long timeStamp) {
        this.sendId = sendId;
        this.sendMessage = sendMessage;
        this.timeStamp = timeStamp;
    }

    public Chat(String sendId, String sendMessage, long timeStamp, List<TimerSender> timer) {
        this.sendId = sendId;
        this.sendMessage = sendMessage;
        this.timeStamp = timeStamp;
        this.timer = timer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<TimerSender> getTimer() {
        return timer;
    }

    public void setTimer(List<TimerSender> timer) {
        this.timer = timer;
    }
}
