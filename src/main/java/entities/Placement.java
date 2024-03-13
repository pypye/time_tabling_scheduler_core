package entities;

import entities.rooms.Room;

public class Placement {
    private Room room;
    private Time time;

    public Placement(Room room, Time time) {
        this.room = room;
        this.time = time;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Placement placement)) {
            return false;
        }
        if (placement.getRoom() == null || room == null) {
            if (placement.getRoom() == null && room == null) {
                return time.equals(placement.getTime());
            }
            return false;

        }
        return room.equals(placement.getRoom()) && time.equals(placement.getTime());
    }

    @Override
    public int hashCode() {
        int result = room != null ? room.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
