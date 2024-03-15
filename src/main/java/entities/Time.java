package entities;

import utils.StringFormatter;

public class Time {
    private String days;
    private int start;
    private int length;
    private String weeks;

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

    public String getWeeks() {
        return this.weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
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
            && this.getWeeks().equals(time.getWeeks());
    }

    @Override
    public int hashCode() {
        int result = days != null ? days.hashCode() : 0;
        result = 31 * result + start;
        result = 31 * result + length;
        result = 31 * result + (weeks != null ? weeks.hashCode() : 0);
        return result;
    }

}
