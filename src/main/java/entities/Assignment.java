package entities;

import com.google.ortools.sat.Literal;
import entities.courses.Class;
import entities.rooms.Room;

public class Assignment {
    private Class clazz;
    private Room room;
    private Time time;

    private Literal literal;

    public Assignment(Class clazz, Room room, Time time, Literal literal) {
        this.clazz = clazz;
        this.room = room;
        this.time = time;
        this.literal = literal;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Literal getLiteral() {
        return literal;
    }

    public void setLiteral(Literal literal) {
        this.literal = literal;
    }
}
