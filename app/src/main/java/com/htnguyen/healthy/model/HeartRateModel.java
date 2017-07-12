package com.htnguyen.healthy.model;

public class HeartRateModel {

    private final long sinal;
    private final long time;

    public HeartRateModel(long sinal, long time) {
        this.sinal = sinal;
        this.time = time;
    }

    public long getSinal() {
        return sinal;
    }

    public long getTime() {
        return time;
    }
}
