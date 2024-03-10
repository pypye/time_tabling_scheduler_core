package entities;

import utils.StringFormatter;

public class Time {
    private String days;
    private int start;
    private int length;
    private String week;

    private int penalty;

    public Time() {
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

    public int getEnd() {
        return this.start + this.length;
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

    public int getPenalty() {
        return this.penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }

    public boolean getEquals(Time obj) {
        return this.getStart() == obj.getStart()
            && this.getEnd() == obj.getEnd()
            && this.getDays().equals(obj.getDays())
            && this.getWeek().equals(obj.getWeek());
    }
}
