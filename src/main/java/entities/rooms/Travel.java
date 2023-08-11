//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package entities.rooms;

import utils.StringFormatter;

public class Travel {
    private String room;
    private int value;

    public Travel() {
    }

    public Travel(String room, int value) {
        this.room = room;
        this.value = value;
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

    public String toString() {
        return StringFormatter.printObject(this);
    }
}
