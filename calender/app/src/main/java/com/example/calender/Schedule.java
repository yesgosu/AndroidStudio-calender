package com.example.calender;

public class Schedule {
    public final String title;
    public final int year;
    public final int month;
    public final int date;
    public final int hour;
    public final int minute;
    public final String point;

    public Schedule(String title, int year, int month, int date, int hour, int minute, String point) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.point = point;
    }
}
