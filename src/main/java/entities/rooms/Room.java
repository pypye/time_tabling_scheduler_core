package entities.rooms;

import entities.Time;
import utils.StringFormatter;

import java.util.List;

public class Room {
    private String id;
    private int capacity;
    private List<Travel> travelList;
    private List<Time> unavailableList;

    private int penalty;

    public Room() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Travel> getTravelList() {
        return this.travelList;
    }

    public void setTravelList(List<Travel> travelList) {
        this.travelList = travelList;
    }

    public List<Time> getUnavailableList() {
        return this.unavailableList;
    }

    public void setUnavailableList(List<Time> unavailableList) {
        this.unavailableList = unavailableList;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Room room)) {
            return false;
        }
        return id.equals(room.getId());
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(id);
    }
}
