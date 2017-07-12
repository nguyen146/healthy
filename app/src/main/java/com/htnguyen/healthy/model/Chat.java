package com.htnguyen.healthy.model;

public class Chat {
    private String key;
    private String sendId;
    private String sendMessage;
    private long timeStamp;

    public Chat() {
    }

    public Chat(String sendId, String sendMessage, long timeStamp) {
        this.sendId = sendId;
        this.sendMessage = sendMessage;
        this.timeStamp = timeStamp;
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
}
