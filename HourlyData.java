package com.example.wthapp;

public class HourlyData {
    private final String hour;
    private final double temp;
    private final String desc;

    public HourlyData(String hour, double temp, String desc) {
        this.hour = hour;
        this.temp = temp;
        this.desc = desc;
    }

    public String getHour() { return hour; }
    public double getTemp() { return temp; }
    public String getDesc() { return desc; }
}
