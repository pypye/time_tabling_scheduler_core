package entities.rooms;

import utils.StringFormatter;

public class Travel {
    private String room;
    private int value;

    public Travel() {
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }
}
