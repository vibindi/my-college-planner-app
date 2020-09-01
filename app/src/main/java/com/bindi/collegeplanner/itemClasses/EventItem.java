package com.bindi.collegeplanner.itemClasses;

import java.util.Calendar;

public class EventItem {

    private String title;
    private int year;
    private int month;
    private int day;
    private String notes;
    private String collegeName;
    private String colorStr;

    public EventItem(String title, int year, int month, int day) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.notes = "";
    }

    public EventItem(String title, int year, int month, int day, String collegeName, String colorStr) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.notes = notes;
        this.collegeName = collegeName;
        this.colorStr = colorStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }
}
