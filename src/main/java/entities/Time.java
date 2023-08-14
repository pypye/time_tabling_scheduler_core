package entities;

import utils.StringFormatter;

public class Time {
    private String days;
    private int start;
    private int length;
    private String week;

    public Time() {
    }

    public Time(String days, int start, int length, String week) {
        this.days = days;
        this.start = start;
        this.length = length;
        this.week = week;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
