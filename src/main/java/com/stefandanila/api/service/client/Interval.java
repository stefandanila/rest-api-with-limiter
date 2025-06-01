package com.stefandanila.api.service.client;

import java.time.temporal.ChronoUnit;

public class Interval {
    private int value;
    private ChronoUnit unit;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        this.unit = unit;
    }
}
