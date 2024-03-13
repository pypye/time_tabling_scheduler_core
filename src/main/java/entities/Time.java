package entities;

import utils.StringFormatter;

public class Time {
    private String days;
    private int start;
    private int length;
    private String week;

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

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Time time)) {
            return false;
        }
        return this.getStart() == time.getStart()
            && this.getEnd() == time.getEnd()
            && this.getDays().equals(time.getDays())
            && this.getWeek().equals(time.getWeek());
    }

    @Override
    public int hashCode() {
        int result = days != null ? days.hashCode() : 0;
        result = 31 * result + start;
        result = 31 * result + length;
        result = 31 * result + (week != null ? week.hashCode() : 0);
        return result;
    }

}
